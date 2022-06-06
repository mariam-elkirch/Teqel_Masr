package com.example.teqelmasr.displaySparePart.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.navigation.fragment.findNavController
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentFilterBottomSheetBinding
import com.example.teqelmasr.model.FilterValues
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SparePartsFilterBottomSheetFragment : BottomSheetDialogFragment() {

    private val binding by lazy { FragmentFilterBottomSheetBinding.inflate(layoutInflater) }
    var typesArray = mutableSetOf<String>()


    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        var filterValues = FilterValues()

        filterValues.types = typesArray
        binding.apply {
            configureTypesCheckBoxes()
            rangeSlider.addOnChangeListener { _, _, _ ->
                filterValues.priceStart = rangeSlider.values[0]
                filterValues.priceEnd = rangeSlider.values[1]
            }
            applyButton.setOnClickListener {
                val action =
                    SparePartsFilterBottomSheetFragmentDirections.actionSparePartsFilterBottomSheetFragmentToDisplaySparePartFragment(
                        filterValues
                    )
                findNavController().navigate(action)
            }

        }
        return binding.root
    }

    private fun configureTypesCheckBoxes() {
        binding.apply {
            one.setOnCheckedChangeListener { _, isClicked ->
                if (isClicked) {
                    typesArray.add(oneText.text.toString())
                    Log.i("TAG", " types array ${typesArray.size}")
                }else if (!isClicked){
                    typesArray.remove(oneText.text.toString())
                }

            }
            two.setOnCheckedChangeListener { _, isClicked ->
                if (isClicked) {
                    typesArray.add(twoText.text.toString())
                    Log.i("TAG", " types array ${typesArray.size}")
                }else if (!isClicked){
                    typesArray.remove(twoText.text.toString())
                }

            }
            three.setOnCheckedChangeListener { _, isClicked ->
                if (isClicked) {
                    typesArray.add(threeText.text.toString())
                    Log.i("TAG", " types array ${typesArray.size}")
                }else if (!isClicked){
                    typesArray.remove(threeText.text.toString())
                }

            }
            four.setOnCheckedChangeListener { _, isClicked ->
                if (isClicked) {
                    typesArray.add(fourText.text.toString())
                    Log.i("TAG", " types array ${typesArray.size}")
                }else if (!isClicked){
                    typesArray.remove(fourText.text.toString())
                }

            }
            five.setOnCheckedChangeListener { _, isClicked ->
                if (isClicked) {
                    typesArray.add(fiveText.text.toString())
                    Log.i("TAG", " types array ${typesArray.size}")
                }else if (!isClicked){
                    typesArray.remove(fiveText.text.toString())
                }

            }
            six.setOnCheckedChangeListener { _, isClicked ->
                if (isClicked) {
                    typesArray.add(sixText.text.toString())
                    Log.i("TAG", " types array ${typesArray.size}")
                }else if (!isClicked){
                    typesArray.remove(sixText.text.toString())
                }

            }
            seven.setOnCheckedChangeListener { _, isClicked ->
                if (isClicked) {
                    typesArray.add(sevenText.text.toString())
                    Log.i("TAG", " types array ${typesArray.size}")
                }else if (!isClicked){
                    typesArray.remove(sevenText.text.toString())
                }

            }
            eight.setOnCheckedChangeListener { _, isClicked ->
                if (isClicked) {
                    typesArray.add(eightText.text.toString())
                    Log.i("TAG", " types array ${typesArray.size}")
                }else if (!isClicked){
                    typesArray.remove(eightText.text.toString())
                }

            }
            nine.setOnCheckedChangeListener { _, isClicked ->
                if (isClicked) {
                    typesArray.add(nineText.text.toString())
                    Log.i("TAG", " types array ${typesArray.size}")
                }else if (!isClicked){
                    typesArray.remove(nineText.text.toString())
                }

            }
            ten.setOnCheckedChangeListener { _, isClicked ->
                if (isClicked) {
                    typesArray.add(tenText.text.toString())
                    Log.i("TAG", " types array ${typesArray.size}")
                }else if (!isClicked){
                    typesArray.remove(tenText.text.toString())
                }

            }
            eleven.setOnCheckedChangeListener { _, isClicked ->
                if (isClicked) {
                    typesArray.add(elevenText.text.toString())
                    Log.i("TAG", " types array ${typesArray.size}")
                }else if (!isClicked){
                    typesArray.remove(elevenText.text.toString())
                }

            }
            twelve.setOnCheckedChangeListener { _, isClicked ->
                if (isClicked) {
                    typesArray.add(twelveText.text.toString())
                    Log.i("TAG", " types array ${typesArray.size}")
                }else if (!isClicked){
                    typesArray.remove(twelveText.text.toString())
                }

            }
            thirteen.setOnCheckedChangeListener { _, isClicked ->
                if (isClicked) {
                    typesArray.add(thirteenText.text.toString())
                    Log.i("TAG", " types array ${typesArray.size}")
                }else if (!isClicked){
                    typesArray.remove(thirteenText.text.toString())
                }

            }
            fourteen.setOnCheckedChangeListener { _, isClicked ->
                if (isClicked) {
                    typesArray.add(fourteenText.text.toString())
                    Log.i("TAG", " types array ${typesArray.size}")
                }else if (!isClicked){
                    typesArray.remove(fourteenText.text.toString())
                }

            }
        }
    }

}