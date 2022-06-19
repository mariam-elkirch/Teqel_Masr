package com.example.teqelmasr.displaySellerProducts.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentDetailsSellerProductBinding
import com.example.teqelmasr.databinding.FragmentDisplaySellerProductsBinding


class DetailsSellerProductFragment : Fragment() {

    private val binding by lazy { FragmentDetailsSellerProductBinding.inflate(layoutInflater) }
    private val args by navArgs<DetailsSellerProductFragmentArgs>()
    private val TAG = "DetailsSellerProductFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        setUpUI()

        return binding.root
    }

    private fun setUpUI() {
        binding.apply {
            priceTxt.text = "${args.currentProduct.variants?.get(0)?.price.toString()} ${getString(R.string.le)}"
            dateTxt.text = args.currentProduct.published_at?.slice(IntRange(0,9))
            titleTxt.text = args.currentProduct.title
            categoryTxt.text = args.currentProduct.tags
            typeTxt.text = args.currentProduct.productType
            Log.i(TAG, "${args.currentProduct.productType}")
            vendorTxt.text = args.currentProduct.templateSuffix
            productDesc.text = args.currentProduct.bodyHtml

        }
        Glide.with(requireContext()).load(args.currentProduct.image?.src).centerCrop().placeholder(R.drawable.placeholder)
            .into(binding.imageItem)
    }





}