package com.example.teqelmasr.displayEquipmentRent.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teqelmasr.model.RepositoryInterface

class DisplayRentEquipmentViewModelFactory(private val repo: RepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DisplayRentEquipmentViewModel::class.java)) {
            DisplayRentEquipmentViewModel(repo) as T
        } else {
            throw IllegalArgumentException("DisplaySparePartsViewModel Class not found")
        }
    }

}