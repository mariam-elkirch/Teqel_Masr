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
import java.util.*
import kotlin.collections.ArrayList

class EquipmentRentFilterBottomSheetFragment : BottomSheetDialogFragment() , OnProductClickListener {
    private val binding by lazy { FragmentEquipmentRentFilterBottomSheetBinding.inflate(layoutInflater) }
    var typesArray = mutableSetOf<String>()
    val allProductList = ArrayList<Product>()
    val filterResultList = ArrayList<Product>()
    private val viewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            factory = DisplayRentEquipmentViewModelFactory(
                Repository.getInstance(
                    Client.getInstance(),
                    requireContext()
                )
            )
        )[DisplayRentEquipmentViewModel::class.java]
    }
    private val equipmentRentAdapter by lazy {
        DisplayRentEquipmentRecyclerAdapter(
            requireContext(),
            this)}

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
        fetchEquipmentRent()
        var filterValues = FilterValues()

        filterValues.types = typesArray
        binding.apply {
            configureTypesCheckBoxes()
            rangeSlider.addOnChangeListener { _, _, _ ->
                filterValues.priceStart = rangeSlider.values[0]
                filterValues.priceEnd = rangeSlider.values[1]
            }
            applyButton.setOnClickListener {
                filterByProductType()
                val action =
                    EquipmentRentFilterBottomSheetFragmentDirections.actionEquipmentRentFilterBottomSheetFragmentToDisplayEquipmentRentFragment(
                        filterResultList.toTypedArray()
                    )
                findNavController().navigate(action)
                equipmentRentAdapter.setEquipmentRentList(filterResultList)
            }

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    private fun fetchEquipmentRent() {
        viewModel.rentEquipmentLiveData.observe(viewLifecycleOwner) {
           // equipmentRentAdapter.setEquipmentRentList(it.products!!)
            allProductList.addAll(it.products!!)

        }

    }
    private fun filterByProductType(){

        for(i in 0 until allProductList.size){
        for (cat in typesArray){
            if (allProductList[i].productType.toString().lowercase(Locale.getDefault()).equals(cat)){
                filterResultList.add(allProductList[i])
            }

            }
        }
        Log.i("TAG", " All products ${allProductList.size}")

        Log.i("TAG", " filtered products ${filterResultList.size}")

    }




    override fun onProductClick(product: Product) {
        TODO("Not yet implemented")
    }

    override fun onEmptyList(searchKey: String) {
        TODO("Not yet implemented")
    }

    override fun onFullList() {
        TODO("Not yet implemented")
    }

}