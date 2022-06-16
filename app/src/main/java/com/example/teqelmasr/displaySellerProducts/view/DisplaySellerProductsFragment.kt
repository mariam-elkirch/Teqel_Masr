package com.example.teqelmasr.displaySellerProducts.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentDisplaySellerProductsBinding
import com.example.teqelmasr.displaySellerProducts.viewModel.MyProductsViewModel
import com.example.teqelmasr.displaySellerProducts.viewModel.MyProductsViewModelFactory
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.ProductItem
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client
import com.google.firebase.auth.FirebaseAuth


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

    private lateinit var viewModel: MyProductsViewModel
    private lateinit var adapter: MyProductsAdapter

    private val args by navArgs<DisplaySellerProductsFragmentArgs>()

    private lateinit var productList: ArrayList<Product>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        adapter = MyProductsAdapter(requireContext(), this)
        viewModel = ViewModelProvider(requireActivity(), factory)[MyProductsViewModel::class.java]


        setUpUI()

        observeMyProducts()

        handleRefresher()

        return binding.root
    }


    private fun observeMyProducts() {
        viewModel.getMyProducts()
        viewModel.myProducts?.observe(viewLifecycleOwner) {
            fillData(it)
            Log.i(TAG, "SellerProduct: ${it?.size}")
            Log.i(TAG, "SellerProduct: ${FirebaseAuth.getInstance().currentUser?.uid}")
            binding.refresher.isRefreshing = false

        }
    }

    private fun fillData(products: ArrayList<Product>?) = binding.apply {

        if (products.isNullOrEmpty()) {
            noProducts.visibility = View.VISIBLE
        }else{
            noProducts.visibility = View.INVISIBLE
        }


        if (args.filterObj != null) {
            productList =
                products?.filter {
                    it.variants?.get(0)?.price?.toInt() in args.filterObj!!.priceRange
                } as ArrayList<Product>
            if(!(args.filterObj!!.categories.isNullOrEmpty())){
                productList = productList.filter { it.tags in args.filterObj!!.categories } as ArrayList<Product>
            }
            if(!(args.filterObj!!.types.isNullOrEmpty())){
                productList = productList.filter { it.productType in args.filterObj!!.types } as ArrayList<Product>
                Log.i(TAG, "FILTER type condition: ${productList.size}")

            }

            Log.i(TAG, "FILTER: ${productList.size}")

            adapter.setData(productList)
            shimmer.stopShimmer()
            shimmer.visibility = View.GONE

        }
        else {
            productList = products ?: ArrayList<Product>()
            adapter.setData(products)
            shimmer.stopShimmer()
            shimmer.visibility = View.GONE
            Log.i(TAG, "IN ELSE: ${productList.size}")
        }

    }

    private fun setUpUI() = binding.apply {
        refresher.isRefreshing = false
        noProducts.visibility = View.GONE
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

    private fun handleRefresher() = binding.refresher.apply {
        setColorSchemeColors(resources.getColor(R.color.orange,null))
        setOnRefreshListener {
            isRefreshing = true
            observeMyProducts()
        }
    }

    override fun onDeleteClick(product: Product,listSize: Int) {
        Log.i(TAG, "onDeleteClick: $listSize")
        viewModel.deleteProduct(product)
        if(listSize == 1){
            binding.noProducts.visibility = View.VISIBLE
        }
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

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume: ${args.filterObj?.priceRange}")
        Log.i(TAG, "onResume: ${args.filterObj?.categories}")
        Log.i(TAG, "onResume: ${args.filterObj?.types}")

        viewModel.getMyProducts()
        observeMyProducts()
    }

    override fun onPause() {
        super.onPause()
        binding.noProducts.visibility = View.GONE
    }


}