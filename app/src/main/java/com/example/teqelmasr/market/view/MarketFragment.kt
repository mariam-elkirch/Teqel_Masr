package com.example.teqelmasr.market.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentMarketBinding
import com.example.teqelmasr.displaySellerProducts.view.DisplaySellerProductsFragmentArgs
import com.example.teqelmasr.displaySellerProducts.view.DisplaySellerProductsFragmentDirections
import com.example.teqelmasr.displaySparePart.view.DisplaySparePartFragmentDirections
import com.example.teqelmasr.displaySparePart.view.OnProductClickListener
import com.example.teqelmasr.helper.Constants
import com.example.teqelmasr.market.viewModel.MarketViewModel
import com.example.teqelmasr.market.viewModel.MarketViewModelFactory
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client


class MarketFragment : Fragment(), OnProductClickListener {

    private val binding by lazy { FragmentMarketBinding.inflate(layoutInflater) }
    private val args by navArgs<MarketFragmentArgs>()
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
    private lateinit var productList: List<Product>

    override fun onResume() {
        super.onResume()
        Log.i("DisplaySparePartFragment", "onResume: ")
        getAllProducts()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setUpUI()

        getAllProducts()

        return binding.root
    }

    private fun setUpSearch() {
        binding.apply {

            searchSpareParts.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    adapter.filter.filter(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    adapter.filter.filter(newText)
                    return true
                }
            })

            searchSpareParts.setOnCloseListener(SearchView.OnCloseListener() {
                Log.i("Tag", "onCreateView: setOnCloseListener")
                binding.apply {
                    noResultsImage.visibility = View.GONE
                    noResultText.visibility = View.GONE
                }
                false
            })
        }
    }

    private fun setUpUI(){
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)

        binding.recyclerViewAllProducts.adapter = adapter
        binding.recyclerViewAllProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.refreshLayout.setOnRefreshListener {
            getAllProducts()
        }
        setUpSearch()
        binding.refreshLayout.setOnRefreshListener {
            getAllProducts()
            onFullList()
        }
        binding.filterButton.setOnClickListener {
            val action: NavDirections =
                MarketFragmentDirections.actionMarketFragmentToFiltrationSheetFragment(Constants.MARKET_FRAGMENT)
            findNavController().navigate(action)
        }
    }


    private fun fillData(products: List<Product>?) = binding.apply {


        if (args.filterObj != null) {
            productList =
                products?.filter {
                    it.variants?.get(0)?.price?.toInt() in args.filterObj!!.priceRange
                } as ArrayList<Product>
            if (!(args.filterObj!!.categories.isNullOrEmpty())) {
                productList =
                    productList.filter { it.tags in args.filterObj!!.categories } as ArrayList<Product>
            }
            if (!(args.filterObj!!.types.isNullOrEmpty())) {
                productList =
                    productList.filter { it.productType in args.filterObj!!.types } as ArrayList<Product>
                Log.i("TAG", "FILTER type condition: ${productList.size}")

            }

            Log.i("TAG", "FILTER: ${productList.size}")

            adapter.setData(productList)
            spareShimmer.stopShimmer()
            spareShimmer.visibility = View.GONE

        } else {
            productList = products ?: ArrayList<Product>()
            adapter.setData(products!!)
            spareShimmer.stopShimmer()
            spareShimmer.visibility = View.GONE
            Log.i("TAG", "IN ELSE: ${productList.size}")
        }

    }

    private fun getAllProducts() {
        viewModel.getAllProducts()
        viewModel.allProductsLiveData.observe(viewLifecycleOwner) {
            binding.refreshLayout.isRefreshing = false
            fillData(it)
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
        binding.apply {
            noResultsImage.visibility = View.VISIBLE
            noResultText.text = "No Results for your search \"$searchKey\""
            noResultText.visibility = View.VISIBLE
        }
    }

    override fun onFullList() {
        binding.apply {
            noResultsImage.visibility = View.GONE
            noResultText.visibility = View.GONE
        }
    }

}