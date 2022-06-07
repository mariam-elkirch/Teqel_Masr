package com.example.teqelmasr.displayEquipmentSell.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentEquimentSellBottonSheetFrgmentBinding
import com.example.teqelmasr.databinding.FragmentFilterBottomSheetBinding
import com.example.teqelmasr.displaySparePart.view.SparePartsFilterBottomSheetFragmentDirections
import com.example.teqelmasr.model.FilterValues
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EquimentSellBottonSheetFrgment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EquimentSellBottonSheetFrgment :  BottomSheetDialogFragment() {

    private val binding by lazy { FragmentEquimentSellBottonSheetFrgmentBinding.inflate(layoutInflater) }
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
                   EquimentSellBottonSheetFrgmentDirections.
                   actionEquimentSellBottonSheetFrgmentToDisplayEquipmentSellFragment()
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


        }
    }

}