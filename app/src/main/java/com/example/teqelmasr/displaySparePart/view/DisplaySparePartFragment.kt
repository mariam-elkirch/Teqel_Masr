package com.example.teqelmasr.displaySparePart.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.teqelmasr.databinding.FragmentDisplaySparePartBinding
import com.example.teqelmasr.displaySparePart.viewModel.DisplaySparePartsViewModel
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.model.RepositoryInterface
import com.example.teqelmasr.network.Client


class DisplaySparePartFragment : Fragment() {

    private val binding by lazy { FragmentDisplaySparePartBinding.inflate(layoutInflater) }

    private val sparePartsAdapter by lazy { DisplaySparePartsRecyclerAdapter() }

    private val viewModel by lazy {DisplaySparePartsViewModel(Repository.getInstance(Client.getInstance(),requireContext()))}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding.recyclerViewSpareParts.adapter = sparePartsAdapter
        binding.recyclerViewSpareParts.hasFixedSize()
        binding.recyclerViewSpareParts.layoutManager = GridLayoutManager(requireContext(), 2)

        fetchSpareParts()

        return binding.root


    }

    fun fetchSpareParts(){
        viewModel.fetchSpareParts()
        viewModel.sparePartsLiveData.observe(viewLifecycleOwner) {
            sparePartsAdapter.setSparePartsList(it.products)
        }
    }



}