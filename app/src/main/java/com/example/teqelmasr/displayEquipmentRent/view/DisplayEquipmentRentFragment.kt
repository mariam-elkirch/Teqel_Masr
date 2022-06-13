package com.example.teqelmasr.displayEquipmentRent.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentDisplayEquipmentRentBinding
import com.example.teqelmasr.displayEquipmentRent.viewModel.DisplayRentEquipmentViewModel
import com.example.teqelmasr.displayEquipmentRent.viewModel.DisplayRentEquipmentViewModelFactory
import com.example.teqelmasr.displaySparePart.view.OnProductClickListener
import com.example.teqelmasr.displaySparePart.viewModel.DisplaySparePartsViewModel
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client
import java.util.*
import kotlin.collections.ArrayList


class DisplayEquipmentRentFragment : Fragment() , OnProductClickListener {
    private val allProductList = ArrayList<Product>()
    private var filterResultList = ArrayList<Product>()
    private val args by navArgs<DisplayEquipmentRentFragmentArgs>()

    private val binding by lazy { FragmentDisplayEquipmentRentBinding.inflate(layoutInflater) }

    private val equipmentRentAdapter by lazy {
        DisplayRentEquipmentRecyclerAdapter(
            requireContext(),
            this)}

    private val viewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            factory = DisplayRentEquipmentViewModelFactory(
                Repository.getInstance(
                    Client.getInstance(),
                    requireContext()
                )
            )
        )[DisplayRentEquipmentViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        binding.apply {
            recyclerViewRentEquipment.adapter = equipmentRentAdapter
            recyclerViewRentEquipment.hasFixedSize()
            recyclerViewRentEquipment.layoutManager = LinearLayoutManager(requireContext())
            searchRentEquipment.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener,
             SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    equipmentRentAdapter.filter.filter(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    equipmentRentAdapter.filter.filter(newText)
                    return true
                }
            })
            searchRentEquipment.setOnCloseListener(android.widget.SearchView.OnCloseListener() {
                binding.apply {
                    noResultsImage.visibility = View.GONE
                    noResultText.visibility = View.GONE
                }
                false
            })
        }
        binding.filterButtonRentEquipment.setOnClickListener { findNavController().navigate(R.id.action_displayEquipmentRentFragment_to_equipmentRentFilterBottomSheetFragment) }
        binding.swipeRefreshLayoutRent.setOnRefreshListener {
            viewModel.fetchRentEquipments()
        }
        fetchEquipmentRent()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
            filterData()
        fetchEquipmentRent()
    }

    private fun fetchEquipmentRent() {
        viewModel.rentEquipmentLiveData.observe(viewLifecycleOwner) {
            equipmentRentAdapter.setEquipmentRentList(it)
           // searchResultList.addAll(it.products)
            allProductList.addAll(it)
            binding.shimmerrent.stopShimmer()
            binding.shimmerrent.visibility = View.GONE
            binding.swipeRefreshLayoutRent.isRefreshing = false

        }
    }

    override fun onProductClick(product: Product) {
        val action = DisplayEquipmentRentFragmentDirections.actionDisplayEquipmentRentFragmentToDetailsEquipmentRentFragment(product)
        binding.root.findNavController().navigate(action)
    }

    override fun onEmptyList(searchKey: String) {
        binding.apply {
            Log.i("TAG", "onEmptyList:testtttt ")
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

    private fun filterData(){
    if(args.filterObj!=null ) {

        // filter with type and price
        if(!(args.filterObj!!.types.isNullOrEmpty()) && args.filterObj!!.priceStart != null && args.filterObj!!.priceEnd != null ) {
            filterResultList = allProductList.filter {
                it.productType!!.toString()
                    .lowercase(Locale.getDefault()) in args.filterObj!!.types!!
                        && it.variants?.get(0)!!.price!! >= args.filterObj!!.priceStart!!
                        && it.variants?.get(0)!!.price!! <= args.filterObj!!.priceEnd!!
            } as ArrayList<Product>
            updateData(filterResultList)
            Log.i("TAG", "filter with type and price")

        }
        // filter with price only

        else if(args.filterObj!!.types.isNullOrEmpty() && args.filterObj!!.priceStart != null && args.filterObj!!.priceEnd != null) {
            filterResultList = allProductList.filter {
                it.variants?.get(0)!!.price!! >= args.filterObj!!.priceStart!!
                        && it.variants?.get(0)!!.price!! <= args.filterObj!!.priceEnd!!
            } as ArrayList<Product>
            updateData(filterResultList)
            Log.i("TAG", "filter with price only")

        }
        // filter with type only
        else if(!(args.filterObj!!.types.isNullOrEmpty()) &&
            args.filterObj!!.priceEnd == null){
            filterResultList = allProductList.filter {
                it.productType!!.toString()
                    .lowercase(Locale.getDefault()) in args.filterObj!!.types!!
            } as ArrayList<Product>
            updateData(filterResultList)
            Log.i("TAG", "filter with type only")

        }


        else if( args.filterObj!!.types.isNullOrEmpty() && args.filterObj!!.priceEnd ==null){
                updateData(allProductList)
            Log.i("TAG", "apply without input ")

        }
    }

}
    private fun updateData( data :ArrayList<Product>){
        equipmentRentAdapter.setEquipmentRentList(data)
    }

}