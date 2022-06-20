package com.example.teqelmasr.displaySparePart.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.teqelmasr.R
import com.example.teqelmasr.authentication.login.LoginActivity
import com.example.teqelmasr.databinding.FragmentDetailsSparePartBinding
import com.example.teqelmasr.displayEquipmentRent.view.DetailsEquipmentRentFragmentArgs
import com.example.teqelmasr.displayEquipmentSell.view.DetailsEquipmentSellFragmentDirections
import com.example.teqelmasr.helper.Constants
import com.example.teqelmasr.model.ContactInfo
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class DetailsSparePartFragment : Fragment() {
    private val user = Firebase.auth.currentUser
    private val binding by lazy { FragmentDetailsSparePartBinding.inflate(layoutInflater) }
    private val args by navArgs<DetailsSparePartFragmentArgs>()
    private var clicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding.apply {
            backButton.setOnClickListener {
                findNavController().popBackStack()
            }
            titleTxt.text = args.product.title
            priceTxt.text = "${args.product.variants?.get(0)?.price.toString()} LE"

            categoryTxt.text = args.product.tags
            typeTxt.text = args.product.productType
            productDesc.text = args.product.bodyHtml
            vendorTxt.text = args.product.templateSuffix
            dateTxt.text = args.product.published_at?.slice(IntRange(0, 9))
            Glide.with(requireContext()).load(args.product.image?.src).centerCrop()
                .placeholder(R.drawable.placeholder).into(binding.imageItem)
            binding.showButton.setOnClickListener{
                if (user != null) {
                    Log.i("TAG", " User is signed in")
                    Log.i("tag",args.product.variants?.get(0)?.title.toString()+"args title")
                    var contact = ContactInfo(args.product.variants?.get(0)?.sku.toString(),args.product.variants?.get(0)?.title.toString())
                    val action = DetailsSparePartFragmentDirections.actionDetailsSparePartFragmentToContactInfoFragment(contact,
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
//            favIcon.setOnClickListener {
//                if (!clicked) {
//                    favoriteButton.setBackgroundResource(R.drawable.ic_heart_fill)
//                    clicked = true
//                } else {
//                    favoriteButton.setBackgroundResource(R.drawable.ic_heart_outline)
//                    clicked = false
//                }
//            }
        }
        return binding.root
    }

}