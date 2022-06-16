package com.example.teqelmasr.market.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentMarketBinding
import com.example.teqelmasr.displaySparePart.view.DisplaySparePartFragmentDirections
import com.example.teqelmasr.displaySparePart.view.OnProductClickListener
import com.example.teqelmasr.market.viewModel.MarketViewModel
import com.example.teqelmasr.market.viewModel.MarketViewModelFactory
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client


class MarketFragment : Fragment(), OnProductClickListener {

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
    private val adapter by lazy {
        MarketRecyclerViewAdapter(
            requireContext(),
            this
        )
    }

    override fun onResume() {
        super.onResume()
        Log.i("DisplaySparePartFragment", "onResume: ")
        getAllProducts()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)

        binding.recyclerViewAllProducts.adapter = adapter
        binding.recyclerViewAllProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.refreshLayout.setOnRefreshListener {
            getAllProducts()
        }

        getAllProducts()

        return binding.root
    }

    private fun getAllProducts() {
        viewModel.getAllProducts()
        viewModel.allProductsLiveData.observe(viewLifecycleOwner) {
            binding.refreshLayout.isRefreshing = false
            adapter.setData(it)
            binding.apply {
                searchSpareParts.visibility = View.VISIBLE
                filterButton.visibility = View.VISIBLE
                spareShimmer.stopShimmer()
                spareShimmer.visibility = View.GONE
            }
        }
    }

    override fun onProductClick(product: Product) {
        val action =
            MarketFragmentDirections.actionMarketFragmentToDetailsSparePartFragment2(
                product
            )
        binding.root.findNavController().navigate(action)
    }

    override fun onEmptyList(searchKey: String) {

    }

    override fun onFullList() {

    }

}