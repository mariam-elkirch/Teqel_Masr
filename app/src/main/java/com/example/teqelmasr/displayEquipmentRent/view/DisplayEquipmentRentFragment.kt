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
    val allProductList = ArrayList<Product>()
    val searchResultList = ArrayList<Product>()
    val filterResultList = ArrayList<Product>()
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
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
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
            binding.filterButtonRentEquipment.setOnClickListener { findNavController().navigate(R.id.action_displayEquipmentRentFragment_to_equipmentRentFilterBottomSheetFragment) }


        }



        fetchEquipmentRent()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if(args.filterObj!=null) {

            for(i in 0 until allProductList.size){
                for (cat in args.filterObj?.types!!){
                    if (allProductList[i].productType.toString().lowercase(Locale.getDefault()) == cat){
                        filterResultList.add(allProductList[i])
                    }

                }

               /* allProductList.filter {
                    args.filterObj!!.types?.filter {
                        if (.productType.toString().lowercase(Locale.getDefault()))

                    }
                }*/
            }
            equipmentRentAdapter.setEquipmentRentList(filterResultList)

        }
    }
    private fun fetchEquipmentRent() {
        viewModel.rentEquipmentLiveData.observe(viewLifecycleOwner) {
            equipmentRentAdapter.setEquipmentRentList(it)
           // searchResultList.addAll(it.products)
            allProductList.addAll(it)
            binding.shimmerrent.stopShimmer()
            binding.shimmerrent.visibility = View.GONE
        }
    }

    override fun onProductClick(product: Product) {
        val action = DisplayEquipmentRentFragmentDirections.actionDisplayEquipmentRentFragmentToDetailsEquipmentRentFragment(product)
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