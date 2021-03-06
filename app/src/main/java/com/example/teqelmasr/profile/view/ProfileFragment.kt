package com.example.teqelmasr.profile.view


import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentProfileBinding
import com.example.teqelmasr.helper.Constants
import com.example.teqelmasr.home.HomeActivity
import com.example.teqelmasr.model.Customer
import com.example.teqelmasr.model.CustomerObj
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client
import com.example.teqelmasr.profile.viewmodel.ProfileViewModel
import com.example.teqelmasr.profile.viewmodel.ProfileViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class ProfileFragment : Fragment() {

    private lateinit var sharedPref: SharedPreferences

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
        sharedPref = requireContext().getSharedPreferences("MyPref",
            AppCompatActivity.MODE_PRIVATE
        )

        displayWaitingDialog()

        val editor: SharedPreferences.Editor = sharedPref.edit()
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        fetchCustomer()
        //val id = binding.radioGrouplang.checkedRadioButtonId
      //  val radioButton: RadioButton = binding.root.findViewById(id)
/*
        id.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->


                if(radio.text.toString().equals(R.string.English) ) {
                    val config = context?.resources?.configuration
                    editor.putString("lang", "en")
                    editor.apply()
                    val locale = Locale("en")
                    Locale.setDefault(locale)
                    config?.setLocale(locale)

                    context?.createConfigurationContext(config!!)
                    context?.resources?.updateConfiguration(
                        config,
                        context?.resources!!.displayMetrics
                    )
                }
                if(radio.text.toString().equals(R.string.Arabic)){
                    val config = context?.resources?.configuration
                    editor.putString("lang","ar")
                    editor.apply()
                    val locale = Locale("ar")
                    Locale.setDefault(locale)
                    config?.setLocale(locale)

                    context?.createConfigurationContext(config!!)
                    context?.resources?.updateConfiguration(config, context?.resources!!.displayMetrics)
                }

                /* Toast.makeText(activity," On checked change :"+
                         " ${radio.text}",
                     Toast.LENGTH_SHORT).show()*/
            })*/
      /*  when(radioButton.text.toString()){
            getString(R.string.English) -> {
                val config = context?.resources?.configuration
                editor.putString("lang","en")
                editor.apply()
                val locale = Locale("en")
                Locale.setDefault(locale)
                config?.setLocale(locale)

                context?.createConfigurationContext(config!!)
                context?.resources?.updateConfiguration(config, context?.resources!!.displayMetrics)
            }
            getString(R.string.Arabic) -> {
                val config = context?.resources?.configuration
                editor.putString("lang","ar")
                editor.apply()
                val locale = Locale("ar")
                Locale.setDefault(locale)
                config?.setLocale(locale)

                context?.createConfigurationContext(config!!)
                context?.resources?.updateConfiguration(config, context?.resources!!.displayMetrics)
            }
        }
*/


        return binding.root
    }

    private fun updateCustomer(customerId: Long, customerName: String, customerType: String) {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        val name = binding.nameEdt.text.toString().trim()


        val id = binding.radioGroup.checkedRadioButtonId
        val radioButton: RadioButton = binding.root.findViewById(id)

        if (customerName != binding.nameEdt.text.toString()
                .trim() || customerType != radioButton.text.toString()
                .lowercase(Locale.getDefault()) && isValidName()
        ) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(requireActivity().getString(R.string.edit_profile))
            builder.setMessage(getString(R.string.sure_to_edit_profile))

            builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
                val customerObj = CustomerObj(
                    first_name = name,
                    note = when(binding.sellerRadioButton.isChecked){
                                true -> Constants.SELLER_TYPE
                                else -> {Constants.BUYER_TYPE}
                    }
                    ,id = customerId
                )
                when(radioButton.text.toString().lowercase(Locale.getDefault())){
                    getString(R.string.seller_note) -> editor.putString(Constants.USER_TYPE,Constants.SELLER_TYPE)
                    getString(R.string.buyer_note) -> editor.putString(Constants.USER_TYPE,Constants.BUYER_TYPE)
                }
                editor.apply()

                val customer = Customer(customerObj)
                viewModel.updateCustomer(customer)
                //displayWaitingDialog()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.profile_updated),
                    Toast.LENGTH_LONG
                ).show()
                //findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
                startActivity(Intent(requireContext(), HomeActivity::class.java))

            }

            builder.setNegativeButton(getString(R.string.no)) { _, _ ->
            }

            builder.show()
        } else{
            Toast.makeText(
                requireContext(),
                getString(R.string.no_changes),
                Toast.LENGTH_LONG
            ).show()
        }


    }


    private fun isValidName(): Boolean {
        val nameInput: String =  binding.nameEdt.text.toString().trim()
        val isValidate: Boolean = if (nameInput.isEmpty()) {
            binding.nameEdt.setError(getString(R.string.name_error))
            false
        } else {
            binding.nameEdt.setError(null)
            true
        }
        return isValidate
    }

    private fun displayWaitingDialog() {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_progress)
        CoroutineScope(Dispatchers.Main).launch {
            dialog.show()
            delay(2000)
            dialog.dismiss()
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