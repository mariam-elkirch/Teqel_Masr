package com.example.teqelmasr.profile.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.teqelmasr.databinding.FragmentProfileBinding
import com.example.teqelmasr.model.Customer
import com.example.teqelmasr.model.CustomerObj
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client
import com.example.teqelmasr.profile.viewmodel.ProfileViewModel
import com.example.teqelmasr.profile.viewmodel.ProfileViewModelFactory

class ProfileFragment : Fragment() {

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
    ): View {
        // Inflate the layout for this fragment
        fetchCustomer()


        return binding.root
    }

    private fun updateCustomer(customerId: Long) {

        Log.i("TAG", "updateCustomer: email ${binding.emailEdt.text.toString().trim()}")

        val customerObj = CustomerObj(
            first_name = binding.nameEdt.text.toString().trim(),
            id = customerId
        )
        val customer = Customer(customerObj)
        viewModel.updateCustomer(customer)
    }

    private fun fetchCustomer() {
        viewModel.fetchCustomers()
        viewModel.customerLiveData.observe(viewLifecycleOwner) { customer ->
            if (!customer.isNullOrEmpty()) {
                binding.emailEdt.setText(customer[0].email.toString())
                binding.nameEdt.setText(customer[0].first_name.toString())
                binding.saveButton.setOnClickListener {
                    updateCustomer(customer[0].id!!)
                    Log.i("TAG", "fetchCustomer: customer id ${customer[0].id!!} ")
                }
            }
        }
    }
}