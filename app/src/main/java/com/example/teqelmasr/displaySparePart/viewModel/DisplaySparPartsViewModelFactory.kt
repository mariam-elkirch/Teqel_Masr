package com.example.teqelmasr.displaySparePart.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teqelmasr.model.RepositoryInterface

class DisplaySparPartsViewModelFactory(private val repo: RepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DisplaySparePartsViewModel::class.java)) {
            DisplaySparePartsViewModel(repo) as T
        } else {
            throw IllegalArgumentException("DisplaySparePartsViewModel Class not found")
        }
    }

}
