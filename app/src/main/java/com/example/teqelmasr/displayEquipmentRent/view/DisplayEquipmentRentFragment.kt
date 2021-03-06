package com.example.teqelmasr.displayEquipmentRent.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentDisplayEquipmentRentBinding
import com.example.teqelmasr.displayEquipmentRent.viewModel.DisplayRentEquipmentViewModel
import com.example.teqelmasr.displayEquipmentRent.viewModel.DisplayRentEquipmentViewModelFactory
import com.example.teqelmasr.displaySparePart.view.OnProductClickListener
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client
import java.util.*


class DisplayEquipmentRentFragment : Fragment() , OnProductClickListener {
    private var allProductList = ArrayList<Product>()
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
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        binding.apply {
            recyclerViewRentEquipment.adapter = equipmentRentAdapter
            recyclerViewRentEquipment.hasFixedSize()
            recyclerViewRentEquipment.layoutManager = LinearLayoutManager(requireContext())
            search()
            activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    view?.findNavController()?.navigate(R.id.homeFragment)
                }
            })
        }
        binding.filterButtonRentEquipment.setOnClickListener { findNavController().navigate(R.id.action_displayEquipmentRentFragment_to_equipmentRentFilterBottomSheetFragment) }
        binding.swipeRefreshLayoutRent.setOnRefreshListener { viewModel.fetchRentEquipments()
            onFullList()
        }
        fetchEquipmentRent()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
            //filterData()
      //  fetchEquipmentRent()
    }

    private fun fetchEquipmentRent() {
        viewModel.rentEquipmentLiveData.observe(viewLifecycleOwner) {
            Log.i("TAG", "fetchEquipmentRent: ${it.size}")
            //equipmentRentAdapter.setEquipmentRentList(it)
           //searchResultList.addAll(it.products)
            allProductList= it as ArrayList<Product>
            filterData()
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
            Log.i("TAG", "filterData: ${filterResultList.size}")
            equipmentRentAdapter.setEquipmentRentList(filterResultList)

            Log.i("TAG", "filter with type and price")

        }
        // filter with price only

        else if(args.filterObj!!.types.isNullOrEmpty() && args.filterObj!!.priceStart != null && args.filterObj!!.priceEnd != null) {
            filterResultList = allProductList.filter {
                it.variants?.get(0)!!.price!! >= args.filterObj!!.priceStart!!
                        && it.variants?.get(0)!!.price!! <= args.filterObj!!.priceEnd!!
            } as ArrayList<Product>
            equipmentRentAdapter.setEquipmentRentList(filterResultList)
            Log.i("TAG", "filterData:price ${filterResultList.size}")

            Log.i("TAG", "filter with price only")

        }
        // filter with type only
        else if(!(args.filterObj!!.types.isNullOrEmpty()) &&
            args.filterObj!!.priceEnd == null){
            filterResultList = allProductList.filter {
                it.productType!!.toString()
                    .lowercase(Locale.getDefault()) in args.filterObj!!.types!!
            } as ArrayList<Product>
            equipmentRentAdapter.setEquipmentRentList(filterResultList)

            Log.i("TAG", "filterData:type ${filterResultList.size}")

            Log.i("TAG", "filter with type only")

        }


        else if( args.filterObj!!.types.isNullOrEmpty() && args.filterObj!!.priceEnd ==null){
            filterResultList = allProductList
            equipmentRentAdapter.setEquipmentRentList(filterResultList)

            Log.i("TAG", "apply without input ")

        }
    }else{
        equipmentRentAdapter.setEquipmentRentList(allProductList)
    }

}
    private fun updateData( data :ArrayList<Product>){
        Log.i("TAG", "updateData: ${data.size}")
        equipmentRentAdapter.setEquipmentRentList(data)
    }
    private fun search(){
        binding.apply {
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
    }


}