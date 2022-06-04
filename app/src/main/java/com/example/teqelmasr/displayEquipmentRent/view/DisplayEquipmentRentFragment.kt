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
    private val binding by lazy { FragmentDisplayEquipmentRentBinding.inflate(layoutInflater) }

    private val equipmentRentAdapter by lazy {
        DisplayRentEquipmentRecyclerAdapter(
            requireContext(),
            this)}

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu,menu)
        val menuItem = menu!!.findItem(R.id.action_search)
        if (menuItem!=null){
            val searchView = menuItem.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if(newText!!.isNotEmpty()){
                        searchResultList.clear()
                       equipmentRentAdapter.setEquipmentRentList(searchResultList)
                        val search = newText.lowercase(Locale.getDefault())
                        allProductList.forEach {
                            if (it.title?.lowercase(Locale.getDefault())!!.contains(search))
                            {
                                searchResultList.add(it)
                                equipmentRentAdapter.setEquipmentRentList(searchResultList)

                            }
                        }

                    }
                    else{
                        searchResultList.clear()
                        searchResultList.addAll(allProductList)
                        equipmentRentAdapter.setEquipmentRentList(searchResultList)

                    }
                    return true
                }

            })
        }

    }
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

        }

        fetchEquipmentRent()
        return binding.root
    }

    private fun fetchEquipmentRent() {
        viewModel.rentEquipmentLiveData.observe(viewLifecycleOwner) {
            equipmentRentAdapter.setEquipmentRentList(it.products!!)
            searchResultList.addAll(it.products)
            allProductList.addAll(it.products)
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
        TODO("Not yet implemented")
    }

    override fun onFullList() {

    }


}