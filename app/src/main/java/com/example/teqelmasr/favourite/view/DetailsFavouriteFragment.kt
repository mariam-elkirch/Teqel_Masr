package com.example.teqelmasr.favourite.view

import DraftOrder
import FavouriteProduct
import LineItem
import NoteAttribute
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.teqelmasr.R
import com.example.teqelmasr.authentication.login.LoginActivity
import com.example.teqelmasr.databinding.FragmentDetailsFavouriteBinding
import com.example.teqelmasr.displayEquipmentRent.view.DisplayEquipmentRentFragmentArgs
import com.example.teqelmasr.displaySparePart.view.DetailsSparePartFragmentDirections
import com.example.teqelmasr.favourite.viewModel.AddToFavoriteViewModel
import com.example.teqelmasr.favourite.viewModel.AddToFavoriteViewModelFactory
import com.example.teqelmasr.helper.Constants
import com.example.teqelmasr.model.ContactInfo
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import favCustomer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailsFavouriteFragment : Fragment() {
    private val args by navArgs<DetailsFavouriteFragmentArgs>()
    private val user = Firebase.auth.currentUser
   private lateinit var product : FavouriteProduct
  lateinit var contactInfo : ContactInfo
    private val binding by lazy {FragmentDetailsFavouriteBinding.inflate(layoutInflater)}
    private var isFavorite : Boolean = true
    private val viewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            factory = AddToFavoriteViewModelFactory(
                Repository.getInstance(
                    Client.getInstance(),
                    requireContext()
                )
            )
        )[AddToFavoriteViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showProgressDialog()

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.getProductDetails(args.productID)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.apply {
            Log.i("TAG", "onCreateView: ${args.productID}")
            viewModel.productDetailsLiveData.observe(requireActivity()) {
            Glide.with(activity?.baseContext!!).load(it.product.images?.get(0)?.src).centerCrop()
                .placeholder(R.drawable.placeholder).into(imageItem)
                titleTxt.text = it.product.title
                priceTxt.text = it.product.variants?.get(0)?.price.toString()
                val dateInString = it.product.updated_at.toString()
                if (dateInString != null) {
                    val dateAfterCut = dateInString.substringBefore('T')
                    dateTxt.text = "${dateAfterCut}"
                }
                categoryTxt.text = context?.resources?.getString(R.string.EquipmentRent)
                typeTxt.text = it.product.productType
                vendorTxt.text = it.product.templateSuffix
                productDesc.text = it.product.bodyHtml
                 contactInfo = ContactInfo(it.product.variants?.get(0)?.sku.toString(),it.product.variants?.get(0)?.title.toString())
                val image = listOf(NoteAttribute(name = "image",value = it.product.images?.get(0)?.src))
                val productInfo = listOf(LineItem(productID = it.product!!.variants?.get(0)!!.product_id!!,variant_id =it.product!!.variants?.get(0)!!.id!! ,taxable = false,title = it.product!!.title!!,1, it.product.variants?.get(0)?.price.toString()))
                product = FavouriteProduct(DraftOrder(user?.email?:"unknown user",note = "",noteAttributes = image,lineItems = productInfo , customer = favCustomer(email = user?.email, firstName =user?.displayName,phone = user?.phoneNumber)) )
                if (isFavorite){
                    favIcon?.visibility = View.GONE
                    favFillIcon?.visibility = View.VISIBLE
                }else{
                    favFillIcon?.visibility = View.GONE
                    favIcon?.visibility = View.VISIBLE
                }
                favFillIcon.setOnClickListener {
                    viewModel.deleteFavProduct(args.draftOrderID)
                    favIcon.visibility = View.VISIBLE
                    favFillIcon.visibility = View.GONE

                }
                favIcon.setOnClickListener {
                    if(product!=null){
                        favIcon.visibility = View.GONE
                        favFillIcon?.visibility = View.VISIBLE
                        viewModel.addToFavorite(product!!)
                    }

                }
               showButton.setOnClickListener{
                    if (user != null) {


                        val action = DetailsFavouriteFragmentDirections.actionDetailsFavouriteFragmentToContactInfoFragment2(contactInfo,
                            Constants.RENT_SOURCE)
                        binding.root.findNavController().navigate(action)
                    }else {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.loginContactInfo),
                            Snackbar.LENGTH_INDEFINITE
                        ).setAction(getString(R.string.login)) {
                            startActivity(Intent(requireContext(), LoginActivity::class.java))
                        }.setDuration(6000).show()
                    }

                }
        }

        }
        return binding.root
    }
    private fun showProgressDialog(){
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.custom_progress)
        CoroutineScope(Dispatchers.Main).launch {
            dialog.show()
            delay(1000)
            dialog.dismiss()
        }
    }
}