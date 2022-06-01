package com.example.teqelmasr.displaySparePart.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teqelmasr.databinding.FragmentDisplaySparePartBinding
import com.example.teqelmasr.displaySparePart.viewModel.DisplaySparPartsViewModelFactory
import com.example.teqelmasr.displaySparePart.viewModel.DisplaySparePartsViewModel
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client


class DisplaySparePartFragment : Fragment(), OnProductClickListener {

    private val binding by lazy { FragmentDisplaySparePartBinding.inflate(layoutInflater) }

    private val sparePartsAdapter by lazy { DisplaySparePartsRecyclerAdapter(requireContext(), this) }

    private val viewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            factory = DisplaySparPartsViewModelFactory(
                Repository.getInstance(
                    Client.getInstance(),
                    requireContext()
                )
            )
        )[DisplaySparePartsViewModel::class.java]
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.apply {
            recyclerViewSpareParts.adapter = sparePartsAdapter
            recyclerViewSpareParts.hasFixedSize()
            recyclerViewSpareParts.layoutManager =LinearLayoutManager(requireContext())
        }
        fetchSpareParts()
        return binding.root
    }

    private fun fetchSpareParts() {
        viewModel.sparePartsLiveData.observe(viewLifecycleOwner) {
            sparePartsAdapter.setSparePartsList(it.products!!)
            binding.spareShimmer.stopShimmer()
            binding.spareShimmer.visibility = View.GONE
        }
    }

    override fun onProductClick(product: Product) {
       Log.i("TAG","${product.title} Inside onProductClick")
    }


}