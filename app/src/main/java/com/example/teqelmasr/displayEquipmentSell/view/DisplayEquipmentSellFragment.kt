package com.example.teqelmasr.displayEquipmentSell.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import androidx.recyclerview.widget.LinearLayoutManager

import com.example.teqelmasr.R

import com.example.teqelmasr.databinding.FragmentDisplayEquipmentSellBinding
import com.example.teqelmasr.displayEquipmentSell.viewModel.DisplayEquipmentSellViewModel
import com.example.teqelmasr.displayEquipmentSell.viewModel.DisplayEquipmentSellViewModelFactory

import com.example.teqelmasr.displaySparePart.view.OnProductClickListener
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DisplayEquipmentSellFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DisplayEquipmentSellFragment : Fragment() , OnProductClickListener {
    var equipmentList = ArrayList<Product>()

    private val binding by lazy { FragmentDisplayEquipmentSellBinding.inflate(layoutInflater)  }
    private val args by navArgs<DisplayEquipmentSellFragmentArgs>()
    private val equipmentSellAdapter by lazy {
        DisplaySellEquipmentAdapter(
            requireContext(),
            this)
    }

    private val viewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            factory = DisplayEquipmentSellViewModelFactory(
                Repository.getInstance(
                    Client.getInstance(),
                    requireContext()
                )
            )
        )[DisplayEquipmentSellViewModel::class.java]
    }
    /*  private val refreshListener = SwipeRefreshLayout.OnRefreshListener {

          fetchEquipmentSell()

      }*/

    override fun onResume() {
        super.onResume()
        Log.i("tag","on Resume")
        fetchEquipmentSell()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        binding.apply {
            recyclerViewSellEquipment.adapter = equipmentSellAdapter
            recyclerViewSellEquipment.hasFixedSize()

            recyclerViewSellEquipment.layoutManager = LinearLayoutManager(requireContext())
            searchEquipmentSell.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    equipmentSellAdapter.filter.filter(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    equipmentSellAdapter.filter.filter(newText)
                    return true
                }

            })
            searchEquipmentSell.setOnCloseListener(SearchView.OnCloseListener() {
                binding.apply {
                    noResultsImage.visibility = View.GONE
                    noResultText.visibility = View.GONE
                }
                false
            })

        }
        binding.filterButton.setOnClickListener { findNavController().navigate(R.id.action_displayEquipmentSellFragment_to_equimentSellBottonSheetFrgment) }

        binding.swipeRefreshLayout.setOnRefreshListener{

            Log.i("tag","on Refresh")
            viewModel.fetchSellEquipments()
        }
        fetchEquipmentSell()
        Log.i("tag",args.filterValue?.types.toString()+"ttttttttt")
        Log.i("ARGS start", args.filterValue?.priceStart.toString())
        Log.i("ARGS end", args.filterValue?.priceEnd.toString())

        return binding.root
    }
    //

    private fun fetchEquipmentSell() {

        viewModel.fetchSellEquipments()
        viewModel.sellEquipmentLiveData.observe(viewLifecycleOwner) {productsList->
            // equipmentSellAdapter.setEquipmentSellList(it.products!!)
            binding.shimmersell.stopShimmer()
            Log.i("tag","fetch equipment")
            // binding.swipeRefreshLayout.isRefreshing = false
            //   equipmentList.addAll(it.products)
            fillData(productsList)
            binding.shimmersell.visibility = View.GONE
            binding.swipeRefreshLayout.isRefreshing = false

        }
    }
    private fun fillData(productsList: List<Product>) {
        if (productsList.isNullOrEmpty()) {
            binding.shimmersell.stopShimmer()
            binding.shimmersell.visibility = View.GONE
        }
        if (args.filterValue != null && !(args.filterValue!!.types.isNullOrEmpty()) &&
           args.filterValue!!.priceEnd == null) {
            filterDataType(productsList)
        }
        else if  (args.filterValue != null && args.filterValue!!.types.isNullOrEmpty() &&
            (args.filterValue!!.priceStart != null && args.filterValue!!.priceEnd != null)
        ) {
            filterPrice(productsList)


        }
       else if (args.filterValue != null && !(args.filterValue!!.types.isNullOrEmpty()) &&
            (args.filterValue!!.priceStart != null && args.filterValue!!.priceEnd != null)
        ) {

            filterByPriceAndType(productsList)

        }
        else {
            Log.i("TAG", "fetchSpareParts: ${productsList.size}")
            //  sparePartsAdapter.setData(productItem)
            equipmentSellAdapter.setEquipmentSellList(productsList)
            binding.apply {


                shimmersell.stopShimmer()
                shimmersell.visibility = View.GONE
            }
        }

    }

    private fun filterDataType(productItem: List<Product>) {

            equipmentList =
                productItem.filter { it.productType!!.toLowerCase() in args.filterValue!!.types!! } as ArrayList<Product>

            equipmentSellAdapter.setEquipmentSellList(equipmentList)
            binding.apply {


                shimmersell.stopShimmer()
                shimmersell.visibility = View.GONE
            }

    }
    private fun filterPrice(productsList: List<Product>) {

            equipmentList =
                productsList.filter {
                    it.variants!![0].price!! >= args.filterValue!!.priceStart!!
                            && it.variants!![0].price!! <= args.filterValue!!.priceEnd!!
                } as ArrayList<Product>

            equipmentSellAdapter.setEquipmentSellList(equipmentList)
            binding.apply {


                shimmersell.stopShimmer()
                shimmersell.visibility = View.GONE
            }

    }
    private fun filterByPriceAndType(productsList: List<Product>) {

            equipmentList =
                productsList.filter {
                    it.productType!!.toLowerCase() in args.filterValue!!.types!!
                            && (it.variants!![0].price!! >= args.filterValue!!.priceStart!!
                            && it.variants!![0].price!! <= args.filterValue!!.priceEnd!!)
                } as ArrayList<Product>

            equipmentSellAdapter.setEquipmentSellList(equipmentList)
            binding.apply {


                shimmersell.stopShimmer()
                shimmersell.visibility = View.GONE
            }

    }
    override fun onProductClick(product: Product) {
        val action = DisplayEquipmentSellFragmentDirections.actionDisplayEquipmentSellFragmentToDetailsEquipmentSellFragment(product)
        binding.root.findNavController().navigate(action)
        Log.i("TAG", "${product.title} Inside onProductClick")

    }

    override fun onEmptyList(searchKey: String) {

    }

    override fun onFullList() {

    }
}