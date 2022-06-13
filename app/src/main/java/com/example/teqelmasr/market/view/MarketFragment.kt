package com.example.teqelmasr.market.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.ActivityRegisterationBinding
import com.example.teqelmasr.databinding.FragmentMarketBinding
import com.example.teqelmasr.displaySparePart.viewModel.DisplaySparPartsViewModelFactory
import com.example.teqelmasr.displaySparePart.viewModel.DisplaySparePartsViewModel
import com.example.teqelmasr.market.viewModel.MarketViewModel
import com.example.teqelmasr.market.viewModel.MarketViewModelFactory
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client


class MarketFragment : Fragment() {

    private val binding by lazy { FragmentMarketBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            factory = MarketViewModelFactory(
                Repository.getInstance(
                    Client.getInstance(),
                    requireContext()
                )
            )
        )[MarketViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        getAllProducts()

        return binding.root
    }

    private fun getAllProducts() {
        viewModel.getAllProducts()
        viewModel.allProductsLiveData.observe(viewLifecycleOwner){

        }
    }

}