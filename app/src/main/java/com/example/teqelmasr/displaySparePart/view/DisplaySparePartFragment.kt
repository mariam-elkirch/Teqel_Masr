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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teqelmasr.databinding.FragmentDisplaySparePartBinding
import com.example.teqelmasr.displaySparePart.viewModel.DisplaySparPartsViewModelFactory
import com.example.teqelmasr.displaySparePart.viewModel.DisplaySparePartsViewModel
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client
import java.util.*


class DisplaySparePartFragment : Fragment(), OnProductClickListener {

    private val binding by lazy { FragmentDisplaySparePartBinding.inflate(layoutInflater) }

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.apply {
            recyclerViewSpareParts.adapter = sparePartsAdapter
            recyclerViewSpareParts.hasFixedSize()
            recyclerViewSpareParts.layoutManager = LinearLayoutManager(requireContext())

            //sparePartSearch.setOnCloseListener { true }
        }
        fetchSpareParts()
        return binding.root
    }

    private fun fetchSpareParts() {
        viewModel.sparePartsLiveData.observe(viewLifecycleOwner) { productItem ->
            binding.searchSpareParts.visibility = View.VISIBLE
            sparePartsAdapter.setSparePartsList(productItem.products!!)
            binding.spareShimmer.stopShimmer()
            binding.spareShimmer.visibility = View.GONE
            binding.searchSpareParts.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    TODO("Not yet implemented")
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val searchKeyWord = newText!!.toLowerCase(Locale.getDefault())
                    if (searchKeyWord.isNotEmpty()) {
                        Log.i("TAG", "query word is ${newText}")
                        productItem.products.forEach { product ->
                            if (product.title?.toLowerCase()?.contains(searchKeyWord)!!) {
                                Log.i("TAG", "search result is ${product.title}")
                            }

                        }

                    }
                    return false
                }


            })
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


}