package com.example.teqelmasr.displayEquipmentSell.view

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentContactInfoBinding
import com.example.teqelmasr.databinding.FragmentEquimentSellBottonSheetFrgmentBinding
import com.example.teqelmasr.databinding.FragmentMapsBinding
import com.example.teqelmasr.location.view.MapsFragmentArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.example.teqelmasr.helper.Constants
import com.example.teqelmasr.location.view.MapsFragmentDirections


class ContactInfoFragment : BottomSheetDialogFragment() {
    private val binding by lazy { FragmentContactInfoBinding.inflate(layoutInflater) }
    private val args by navArgs<ContactInfoFragmentArgs>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)

        /*when (args.source) {

            Constants.ADD_SOURCE -> {
                var product = args.enteredProduct
                Log.i("tag", "price" + product?.price + "ffff")
                product?.address = returnLocationToHome


                val action: NavDirections =
                    MapsFragmentDirections.actionMapsFragmentToAddEquipmentSellFragment(
                        product
                    )
                binding.root.findNavController().navigate(action)
            }
            Constants.EDIT_SOURCE -> {
                val product = args.currentProduct
                product?.variants?.get(0)?.option1 = returnLocationToHome
                val action: NavDirections =
                    MapsFragmentDirections.actionMapsFragmentToEditSellerProductFragment(
                        args.currentProduct!!
                    )
                binding.root.findNavController().navigate(action)

            }
        }*/



        binding.myLocation.setText(args.contactObj?.address)
        binding.telephone.setText(args.contactObj?.telephone)

        binding.callButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:"+args.contactObj?.telephone)
            startActivity(intent)
        }

        return binding.root
    }


}