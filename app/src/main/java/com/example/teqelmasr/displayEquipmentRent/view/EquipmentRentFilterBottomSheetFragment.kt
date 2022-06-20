package com.example.teqelmasr.displayEquipmentRent.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentEquipmentRentFilterBottomSheetBinding
import com.example.teqelmasr.displayEquipmentRent.viewModel.DisplayRentEquipmentViewModel
import com.example.teqelmasr.displayEquipmentRent.viewModel.DisplayRentEquipmentViewModelFactory
import com.example.teqelmasr.displaySparePart.view.OnProductClickListener
import com.example.teqelmasr.model.FilterValues
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class EquipmentRentFilterBottomSheetFragment : BottomSheetDialogFragment()  {
    private val binding by lazy { FragmentEquipmentRentFilterBottomSheetBinding.inflate(layoutInflater) }
    var typesArray = mutableSetOf<String>()

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var filterValues = FilterValues()

        filterValues.types = typesArray
        binding.apply {
            configureTypesCheckBoxes()
            rangePriceSlider.addOnChangeListener { _, _, _ ->
                filterValues.priceStart = rangePriceSlider.values[0]
                filterValues.priceEnd = rangePriceSlider.values[1]
            }
            rangePriceSlider.setLabelFormatter { value: Float ->
                val format = NumberFormat.getInstance()
                format.maximumFractionDigits = 0
                // format.roundingMode = RoundingMode.CEILING
                // format.currency = Currency.getInstance("EGP")
                format.format(value.toDouble())
            }

            applyButton.setOnClickListener {
                val action =
                    EquipmentRentFilterBottomSheetFragmentDirections.actionEquipmentRentFilterBottomSheetFragmentToDisplayEquipmentRentFragment(
                        filterValues
                    )
                findNavController().navigate(action)
            }

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    private fun configureTypesCheckBoxes() {
        binding.apply {
            categoryOneCheckBox.setOnCheckedChangeListener { _, isClicked ->
                if (isClicked) {
                    typesArray.add(categoryOneText.text.toString())
                    Log.i("TAG", " types array ${typesArray.size}")
                }else if (!isClicked){
                    typesArray.remove(categoryOneText.text.toString())
                }

            }
            categoryTwoCheckBox.setOnCheckedChangeListener { _, isClicked ->
                if (isClicked) {
                    typesArray.add(categoryTwoText.text.toString())
                    Log.i("TAG", " types array ${typesArray.size}")
                }else if (!isClicked){
                    typesArray.remove(categoryTwoText.text.toString())
                }

            }
            categoryThreeCheckBox.setOnCheckedChangeListener { _, isClicked ->
                if (isClicked) {
                    typesArray.add(categoryThreeText.text.toString())
                    Log.i("TAG", " types array ${typesArray.size}")
                }else if (!isClicked){
                    typesArray.remove(categoryThreeText.text.toString())
                }

            }
            categoryFourCheckBox.setOnCheckedChangeListener { _, isClicked ->
                if (isClicked) {
                    typesArray.add(categoryFourText.text.toString())
                    Log.i("TAG", " types array ${typesArray.size}")
                }else if (!isClicked){
                    typesArray.remove(categoryFourText.text.toString())
                }

            }
            categoryFiveCheckBox.setOnCheckedChangeListener { _, isClicked ->
                if (isClicked) {
                    typesArray.add(categoryFiveText.text.toString())
                    Log.i("TAG", " types array ${typesArray.size}")
                }else if (!isClicked){
                    typesArray.remove(categoryFiveText.text.toString())
                }

            }


        }

    }
}