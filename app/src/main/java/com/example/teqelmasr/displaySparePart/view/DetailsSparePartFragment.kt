package com.example.teqelmasr.displaySparePart.view

import DraftOrder
import FavouriteProduct
import LineItem
import NoteAttribute
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.teqelmasr.R
import com.example.teqelmasr.authentication.login.LoginActivity
import com.example.teqelmasr.databinding.FragmentDetailsSparePartBinding
import com.example.teqelmasr.displayEquipmentRent.view.DetailsEquipmentRentFragmentArgs
import com.example.teqelmasr.displayEquipmentSell.view.DetailsEquipmentSellFragmentDirections
import com.example.teqelmasr.displayEquipmentSell.viewModel.DisplayEquipmentSellViewModel
import com.example.teqelmasr.displayEquipmentSell.viewModel.DisplayEquipmentSellViewModelFactory
import com.example.teqelmasr.displaySparePart.viewModel.DisplaySparPartsViewModelFactory
import com.example.teqelmasr.displaySparePart.viewModel.DisplaySparePartsViewModel
import com.example.teqelmasr.helper.Constants
import com.example.teqelmasr.model.ContactInfo
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import favCustomer


class DetailsSparePartFragment : Fragment() {
    private val user = Firebase.auth.currentUser
    private val binding by lazy { FragmentDetailsSparePartBinding.inflate(layoutInflater) }
    lateinit var viewModel : DisplaySparePartsViewModel
    private lateinit var viewModelFactory : DisplaySparPartsViewModelFactory
    private val args by navArgs<DetailsSparePartFragmentArgs>()
    private  var favProduct : FavouriteProduct? = null
    var product : FavouriteProduct? = null
    private val sharedPrefFile = "favorite"
    private var isFavorite : Boolean = false
    private var sharedPreferences: SharedPreferences? = null
    private var sharedProductIDs = mutableSetOf<String>()
    private var productID : Long? = 0


    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences  = requireActivity().getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)
        viewModelFactory = DisplaySparPartsViewModelFactory(
            Repository.getInstance(Client.getInstance(),requireContext())
        )
        viewModel = ViewModelProvider(requireActivity(),viewModelFactory)[DisplaySparePartsViewModel::class.java]
        viewModel.getFavProducts()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        productID = args.product.variants?.get(0)?.product_id
        getSavedFavorite()
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        setUI()
        getFavoriteProduct()
        return binding.root
    }
    private fun  setUI(){
        binding.apply {
            titleTxt.text = args.product.title
            priceTxt.text =
                "${args.product.variants?.get(0)?.price.toString()} " + getString(R.string.currency)
            when (args.product.tags) {
                Constants.SPARE_TAG -> categoryTxt.text =
                    context?.resources?.getString(R.string.spare_parts)
                Constants.RENT_EQ_TAG -> categoryTxt.text =
                    context?.resources?.getString(R.string.EquipmentRent)
                Constants.SELL_EQ_TAG -> categoryTxt.text =
                    context?.resources?.getString(R.string.EquipmentSell)
            }
            //args.product.tags
            typeTxt.text = args.product.productType
            productDesc.text = args.product.bodyHtml
            vendorTxt.text = args.product.templateSuffix
            dateTxt.text = args.product.published_at?.slice(IntRange(0, 9))
            Glide.with(requireContext()).load(args.product.image?.src).centerCrop()
                .placeholder(R.drawable.placeholder).into(binding.imageItem)
            binding.showButton.setOnClickListener {
                if (user != null) {
                    Log.i("TAG", " User is signed in")
                    Log.i("tag", args.product.variants?.get(0)?.title.toString() + "args title")
                    var contact = ContactInfo(
                        args.product.variants?.get(0)?.sku.toString(),
                        args.product.variants?.get(0)?.title.toString()
                    )
                    val action =
                        DetailsSparePartFragmentDirections.actionDetailsSparePartFragmentToContactInfoFragment(
                            contact,
                            Constants.RENT_SOURCE
                        )
                    binding.root.findNavController().navigate(action)
                } else {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.loginContactInfo),
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(getString(R.string.login)) {
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                    }.setDuration(6000).show()
                }

            }
            if (user != null) {
                val image = listOf(NoteAttribute(name = "image", value = args.product.image?.src))
                val productInfo = listOf(
                    LineItem(
                        productID = args.product!!.variants?.get(0)!!.product_id!!,
                        variant_id = args.product!!.variants?.get(0)!!.id!!,
                        taxable = false,
                        title = args.product!!.title!!,
                        1,
                        args.product.variants?.get(0)?.price.toString()
                    )
                )
                product = FavouriteProduct(
                    DraftOrder(
                        user.email ?: "unknown user",
                        note = "",
                        noteAttributes = image,
                        lineItems = productInfo,
                        customer = favCustomer(
                            email = user.email,
                            firstName = user.displayName,
                            phone = user.phoneNumber
                        )
                    )
                )
            }
            if (isFavorite){
                favIcon?.visibility = View.GONE
                favFillIcon?.visibility = View.VISIBLE
            }else{
                favFillIcon?.visibility = View.GONE
                favIcon?.visibility = View.VISIBLE
            }
            favIcon.setOnClickListener {
                if (user != null && product != null) {
                    favIcon.visibility = View.GONE
                    favFillIcon?.visibility = View.VISIBLE
                    viewModel.addToFavorite(product!!)
                    // the post response take into object
                    viewModel.favouriteResponse.observe(requireActivity()) {
                        favProduct = FavouriteProduct(it.draftOrder)
                        saveFavorite(favProduct!!)
                        if (isAdded) {
                            Toast.makeText(activity, R.string.addedToFav, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.have_to_login),
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(getString(R.string.login)) {
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                    }.setDuration(6000).show()
                }
            }
            favFillIcon.setOnClickListener {
                if (favProduct != null) {
                    viewModel.deleteFavProduct(favProduct!!)
                    if (isAdded) {
                        Toast.makeText(context, R.string.deleteFromFav, Toast.LENGTH_SHORT).show()
                    }
                }
                removeFromShared(productID.toString())
                favFillIcon?.visibility = View.GONE
                favIcon?.visibility = View.VISIBLE
            }
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
                    sharedProductIDs = sharedPreferences!!.getStringSet("favID", mutableSetOf())!!
                    if (sharedProductIDs.isNotEmpty()) {
                        isFavorite = productID.toString() in sharedProductIDs
                    }
                    if (isFavorite) {
                        binding.apply {
                            favIcon?.visibility = View.GONE
                            favFillIcon?.visibility = View.VISIBLE
                        }
                    } else {
                        binding.apply {
                            favFillIcon?.visibility = View.GONE
                            favIcon?.visibility = View.VISIBLE
                        }

                    }
                }
            }
            /*
            viewModel.allFavListLiveData.observe(requireActivity()) {
                for (fav in it) {
                    favProduct = FavouriteProduct(fav)
                    saveFavorite(favProduct ?: null)
                    getSavedFavorite(view)
                    setUI(view)
                }
            }
             */
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