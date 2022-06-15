package com.example.teqelmasr.displayEquipmentRent.view

import DraftOrder
import FavouriteProduct
import LineItem
import NoteAttribute
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.teqelmasr.R
import com.example.teqelmasr.displayEquipmentRent.viewModel.DisplayRentEquipmentViewModel
import com.example.teqelmasr.displayEquipmentRent.viewModel.DisplayRentEquipmentViewModelFactory
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client


class DetailsEquipmentRentFragment : Fragment() {
    private val args by navArgs<DetailsEquipmentRentFragmentArgs>()
    lateinit var viewModel : DisplayRentEquipmentViewModel
    private lateinit var viewModelFactory : DisplayRentEquipmentViewModelFactory
    private  var favProduct : FavouriteProduct? = null
    private val sharedPrefFile = "favorite"
    private var isFavorite : Boolean = false
    private var sharedPreferences: SharedPreferences? = null
    private var sharedProductIDs = mutableSetOf<String>()
    private var productID : Long? = 0
   // private var productIdSet = mutableSetOf<String>()
    var editor:SharedPreferences.Editor? =null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences  = requireActivity().getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       getSavedFavorite()
        productID = args.product.variants?.get(0)?.product_id
        val view:View = inflater.inflate(R.layout.fragment_details_equipment_rent, container, false)
        setUI(view)
        viewModelFactory = DisplayRentEquipmentViewModelFactory(
            Repository.getInstance(Client.getInstance(),requireContext())
        )
        viewModel = ViewModelProvider(requireActivity(),viewModelFactory)[DisplayRentEquipmentViewModel::class.java]
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
    category?.text = "Equipment For Rent"
    var type = view.findViewById<TextView>(R.id.type_txt)
    type?.text = args.product.productType
    var manufactor = view.findViewById<TextView>(R.id.vendor_txt)
    manufactor?.text = args.product.templateSuffix
    var description = view.findViewById<TextView>(R.id.product_desc)
    description?.text = args.product.bodyHtml
    var productImg = view.findViewById<ImageView>(R.id.image_item)
    Glide.with(activity?.baseContext!!).load(args.product.image?.src).centerCrop()
        .placeholder(R.drawable.placeholder).into(productImg)
    val image = listOf(NoteAttribute(name = "image",value = args.product.image?.src))
    val productInfo = listOf(LineItem(productID = args.product!!.variants?.get(0)!!.product_id!!,variant_id =args.product!!.variants?.get(0)!!.id!! ,taxable = false,title = args.product!!.title!!,1, args.product.variants?.get(0)?.price.toString()))
    var product = FavouriteProduct(DraftOrder(email = "noor@gmail.com",note = "",noteAttributes = image,lineItems = productInfo ) )
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
        favIcon.visibility = View.GONE
        addedToFavorite?.visibility = View.VISIBLE
        viewModel.addToFavorite(product)
        // the post response take into object
        viewModel.favouriteResponse.observe(requireActivity()) {
            favProduct = FavouriteProduct(it.draftOrder)
            saveFavorite(favProduct!!)
        }
    }

    addedToFavorite?.setOnClickListener {
        if (favProduct!=null) {
            viewModel.deleteFavProduct(favProduct!!)
        }
        removeFromShared(productID.toString())
        addedToFavorite?.visibility = View.GONE
        favIcon?.visibility = View.VISIBLE

    }
}
private fun saveFavorite(product : FavouriteProduct){
     editor = sharedPreferences!!.edit()
    Log.i("TAG", "saveFavorite Before add: ${sharedProductIDs.size}")
    sharedProductIDs.add(product.draftOrder.lineItems[0].productID.toString() )
    Log.i("TAG", "saveFavorite after add: ${sharedProductIDs.size}")

    editor?.putStringSet("favID",sharedProductIDs)
    editor?.apply()
    editor?.commit()
}
private fun getSavedFavorite() {
    productID = args.product.variants?.get(0)?.product_id
    Log.i("TAG", "getSavedFavorite: hello ${sharedProductIDs.size}")
    sharedProductIDs = sharedPreferences!!.getStringSet("favID", mutableSetOf())!!
    Log.i("TAG", "getSavedFavorite: ${sharedProductIDs.size}")
    if (sharedProductIDs.isNotEmpty()){
        isFavorite = productID.toString() in sharedProductIDs
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
                Log.i("TAG", "onCreateView: yes iam fav item")
                // take the response in object to send it to delete method
                favProduct = FavouriteProduct(it[0])
            } else {
                Log.i("TAG", "onCreateView:  iam not fav item")
            }
        }
    }
}