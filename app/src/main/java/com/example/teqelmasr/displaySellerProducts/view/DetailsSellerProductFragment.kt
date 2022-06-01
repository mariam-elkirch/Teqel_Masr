package com.example.teqelmasr.displaySellerProducts.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentDetailsSellerProductBinding
import com.example.teqelmasr.databinding.FragmentDisplaySellerProductsBinding


class DetailsSellerProductFragment : Fragment() {
    private val binding by lazy { FragmentDetailsSellerProductBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

}