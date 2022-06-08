package com.example.teqelmasr.displaySparePart.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentDisplaySparePartBinding
import com.example.teqelmasr.displaySparePart.viewModel.DisplaySparPartsViewModelFactory
import com.example.teqelmasr.displaySparePart.viewModel.DisplaySparePartsViewModel
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.ProductItem
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client


class DisplaySparePartFragment : Fragment(), OnProductClickListener {

    private val binding by lazy { FragmentDisplaySparePartBinding.inflate(layoutInflater) }
    private val args by navArgs<DisplaySparePartFragmentArgs>()
    private lateinit var sparePartsList: ArrayList<Product>
    private val sparePartsAdapter by lazy {
        DisplaySparePartsRecyclerAdapter(
            requireContext(),
            this
        )
    }

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

   /* override fun onResume() {
        super.onResume()
        fetchSpareParts()
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Log.i("ARGS", args.filterValues?.priceStart.toString())
        // Log.i("ARGS", args.filterValues?.types?.elementAt(0).toString())
        setUpUI()

        fetchSpareParts()

        return binding.root
    }

    private fun setUpUI() {
        binding.apply {
            recyclerViewSpareParts.adapter = sparePartsAdapter
            recyclerViewSpareParts.hasFixedSize()
            recyclerViewSpareParts.layoutManager = LinearLayoutManager(requireContext())
            filterButton.setOnClickListener { findNavController()
                .navigate(R.id.action_displaySparePartFragment_to_sparePartsFilterBottomSheetFragment) }
            setUpSearch()

        }
    }

    private fun setUpSearch() {
        binding.apply {

            searchSpareParts.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    sparePartsAdapter.filter.filter(query)
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    sparePartsAdapter.filter.filter(newText)
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

    private fun fetchSpareParts() {
        viewModel.fetchSpareParts()
        viewModel.sparePartsLiveData.observe(viewLifecycleOwner) { productItem ->
            fillSparePartsData(productItem)
        }
    }

    private fun fillSparePartsData(productItem: ProductItem) {
        if (productItem.products.isNullOrEmpty()) {
            binding.spareShimmer.stopShimmer()
            binding.spareShimmer.visibility = View.GONE
        }
        if (args.filterValues != null && !(args.filterValues!!.types.isNullOrEmpty())) {
            filterData(productItem)
        } else {
            Log.i("TAG", "fetchSpareParts: ${productItem.products!!.size}")
            sparePartsAdapter.setData(productItem.products)
            binding.apply {
                searchSpareParts.visibility = View.VISIBLE
                filterButton.visibility = View.VISIBLE
                spareShimmer.stopShimmer()
                spareShimmer.visibility = View.GONE

            }
        }
    }

    private fun filterData(productItem: ProductItem) {
        sparePartsList =
            productItem.products?.filter { it.productType in args.filterValues!!.types!! } as ArrayList<Product>
        Log.i("TAG", "IN FILTER: ${sparePartsList[0].variants?.get(0)?.price}")
        sparePartsAdapter.setData(sparePartsList)
        binding.apply {
            searchSpareParts.visibility = View.VISIBLE
            filterButton.visibility = View.VISIBLE
            spareShimmer.stopShimmer()
            spareShimmer.visibility = View.GONE
        }
    }

    override fun onProductClick(product: Product) {
        val action =
            DisplaySparePartFragmentDirections.actionDisplaySparePartFragmentToDetailsSparePartFragment2(
                product
            )
        binding.root.findNavController().navigate(action)
        Log.i("TAG", "${product.title} Inside onProductClick")
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