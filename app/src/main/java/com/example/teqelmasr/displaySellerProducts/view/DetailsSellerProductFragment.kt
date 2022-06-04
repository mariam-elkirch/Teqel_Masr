package com.example.teqelmasr.displaySellerProducts.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentDetailsSellerProductBinding
import com.example.teqelmasr.databinding.FragmentDisplaySellerProductsBinding


class DetailsSellerProductFragment : Fragment() {

    private val binding by lazy { FragmentDetailsSellerProductBinding.inflate(layoutInflater) }
    private val args by navArgs<DetailsSellerProductFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setUpUI()

        return binding.root
    }

    private fun setUpUI() {
        binding.apply {
            priceTxt.text = args.currentProduct.variants!![0].price.toString()
            dateTxt.text = args.currentProduct.published_at?.slice(IntRange(0,9))
            titleTxt.text = args.currentProduct.title
            categoryTxt.text = args.currentProduct.tags
            typeTxt.text = args.currentProduct.productType
            vendorTxt.text = args.currentProduct.templateSuffix
            productDesc.text = args.currentProduct.bodyHtml
        }
        Glide.with(requireContext()).load(args.currentProduct.image?.src).centerCrop().placeholder(R.drawable.placeholder)
            .into(binding.imageItem)
    }

}