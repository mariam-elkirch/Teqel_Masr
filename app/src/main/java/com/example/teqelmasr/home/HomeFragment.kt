package com.example.teqelmasr.home

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.teqelmasr.R
import com.example.teqelmasr.authentication.login.LoginActivity
import com.example.teqelmasr.authentication.register.view.RegistrationActivity

import com.example.teqelmasr.databinding.FragmentHomeBinding
import com.example.teqelmasr.helper.Constants

import com.example.teqelmasr.model.Product
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {
    private lateinit var sharedPref: SharedPreferences
    private lateinit var binding: FragmentHomeBinding
    //private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        sharedPref = requireContext().getSharedPreferences(
            "MyPref",
            AppCompatActivity.MODE_PRIVATE
        )
        Log.i("TAG", "onCreateView: ")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("TAG", "onViewCreated: ")
        MobileAds.initialize(
            requireContext()
        )
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

            (sharedPref.getString(Constants.USER_TYPE, Constants.GUEST_TYPE)).let {
                    when (it) {
                        Constants.GUEST_TYPE -> Snackbar.make(
                            binding.root,
                            getString(R.string.have_to_login),
                            Snackbar.LENGTH_INDEFINITE
                        ).setAction(getString(R.string.login)) {
                            startActivity(Intent(requireContext(), LoginActivity::class.java))
                        }.setDuration(6000).show()

                        Constants.BUYER_TYPE -> Snackbar.make(
                            binding.root,
                            getString(R.string.have_to_seller),
                            Snackbar.LENGTH_INDEFINITE
                        ).setAction(getString(R.string.profile)) {
                            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment())
                        }.setDuration(6000).show()

                        Constants.SELLER_TYPE ->
                            binding.root.findNavController().navigate(
                                HomeFragmentDirections.actionHomeFragmentToAddEquipmentSellFragment(
                                    null
                                )
                            )
                    }
                }
        }

    }

    private fun loadBannerAd() {
        /* val customAdSize = AdSize(250, 250)
         val adView = AdManagerAdView(requireContext())
         adView.setAdSizes(customAdSize)*/
        val adRequest = AdRequest.Builder().build()
        binding.adView?.loadAd(adRequest)
    }
}