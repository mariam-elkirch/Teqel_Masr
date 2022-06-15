package com.example.teqelmasr.favourite.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teqelmasr.displayEquipmentRent.viewModel.DisplayRentEquipmentViewModel
import com.example.teqelmasr.model.RepositoryInterface

class AddToFavoriteViewModelFactory (private val repo: RepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AddToFavoriteViewModel::class.java)) {
            AddToFavoriteViewModel(repo) as T
        } else {
            throw IllegalArgumentException("AddToFavoriteViewModel Class not found")
        }
    }
}