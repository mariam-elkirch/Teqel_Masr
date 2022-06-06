package com.example.teqelmasr.displaySellerProducts.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentDisplaySellerProductsBinding

import com.example.teqelmasr.displaySellerProducts.viewModel.MyProductsViewModel
import com.example.teqelmasr.displaySellerProducts.viewModel.MyProductsViewModelFactory
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.ProductItem
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client

class DisplaySellerProductsFragment : Fragment(), OnBtnListener {

    private val binding by lazy { FragmentDisplaySellerProductsBinding.inflate(layoutInflater) }
    private val factory by lazy { MyProductsViewModelFactory(Repository.getInstance(Client.getInstance(),requireContext())) }
    private val viewModel by lazy { ViewModelProvider(requireActivity(), factory)[MyProductsViewModel::class.java] }
    private lateinit var adapter: MyProductsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        adapter = MyProductsAdapter(requireContext(),this)
        setUpUI()

        observeMyProducts()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        observeMyProducts()
    }
    private fun observeMyProducts() {

        viewModel.myProducts.observe(viewLifecycleOwner){
            fillData(it)
        }
    }

    private fun fillData(productItem: ProductItem) = binding.apply {
        (myProductsRecycler.adapter as MyProductsAdapter).setData(productItem.products!!)
        shimmer.stopShimmer()
        shimmer.visibility = View.GONE
    }

    private fun setUpUI() = binding.apply {
        myProductsRecycler.layoutManager = LinearLayoutManager(requireContext())
        myProductsRecycler.adapter = adapter
        filterIcon.setOnClickListener {
            val action: NavDirections = DisplaySellerProductsFragmentDirections.actionDisplaySellerProductsFragmentToFiltrationSheetFragment()
            findNavController().navigate(action)
        }
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
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
        val action: NavDirections = DisplaySellerProductsFragmentDirections.actionDisplaySellerProductsFragmentToDetailsSellerProductFragment(product)
        binding.root.findNavController().navigate(action)
    }

    override fun onEditClick(product: Product) {
        val action: NavDirections = DisplaySellerProductsFragmentDirections.actionDisplaySellerProductsFragmentToEditSellerProductFragment(product)
        binding.root.findNavController().navigate(action)
    }


}