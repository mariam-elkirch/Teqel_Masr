package com.example.teqelmasr.displayEquipmentSell.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentDisplayEquipmentRentBinding
import com.example.teqelmasr.databinding.FragmentDisplayEquipmentSellBinding
import com.example.teqelmasr.displayEquipmentRent.view.DisplayEquipmentRentFragmentDirections
import com.example.teqelmasr.displayEquipmentRent.view.DisplayRentEquipmentRecyclerAdapter
import com.example.teqelmasr.displayEquipmentRent.viewModel.DisplayRentEquipmentViewModel
import com.example.teqelmasr.displayEquipmentRent.viewModel.DisplayRentEquipmentViewModelFactory
import com.example.teqelmasr.displayEquipmentSell.viewModel.DisplayEquipmentSellViewModel
import com.example.teqelmasr.displayEquipmentSell.viewModel.DisplayEquipmentSellViewModelFactory
import com.example.teqelmasr.displaySparePart.view.DisplaySparePartFragmentArgs
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
     var equipmentListFilter = ArrayList<Product>()
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

        if (args.filterValue != null /*&& !(args.filterObj!!.categories.isNullOrEmpty())*/ /*&& !(args.filterObj!!.types.isNullOrEmpty())*/) {
            if(args.filterValue!=null) {
                for(i in 0 until equipmentList.size){
                    for (cat in args.filterValue?.types!!){
                        if (equipmentList[i].productType.toString().lowercase(Locale.getDefault()) == cat){
                            equipmentListFilter.add(equipmentList[i])
                        }

                    }

                }
                equipmentSellAdapter.setEquipmentSellList(equipmentListFilter)

            }
            else{
                equipmentSellAdapter.setEquipmentSellList(equipmentList)
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
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
            fetchEquipmentSell()
            Log.i("tag","on Refresh")
            viewModel.fetchSellEquipments()
        }
        viewModel.fetchSellEquipments()
         Log.i("tag",args.filterValue?.types.toString()+"ttttttttt")


        return binding.root
    }
  //

    private fun fetchEquipmentSell() {
      binding.swipeRefreshLayout.isRefreshing = true
        viewModel.sellEquipmentLiveData.observe(viewLifecycleOwner) {
            equipmentSellAdapter.setEquipmentSellList(it.products!!)
            binding.shimmersell.stopShimmer()
            Log.i("tag","fetch equipment")
           // binding.swipeRefreshLayout.isRefreshing = false
            equipmentList.addAll(it.products)
            binding.shimmersell.visibility = View.GONE
            binding.swipeRefreshLayout.isRefreshing = false
          //
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