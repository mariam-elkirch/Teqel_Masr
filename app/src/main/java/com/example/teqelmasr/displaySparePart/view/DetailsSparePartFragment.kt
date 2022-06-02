package com.example.teqelmasr.displaySparePart.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentDetailsSparePartBinding
import com.example.teqelmasr.databinding.FragmentDisplaySparePartBinding
import com.example.teqelmasr.displayEquipmentRent.view.DetailsEquipmentRentFragmentArgs


class DetailsSparePartFragment : Fragment() {

    private val binding by lazy { FragmentDetailsSparePartBinding.inflate(layoutInflater) }
    private val args by navArgs<DetailsEquipmentRentFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding.apply {
            titleTxt.text = args.product.title
            priceTxt.text = args.product.variants?.get(0)?.price.toString()
            categoryTxt.text = args.product.tags
            typeTxt.text = args.product.productType
            productDesc.text = args.product.bodyHtml
            vendorTxt.text = args.product.vendor
            dateTxt.text = args.product.published_at
            Glide.with(requireContext()).load(args.product.image?.src).centerCrop()
                .placeholder(R.drawable.placeholder).into(binding.imageItem)

        }

        return binding.root
    }

}