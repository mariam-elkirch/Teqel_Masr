package com.example.teqelmasr.displayEquipmentSell.view

import DraftOrder
import FavouriteProduct
import LineItem
import NoteAttribute
import android.content.Context
import android.content.SharedPreferences
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.teqelmasr.R
import com.example.teqelmasr.addEquipmentSell.view.AddEquipmentSellFragmentDirections
import com.example.teqelmasr.authentication.login.LoginActivity
import com.example.teqelmasr.displayEquipmentRent.view.DetailsEquipmentRentFragmentArgs
import com.example.teqelmasr.helper.Constants
import com.example.teqelmasr.displayEquipmentRent.viewModel.DisplayRentEquipmentViewModel
import com.example.teqelmasr.displayEquipmentRent.viewModel.DisplayRentEquipmentViewModelFactory
import com.example.teqelmasr.displayEquipmentSell.viewModel.DisplayEquipmentSellViewModel
import com.example.teqelmasr.displayEquipmentSell.viewModel.DisplayEquipmentSellViewModelFactory
import com.example.teqelmasr.model.ContactInfo
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth

import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import favCustomer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailsEquipmentSellFragment : Fragment() {
    private val user = Firebase.auth.currentUser
    private val args by navArgs<DetailsEquipmentSellFragmentArgs>()
    lateinit var viewModel : DisplayEquipmentSellViewModel
    private lateinit var viewModelFactory : DisplayEquipmentSellViewModelFactory
    private  var favProduct : FavouriteProduct? = null
    private val sharedPrefFile = "favorite"
    private var isFavorite : Boolean = false
    private var sharedPreferences: SharedPreferences? = null
    private var sharedProductIDs = mutableSetOf<String>()
    private var productID : Long? = 0
    var product : FavouriteProduct? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences  = requireActivity().getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)
        viewModelFactory = DisplayEquipmentSellViewModelFactory(
            Repository.getInstance(Client.getInstance(),requireContext())
        )
        viewModel = ViewModelProvider(requireActivity(),viewModelFactory)[DisplayEquipmentSellViewModel::class.java]
        viewModel.getFavProducts()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_details_equipment_sell, container, false)
        if (user != null) {
            Log.i("TAG", " User is signed in")
        }else {
            Log.i("TAG", " No user is signed in")
        }
        productID = args.productsell.variants?.get(0)?.product_id
        getSavedFavorite(view)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        setUI(view)
        getFavoriteProduct()
        return view
    }

    private fun setUI(view : View){
        var title: TextView = view.findViewById(R.id.title_txt)
        title.text= args.productsell.title
        var price: TextView = view.findViewById(R.id.price_txt)
        price.text= args.productsell.variants?.get(0)?.price.toString()
        var date: TextView = view.findViewById(R.id.date_txt)
        var show: Button = view.findViewById(R.id.showButton)
        show.setOnClickListener{
            if (user != null) {
                Log.i("TAG", " User is signed in")
                Log.i("tag",args.productsell.variants?.get(0)?.title.toString()+"args title")
                var contact = ContactInfo(args.productsell.variants?.get(0)?.sku.toString(),args.productsell.variants?.get(0)?.title.toString())
                val action = DetailsEquipmentSellFragmentDirections.actionDetailsEquipmentSellFragmentToContactInfoFragment(contact,
                    Constants.SELL_SOURCE)
                view.findNavController().navigate(action)
            }else {
                Snackbar.make(
                    view,
                    getString(R.string.loginContactInfo),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(getString(R.string.login)) {
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                }.setDuration(6000).show()
            }

        }
        val dateInString = args.productsell.updated_at
        if (dateInString != null) {
            val dateAfterCut = dateInString.substringBefore('T')
            date.text = "${dateAfterCut}"
        }



        var category = view.findViewById<TextView>(R.id.category_txt)
        category.text = "Equipment For Sell"
        var type = view.findViewById<TextView>(R.id.type_txt)
        type.text = args.productsell.productType
        var manufactor = view.findViewById<TextView>(R.id.vendor_txt)
        manufactor.text = args.productsell.templateSuffix
        var description = view.findViewById<TextView>(R.id.product_desc)
        description.text = args.productsell.bodyHtml
        var productImg = view.findViewById<ImageView>(R.id.image_item)
        Glide.with(activity?.baseContext!!).load(args.productsell.image?.src).centerCrop()
            .placeholder(R.drawable.placeholder).into(productImg)
        if (user!=null){
            val image = listOf(NoteAttribute(name = "image",value = args.productsell.image?.src))
            val productInfo = listOf(LineItem(productID = args.productsell!!.variants?.get(0)!!.product_id!!,variant_id =args.productsell!!.variants?.get(0)!!.id!! ,taxable = false,title = args.productsell!!.title!!,1, args.productsell.variants?.get(0)?.price.toString()))
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
    private fun getSavedFavorite(view: View) {
        productID = args.productsell.variants?.get(0)?.product_id
        sharedProductIDs = sharedPreferences!!.getStringSet("favID", mutableSetOf())!!
        if (sharedProductIDs.isNotEmpty()) {
            isFavorite = productID.toString() in sharedProductIDs
        }
        if (sharedProductIDs.size == 0) {
            viewModel.allFavListLiveData.observe(requireActivity()) {
                for (fav in it) {
                    favProduct = FavouriteProduct(fav)
                    saveFavorite(favProduct ?: null)
                    getSavedFavorite(view)
                    setUI(view)
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
     //   viewModel.getFavProduct(productID!!)
        viewModel.favListLiveData.observe(requireActivity()) {
            if (viewModel.favListLiveData.value?.size != 0) {
                favProduct = FavouriteProduct(it[0])
            }
        }

}
}