package com.example.teqelmasr.profile.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentDisplayEquipmentSellBinding
import com.example.teqelmasr.databinding.FragmentProfileBinding
import com.example.teqelmasr.displayEquipmentSell.viewModel.DisplayEquipmentSellViewModel
import com.example.teqelmasr.displayEquipmentSell.viewModel.DisplayEquipmentSellViewModelFactory
import com.example.teqelmasr.helper.Constants
import com.example.teqelmasr.model.Customer
import com.example.teqelmasr.model.CustomerObj
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client
import com.example.teqelmasr.profile.viewmodel.ProfileViewModel
import com.example.teqelmasr.profile.viewmodel.ProfileViewModelFactory
import java.util.*

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

    private fun updateCustomer(customerId: Long, customerName: String, customerType: String) {


        val id = binding.radioGroup.checkedRadioButtonId
        val radioButton: RadioButton = binding.root.findViewById(id)

        if (customerName != binding.nameEdt.text.toString()
                .trim() || customerType != radioButton.text.toString()
                .lowercase(Locale.getDefault())
        ) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(requireActivity().getString(R.string.edit_profile))
            builder.setMessage(getString(R.string.sure_to_edit_profile))

            builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
                val customerObj = CustomerObj(
                    first_name = binding.nameEdt.text.toString().trim(),
                    note = radioButton.text.toString()
                        .lowercase(Locale.getDefault()),
                    id = customerId
                )
                val customer = Customer(customerObj)
                viewModel.updateCustomer(customer)
                Toast.makeText(requireContext(), getString(R.string.profile_updated), Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
            }

            builder.setNegativeButton(getString(R.string.no)) { _, _ ->
            }

            builder.show()
        }


    }

    private fun fetchCustomer() {

        viewModel.fetchCustomers()

        viewModel.customerLiveData.observe(viewLifecycleOwner) { customer ->
            if (!customer.isNullOrEmpty()) {
                binding.emailEdt.setText(customer[0].email.toString())
                binding.nameEdt.setText(customer[0].first_name.toString())
                when (customer[0].note!!) {
                    Constants.SELLER_TYPE -> binding.sellerRadioButton.isChecked = true
                    Constants.BUYER_TYPE -> binding.buyerRadioButton.isChecked = true
                }
                binding.saveButton.setOnClickListener {
                    updateCustomer(customer[0].id!!, customer[0].first_name!!, customer[0].note!!)
                    Log.i(
                        "TAG",
                        "fetchCustomer: customer id ${customer[0].id!!}  customer Type ${customer[0].note!!} "
                    )
                }
            }
        }
    }
}