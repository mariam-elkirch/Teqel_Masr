package com.example.teqelmasr.displaySparePart.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentDetailsSparePartBinding
import com.example.teqelmasr.displayEquipmentRent.view.DetailsEquipmentRentFragmentArgs


class DetailsSparePartFragment : Fragment() {

    private val binding by lazy { FragmentDetailsSparePartBinding.inflate(layoutInflater) }
    private val args by navArgs<DetailsSparePartFragmentArgs>()
    private var clicked = false

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
            dateTxt.text = args.product.published_at?.slice(IntRange(0, 9))
            Glide.with(requireContext()).load(args.product.image?.src).centerCrop()
                .placeholder(R.drawable.placeholder).into(binding.imageItem)
            favoriteButton.setOnClickListener {
                if (!clicked) {
                    favoriteButton.setBackgroundResource(R.drawable.ic_heart_fill)
                    clicked = true
                } else {
                    favoriteButton.setBackgroundResource(R.drawable.ic_heart_outline)
                    clicked = false
                }
            }


        }

        return binding.root
    }

}