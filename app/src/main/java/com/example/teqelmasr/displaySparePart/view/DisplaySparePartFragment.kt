package com.example.teqelmasr.displaySparePart.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentDisplaySparePartBinding


class DisplaySparePartFragment : Fragment() {
    val binding =  FragmentDisplaySparePartBinding.inflate(layoutInflater)
    private val sparePartsAdapter by lazy { DisplaySparePartsRecyclerAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.recyclerViewSpareParts.adapter = sparePartsAdapter

        return binding.root
    }


}