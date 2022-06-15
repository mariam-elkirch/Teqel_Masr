package com.example.teqelmasr.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentDisplayEquipmentSellBinding
import com.example.teqelmasr.databinding.FragmentHomeBinding
import com.example.teqelmasr.displaySparePart.view.SparePartsFilterBottomSheetFragmentDirections
import com.example.teqelmasr.model.Product
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

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
        MobileAds.initialize(
            requireContext())
        loadBannerAd()
        binding.equipmentSellBtn.setOnClickListener {
           // binding.root.findNavController().navigate(R.id.action_homeFragment_to_displayEquipmentSellFragment)
            val action =
                HomeFragmentDirections.actionHomeFragmentToDisplayEquipmentSellFragment(null)
            findNavController().navigate(action)
        }
        binding.equipmentRentBtn.setOnClickListener {
            //     binding.root.findNavController().navigate(R.id.action_homeFragment_to_displayEquipmentRentFragment)
            val action: NavDirections =
                HomeFragmentDirections.actionHomeFragmentToDisplayEquipmentRentFragment(
                    null
                )
            findNavController().navigate(action)
        }
        binding.SpareBtn.setOnClickListener {
            val action =
                HomeFragmentDirections.actionHomeFragmentToDisplaySparePartFragment(null)
            findNavController().navigate(action)

        }
        binding.sellertn.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser?.uid.isNullOrEmpty()) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.have_to_login),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(getString(R.string.login)) {
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                }.setDuration(6000).show()

            } else {
                val action: NavDirections =
                    HomeFragmentDirections.actionHomeFragmentToAddEquipmentSellFragment(null)
                binding.root.findNavController().navigate(action)
            }

    }
    private fun loadBannerAd() {

        val adRequest = AdRequest.Builder().build()
        binding.adView?.loadAd(adRequest)
    }
}