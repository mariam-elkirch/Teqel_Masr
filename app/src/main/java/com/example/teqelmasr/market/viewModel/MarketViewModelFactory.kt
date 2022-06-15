package com.example.teqelmasr.market.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teqelmasr.displaySparePart.viewModel.DisplaySparePartsViewModel
import com.example.teqelmasr.model.RepositoryInterface

class MarketViewModelFactory(private val repository: RepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MarketViewModel::class.java)){
            MarketViewModel(repository) as T
        }else{
            throw IllegalArgumentException("MarketViewModel Class not found")
        }
    }
}

