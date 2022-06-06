package com.example.teqelmasr.displaySellerProducts.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentFilterationSheetBinding
import com.example.teqelmasr.model.FilterObj
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.slider.RangeSlider


class FiltrationSheetFragment() : BottomSheetDialogFragment() {

    private val TAG = "BottomSheet"
    private lateinit var binding: FragmentFilterationSheetBinding
    private lateinit var start: String
    private lateinit var end: String
    private var categories: ArrayList<String> = ArrayList()
    private var types: ArrayList<String> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterationSheetBinding.inflate(layoutInflater)
        start = binding.rangeSlider.values[0].toString()
        end = binding.rangeSlider.values[1].toString()
        binding.rangeSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: RangeSlider) {
                val values = slider.values
                start = values[0].toString()
                end = values[1].toString()
                Log.i(TAG, "start: ${start} end: ${end}")
            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: RangeSlider) {
                val values = slider.values
                start = values[0].toString()
                end = values[1].toString()
                Log.i(TAG, "start: ${start} end: ${end}")
            }

        })

        binding.applyBtn.setOnClickListener {
            appendCategories()
            appendTypes()
            val filterObj =
                FilterObj(IntRange(start = start.toDouble().toInt(), endInclusive = end.toDouble().toInt()), categories, types)
            val action: NavDirections =
                FiltrationSheetFragmentDirections.actionFiltrationSheetFragmentToDisplaySellerProductsFragment(filterObj)
            findNavController().navigate(action)

        }
        return binding.root
    }

    private fun appendTypes() {

        for (chip in binding.chipGroup.children) {
            val currentChip = chip as Chip
            if (currentChip.isChecked) {
                types.add(currentChip.text.toString())
            }
        }


    }

    private fun appendCategories() {
        if (binding.one.isChecked) {
            categories.add(binding.firstCategory.text.toString())
        }
        if (binding.two.isChecked) {
            categories.add(binding.secondCategory.text.toString())
        }
        if (binding.three.isChecked) {
            categories.add(binding.thirdCategory.text.toString())
        }

    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme)
}