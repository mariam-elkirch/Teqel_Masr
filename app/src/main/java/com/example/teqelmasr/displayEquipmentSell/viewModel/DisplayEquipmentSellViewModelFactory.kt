package com.example.teqelmasr.displayEquipmentSell.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teqelmasr.displayEquipmentRent.viewModel.DisplayRentEquipmentViewModel
import com.example.teqelmasr.model.RepositoryInterface

class DisplayEquipmentSellViewModelFactory (private val repo: RepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DisplayEquipmentSellViewModel::class.java)) {
           DisplayEquipmentSellViewModel(repo) as T
        } else {
            throw IllegalArgumentException("DisplayEquipentSellViewModel Class not found")
        }
    }

}