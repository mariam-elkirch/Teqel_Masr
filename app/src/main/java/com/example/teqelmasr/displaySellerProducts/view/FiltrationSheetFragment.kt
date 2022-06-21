package com.example.teqelmasr.displaySellerProducts.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentFilterationSheetBinding
import com.example.teqelmasr.helper.Constants
import com.example.teqelmasr.model.FilterObj
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.slider.RangeSlider
import java.text.NumberFormat


class FiltrationSheetFragment() : BottomSheetDialogFragment() {

    private val TAG = "BottomSheet"
    private lateinit var binding: FragmentFilterationSheetBinding
    private lateinit var start: String
    private lateinit var end: String
    private var categories: ArrayList<String> = ArrayList()
    private var types: ArrayList<String> = ArrayList()
    private val args by navArgs<FiltrationSheetFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView:sourceFragment ${args.sourceFragment}")
        binding = FragmentFilterationSheetBinding.inflate(layoutInflater)
        start = binding.rangeSlider.values[0].toString()
        end = binding.rangeSlider.values[1].toString()
        binding.rangeSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: RangeSlider) {
                val values = slider.values
                start = values[0].toString()
                end = values[1].toString()
            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: RangeSlider) {
                val values = slider.values
                start = values[0].toString()
                end = values[1].toString()
            }

        })
        binding.rangeSlider.setLabelFormatter { value: Float ->
            val format = NumberFormat.getInstance()
            format.maximumFractionDigits = 0

            format.format(value.toDouble())
        }

        binding.applyBtn.setOnClickListener {
            appendCategories()
            appendTypes()
            val filterObj =
                FilterObj(
                    IntRange(
                        start = start.toDouble().toInt(),
                        endInclusive = end.toDouble().toInt()
                    ), categories, types
                )
            if (args.sourceFragment == Constants.DISPLAY_SELLER){
                val action: NavDirections =
                    FiltrationSheetFragmentDirections.actionFiltrationSheetFragmentToDisplaySellerProductsFragment(
                        filterObj
                    )
                findNavController().navigate(action)
            } else if (args.sourceFragment == Constants.MARKET_FRAGMENT){
                val action: NavDirections =
                    FiltrationSheetFragmentDirections.actionFiltrationSheetFragmentToMarketFragment(
                        filterObj
                    )
                findNavController().navigate(action)
            }


        }

        binding.apply {
            firstCategory.text = getString(R.string.sell_equip_txt)
            secondCategory.text = getString(R.string.rent_equip_txt)
            thirdCategory.text = getString(R.string.spare_parts)
        }

        binding.sellCheckBox.setOnCheckedChangeListener { compoundButton, b ->
            if(compoundButton.isChecked){
                if(binding.spareCheckBox.isChecked){
                    binding.spareGroup.forEach { it.isEnabled = true }
                    binding.equipGroup.forEach { it.isEnabled = true }

                }else{
                    binding.spareGroup.forEach { it.isEnabled = false }

                }
            }else{
                if(binding.rentCheckBox.isChecked && !(binding.spareCheckBox.isChecked)){
                    binding.spareGroup.forEach { it.isEnabled = false }
                    binding.equipGroup.forEach { it.isEnabled = true }

                }else if(binding.rentCheckBox.isChecked && binding.spareCheckBox.isChecked){
                    binding.equipGroup.forEach { it.isEnabled = true }
                    binding.spareGroup.forEach { it.isEnabled = true }

                }else if(!(binding.rentCheckBox.isChecked) && binding.spareCheckBox.isChecked){
                    binding.equipGroup.forEach { it.isEnabled = false }
                    binding.spareGroup.forEach { it.isEnabled = true }


                }else if(!(binding.rentCheckBox.isChecked) && !(binding.spareCheckBox.isChecked)){
                    binding.equipGroup.forEach { it.isEnabled = true }
                    binding.spareGroup.forEach { it.isEnabled = true }
                }
            }
        }

        binding.rentCheckBox.setOnCheckedChangeListener { compoundButton, b ->
            if(compoundButton.isChecked){
                if(binding.spareCheckBox.isChecked){
                    binding.spareGroup.forEach { it.isEnabled = true }
                    binding.equipGroup.forEach { it.isEnabled = true }

                }else{
                    binding.spareGroup.forEach { it.isEnabled = false }
                }
            }else{
                if(binding.sellCheckBox.isChecked && binding.spareCheckBox.isChecked){
                    binding.equipGroup.forEach { it.isEnabled = true }
                    binding.spareGroup.forEach { it.isEnabled = true }

                }else if(binding.sellCheckBox.isChecked && !(binding.spareCheckBox.isChecked)){
                    binding.spareGroup.forEach { it.isEnabled = false }
                    binding.equipGroup.forEach { it.isEnabled = true }

                }else if(!(binding.sellCheckBox.isChecked) && !(binding.spareCheckBox.isChecked)){
                    binding.spareGroup.forEach { it.isEnabled = true }
                    binding.equipGroup.forEach { it.isEnabled = true }
                }else if(!(binding.sellCheckBox.isChecked) && binding.spareCheckBox.isChecked){
                    binding.spareGroup.forEach { it.isEnabled = true }
                    binding.equipGroup.forEach { it.isEnabled = false}
                }
            }
        }

        binding.spareCheckBox.setOnCheckedChangeListener { compoundButton, b ->
            if(compoundButton.isChecked){
                if(binding.sellCheckBox.isChecked && binding.rentCheckBox.isChecked){
                    binding.spareGroup.forEach { it.isEnabled = true }
                    binding.equipGroup.forEach { it.isEnabled = true }
                }else if(!(binding.sellCheckBox.isChecked) && !(binding.rentCheckBox.isChecked)){
                    binding.spareGroup.forEach { it.isEnabled = true }
                    binding.equipGroup.forEach { it.isEnabled = false }
                }else if(!(binding.sellCheckBox.isChecked) && binding.rentCheckBox.isChecked){
                    binding.spareGroup.forEach { it.isEnabled = true }
                    binding.equipGroup.forEach { it.isEnabled = true }
                }else if(binding.sellCheckBox.isChecked && !(binding.rentCheckBox.isChecked)){
                    binding.spareGroup.forEach { it.isEnabled = true }
                    binding.equipGroup.forEach { it.isEnabled = true }
                }
            }else{
                if(binding.sellCheckBox.isChecked || binding.rentCheckBox.isChecked){
                    binding.spareGroup.forEach { it.isEnabled = false }
                    binding.equipGroup.forEach { it.isEnabled = true }
                }else if(!(binding.sellCheckBox.isChecked) && !(binding.rentCheckBox.isChecked)){
                    binding.spareGroup.forEach { it.isEnabled = true }
                    binding.equipGroup.forEach { it.isEnabled = true }
                }

            }
        }


        return binding.root
    }

    private fun appendTypes() {

        binding.spareGroup.forEach {
            if((it as Chip).isChecked){
                types.add(it.text.toString())
            }
        }
        binding.equipGroup.forEach {
            if((it as Chip).isChecked){
                types.add(it.text.toString())
            }
        }


    }

    private fun appendCategories() {
        if (binding.sellCheckBox.isChecked) {
            categories.add("equimentsell")
        }
        if (binding.rentCheckBox.isChecked) {
            categories.add("equimentrent")
        }
        if (binding.spareCheckBox.isChecked) {
            categories.add("spare")
        }

    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme)
}