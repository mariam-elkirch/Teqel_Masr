package com.example.teqelmasr.profile.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentDisplayEquipmentSellBinding
import com.example.teqelmasr.databinding.FragmentProfileBinding
import com.example.teqelmasr.displayEquipmentSell.viewModel.DisplayEquipmentSellViewModel
import com.example.teqelmasr.displayEquipmentSell.viewModel.DisplayEquipmentSellViewModelFactory
import com.example.teqelmasr.model.Customer
import com.example.teqelmasr.model.CustomerObj
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client
import com.example.teqelmasr.profile.viewmodel.ProfileViewModel
import com.example.teqelmasr.profile.viewmodel.ProfileViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    var customerList = ArrayList<CustomerObj>()

    private val binding by lazy { FragmentProfileBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            factory = ProfileViewModelFactory(
                Repository.getInstance(
                    Client.getInstance(),
                    requireContext()
                )
            )
        )[ProfileViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        fetchCustomer()
        return binding.root
    }

    private fun fetchCustomer() {

        viewModel.fetchCustomers()
        viewModel.customerLiveData.observe(viewLifecycleOwner) {
        //  Log.i("tag",it.get(0).email+"customr obj")
            if(!it.isNullOrEmpty()) {
                binding.emailEdt.setText(it.get(0).email.toString())
                binding.nameEdt.setText(it.get(0).first_name.toString())
            }
        }
    }
}