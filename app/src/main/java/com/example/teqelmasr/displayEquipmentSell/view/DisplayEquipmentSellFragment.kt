package com.example.teqelmasr.displayEquipmentSell.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentDisplayEquipmentRentBinding
import com.example.teqelmasr.databinding.FragmentDisplayEquipmentSellBinding
import com.example.teqelmasr.displayEquipmentRent.view.DisplayEquipmentRentFragmentDirections
import com.example.teqelmasr.displayEquipmentRent.view.DisplayRentEquipmentRecyclerAdapter
import com.example.teqelmasr.displayEquipmentRent.viewModel.DisplayRentEquipmentViewModel
import com.example.teqelmasr.displayEquipmentRent.viewModel.DisplayRentEquipmentViewModelFactory
import com.example.teqelmasr.displayEquipmentSell.viewModel.DisplayEquipmentSellViewModel
import com.example.teqelmasr.displayEquipmentSell.viewModel.DisplayEquipmentSellViewModelFactory
import com.example.teqelmasr.displaySparePart.view.OnProductClickListener
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client

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

    private val binding by lazy { FragmentDisplayEquipmentSellBinding.inflate(layoutInflater) }

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        binding.apply {
            recyclerViewSellEquipment.adapter = equipmentSellAdapter
            recyclerViewSellEquipment.hasFixedSize()
            recyclerViewSellEquipment.layoutManager = GridLayoutManager(requireContext(), 2)
        }

        fetchEquipmentSell()
        return binding.root
    }

    private fun fetchEquipmentSell() {
        viewModel.sellEquipmentLiveData.observe(viewLifecycleOwner) {
            equipmentSellAdapter.setEquipmentSellList(it.products!!)
        }
    }

    override fun onProductClick(product: Product) {
      //  val action = DisplayEquipmentRentFragmentDirections.actionDisplayEquipmentRentFragmentToDetailsEquipmentRentFragment(product)
      //  binding.root.findNavController().navigate(action)
        Log.i("TAG", "${product.title} Inside onProductClick")

    }
}