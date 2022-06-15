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
import com.example.teqelmasr.favourite.viewModel.AddToFavoriteViewModel
import com.example.teqelmasr.favourite.viewModel.AddToFavoriteViewModelFactory
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client

class DetailsEquipmentRentFragment : Fragment() {
    private val args by navArgs<DetailsEquipmentRentFragmentArgs>()
    lateinit var viewModel : DisplayRentEquipmentViewModel
    lateinit var viewModelFactory : DisplayRentEquipmentViewModelFactory
    lateinit var favProduct : FavouriteProduct
    private val sharedPrefFile = "favorite"
    private var isFavorite : Boolean = false
    private val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)
    private var sharedProductID : Long = 0
    private var sharedDraftID : Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {

        getSavedFavorite()
        val view:View = inflater.inflate(R.layout.fragment_details_equipment_rent, container, false)
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



        var category = view.findViewById<TextView>(R.id.category_txt)
        category.text = "Equipment For Rent"
        var type = view.findViewById<TextView>(R.id.type_txt)
        type.text = args.product.productType
        var manufactor = view.findViewById<TextView>(R.id.vendor_txt)
        manufactor.text = args.product.templateSuffix
        var description = view.findViewById<TextView>(R.id.product_desc)
        description.text = args.product.bodyHtml
        var productImg = view.findViewById<ImageView>(R.id.image_item)
        Glide.with(activity?.baseContext!!).load(args.product.image?.src).centerCrop()
            .placeholder(R.drawable.placeholder).into(productImg)


        viewModelFactory = DisplayRentEquipmentViewModelFactory(
            Repository.getInstance(Client.getInstance(),requireContext())
        )
        viewModel = ViewModelProvider(requireActivity(),viewModelFactory)[DisplayRentEquipmentViewModel::class.java]

        val image = listOf(NoteAttribute(name = "image",value = args.product.image?.src))
        val productInfo = listOf(LineItem(productID = args.product!!.variants?.get(0)!!.product_id!!,variant_id =args.product!!.variants?.get(0)!!.id!! ,false,title = args.product!!.title!!,1, args.product.variants?.get(0)?.price.toString()))
        var product = FavouriteProduct(DraftOrder(email = "noor@gmail.com",note = "",noteAttributes = image,lineItems = productInfo ) )
        var favIcon = view?.findViewById<ImageView>(R.id.fav_icon)
        var addedToFavorite = view?.findViewById<ImageView>(R.id.favFill_icon)
        favIcon?.setOnClickListener {
            Log.i("TAG", "addProductToFavorite: image pressed ")
            favIcon.visibility = View.GONE
            addedToFavorite?.visibility = View.VISIBLE

            viewModel.addToFavorite(product)
            viewModel.favouriteRespose.observe(requireActivity()) {
                favProduct = FavouriteProduct(it.draftOrder)
                saveFavorite(favProduct)
            }

        }
        addedToFavorite?.setOnClickListener {
            viewModel.deleteFavProduct(favProduct)
            addedToFavorite?.visibility = View.GONE
            favIcon?.visibility = View.VISIBLE
        }



        return view
    }

private fun saveFavorite(product : FavouriteProduct){
    val editor:SharedPreferences.Editor =  sharedPreferences.edit()
    editor.putLong("product_id",product.draftOrder.lineItems[0].productID)
    editor.putLong("draft_id",product.draftOrder.id!!)
    editor.apply()
    editor.commit()
}
private fun getSavedFavorite(){
     sharedProductID = sharedPreferences.getLong("product_id",0)
     sharedDraftID = sharedPreferences.getLong("draft_id",0)
    isFavorite = !(sharedProductID.equals(0)) && !(sharedDraftID.equals("0"))
}


}