package com.example.teqelmasr.displaySellerProducts.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentDisplaySellerProductsBinding
import com.example.teqelmasr.displayEquipmentRent.view.DetailsEquipmentRentFragmentArgs

import com.example.teqelmasr.displaySellerProducts.viewModel.MyProductsViewModel
import com.example.teqelmasr.displaySellerProducts.viewModel.MyProductsViewModelFactory
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.ProductItem
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client
import java.util.ArrayList

class DisplaySellerProductsFragment : Fragment(), OnBtnListener {
    private val TAG = "DisplaySellerProducts"

    private val binding by lazy { FragmentDisplaySellerProductsBinding.inflate(layoutInflater) }
    private val factory by lazy {
        MyProductsViewModelFactory(
            Repository.getInstance(
                Client.getInstance(),
                requireContext()
            )
        )
    }
    private val viewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            factory
        )[MyProductsViewModel::class.java]
    }
    private lateinit var adapter: MyProductsAdapter
    private val args by navArgs<DisplaySellerProductsFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        adapter = MyProductsAdapter(requireContext(), this)
        setUpUI()

        observeMyProducts()

        testFilteration()

        return binding.root
    }

    private fun testFilteration() {
        Log.i(TAG, "price: ${args.filterObj?.priceRange}")
        Log.i(TAG, "categories: ${args.filterObj?.categories}")
        Log.i(TAG, "types: ${args.filterObj?.types}")

    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume: ")
        observeMyProducts()
    }

    private fun observeMyProducts() {

        viewModel.myProducts.observe(viewLifecycleOwner) {
            fillData(it)
        }
    }

    private fun fillData(productItem: ProductItem) = binding.apply {
        filterProducts(productItem.products)
        (myProductsRecycler.adapter as MyProductsAdapter).setData(productItem.products!!)
        shimmer.stopShimmer()
        shimmer.visibility = View.GONE
    }

    private fun filterProducts(products: ArrayList<Product>?) {
        if (args.filterObj != null) {
            products?.filter { product ->
                //product.variants?.get(0)?.price?.toInt()!! in args.filterObj!!.priceRange
                product.templateSuffix.toString() == "turbocharger"
            }
        }
    }


    private fun setUpUI() = binding.apply {
        myProductsRecycler.layoutManager = LinearLayoutManager(requireContext())
        myProductsRecycler.adapter = adapter
        filterIcon.setOnClickListener {
            val action: NavDirections =
                DisplaySellerProductsFragmentDirections.actionDisplaySellerProductsFragmentToFiltrationSheetFragment()
            findNavController().navigate(action)
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                adapter.filter.filter(p0)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                adapter.filter.filter(p0)
                return true
            }

        })

    }

    override fun onDeleteClick(product: Product) {
        viewModel.deleteProduct(product)
    }

    override fun onDetailsClick(product: Product) {
        val action: NavDirections =
            DisplaySellerProductsFragmentDirections.actionDisplaySellerProductsFragmentToDetailsSellerProductFragment(
                product
            )
        binding.root.findNavController().navigate(action)
    }

    override fun onEditClick(product: Product) {
        val action: NavDirections =
            DisplaySellerProductsFragmentDirections.actionDisplaySellerProductsFragmentToEditSellerProductFragment(
                product
            )
        binding.root.findNavController().navigate(action)
    }


}