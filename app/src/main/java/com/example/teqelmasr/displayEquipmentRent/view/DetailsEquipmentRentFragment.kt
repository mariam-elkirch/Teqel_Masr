package com.example.teqelmasr.displayEquipmentRent.view
import android.widget.Toast
import DraftOrder
import FavouriteProduct
import LineItem
import NoteAttribute
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.teqelmasr.R
import com.example.teqelmasr.displayEquipmentRent.viewModel.DisplayRentEquipmentViewModel
import com.example.teqelmasr.displayEquipmentRent.viewModel.DisplayRentEquipmentViewModelFactory
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import favCustomer


class DetailsEquipmentRentFragment : Fragment() {
    private val user = Firebase.auth.currentUser
    private val args by navArgs<DetailsEquipmentRentFragmentArgs>()
    lateinit var viewModel : DisplayRentEquipmentViewModel
    private lateinit var viewModelFactory : DisplayRentEquipmentViewModelFactory
    private  var favProduct : FavouriteProduct? = null
    private val sharedPrefFile = "favorite"
    private var isFavorite : Boolean = false
    private var sharedPreferences: SharedPreferences? = null
    private var sharedProductIDs = mutableSetOf<String>()
    private var productID : Long? = 0
     var product : FavouriteProduct? = null
  //  private var productIdSet = mutableSetOf<String>()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences  = requireActivity().getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)
        viewModelFactory = DisplayRentEquipmentViewModelFactory(
            Repository.getInstance(Client.getInstance(),requireContext())
        )
        viewModel = ViewModelProvider(requireActivity(),viewModelFactory)[DisplayRentEquipmentViewModel::class.java]
        viewModel.getFavProducts()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (user != null) {
            Log.i("TAG", " User is signed in")
        }else {
            Log.i("TAG", " No user is signed in")
        }
        getSavedFavorite()
        productID = args.product.variants?.get(0)?.product_id
        val view:View = inflater.inflate(R.layout.fragment_details_equipment_rent, container, false)
        setUI(view)
        getFavoriteProduct()
        return view

    }
private fun setUI(view : View){
    var title:TextView = view.findViewById(R.id.title_txt)
    title.text= args.product.title
    var price:TextView = view.findViewById(R.id.price_txt)
    price.text= args.product.variants?.get(0)?.price.toString()
    var date:TextView = view.findViewById(R.id.date_txt)
    val dateInString = args.product.updated_at
    if (dateInString != null) {
        val dateAfterCut = dateInString.substringBefore('T')
        date.text = "${dateAfterCut}"
    }
    Log.i("TAG", "onCreateView: test product id ${productID} ${isFavorite} ")
    var category = view.findViewById<TextView>(R.id.category_txt)
    category?.text = context?.resources?.getString(R.string.EquipmentRent)
    var type = view.findViewById<TextView>(R.id.type_txt)
    type?.text = args.product.productType
    var manufactor = view.findViewById<TextView>(R.id.vendor_txt)
    manufactor?.text = args.product.templateSuffix
    var description = view.findViewById<TextView>(R.id.product_desc)
    description?.text = args.product.bodyHtml
    var productImg = view.findViewById<ImageView>(R.id.image_item)
    Glide.with(activity?.baseContext!!).load(args.product.image?.src).centerCrop()
        .placeholder(R.drawable.placeholder).into(productImg)
   if (user!=null){
    val image = listOf(NoteAttribute(name = "image",value = args.product.image?.src))
    val productInfo = listOf(LineItem(productID = args.product!!.variants?.get(0)!!.product_id!!,variant_id =args.product!!.variants?.get(0)!!.id!! ,taxable = false,title = args.product!!.title!!,1, args.product.variants?.get(0)?.price.toString()))
     product = FavouriteProduct(DraftOrder(user.email?:"unknown user",note = "",noteAttributes = image,lineItems = productInfo , customer = favCustomer(email = user.email, firstName =user.displayName,phone = user.phoneNumber)) )
 }
    var favIcon = view?.findViewById<ImageView>(R.id.fav_icon)
    var addedToFavorite = view?.findViewById<ImageView>(R.id.favFill_icon)
    if (isFavorite){
        favIcon?.visibility = View.GONE
        addedToFavorite?.visibility = View.VISIBLE
    }else{
        addedToFavorite?.visibility = View.GONE
        favIcon?.visibility = View.VISIBLE
    }
    favIcon?.setOnClickListener {
         if (user!=null&&product!=null){
        favIcon.visibility = View.GONE
        addedToFavorite?.visibility = View.VISIBLE
        viewModel.addToFavorite(product!!)
        // the post response take into object
        viewModel.favouriteResponse.observe(requireActivity()) {
            favProduct = FavouriteProduct(it.draftOrder)
            saveFavorite(favProduct!!)
            Toast.makeText(activity, R.string.addedToFav, Toast.LENGTH_SHORT).show()

        }
        } else{
            Toast.makeText(activity, R.string.signInFirst, Toast.LENGTH_SHORT).show()
        }
    }

    addedToFavorite?.setOnClickListener {
        if (favProduct!=null) {
            viewModel.deleteFavProduct(favProduct!!)
            Toast.makeText(context, R.string.deleteFromFav, Toast.LENGTH_SHORT).show()

        }
        removeFromShared(productID.toString())
        addedToFavorite?.visibility = View.GONE
        favIcon?.visibility = View.VISIBLE

    }
}
private fun saveFavorite(product : FavouriteProduct?){
    var editor:SharedPreferences.Editor = sharedPreferences!!.edit()
    sharedProductIDs.add(product?.draftOrder!!.lineItems[0].productID.toString() )
    editor.clear()
    editor.putStringSet("favID",sharedProductIDs)
   editor.apply()
    editor.commit()
}
private fun getSavedFavorite() {
    productID = args.product.variants?.get(0)?.product_id
    sharedProductIDs = sharedPreferences!!.getStringSet("favID", mutableSetOf())!!
    if (sharedProductIDs.isNotEmpty()) {
        isFavorite = productID.toString() in sharedProductIDs
    }
    if (sharedProductIDs.size == 0) {
        viewModel.allFavListLiveData.observe(requireActivity()) {
            for (fav in it) {
                favProduct = FavouriteProduct(fav)
                saveFavorite(favProduct ?: null)
                getSavedFavorite()
                setUI(requireView())
            }
        }
    }
}
    private fun removeFromShared(id : String) {
        sharedProductIDs.remove(id)
         sharedPreferences?.edit()?.clear()?.commit()
        sharedPreferences?.edit()?.putStringSet("favID",sharedProductIDs)?.commit()

    }
    private fun getFavoriteProduct(){
        viewModel.getFavProduct(productID!!)
        viewModel.favListLiveData.observe(requireActivity()) {
            if (viewModel.favListLiveData.value?.size != 0) {
                // take the response in object to send it to delete method
                favProduct = FavouriteProduct(it[0])
            } else {
            }
        }
    }
}