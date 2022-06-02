package com.example.teqelmasr.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentDisplayEquipmentSellBinding
import com.example.teqelmasr.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
     // binding = FragmentHomeBinding.inflate(inflater, container, false)

        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.equipmentSellBtn.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_homeFragment_to_displayEquipmentSellFragment)
        }
        binding.equipmentRentBtn.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_homeFragment_to_displayEquipmentRentFragment)
        }
        binding.SpareBtn.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_homeFragment_to_displaySparePartFragment)
        }
        binding.sellertn.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_homeFragment_to_displaySellerProductsFragment)
        }
        binding.postProductBtn.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_homeFragment_to_addEquipmentSellFragment)
        }
    }

}