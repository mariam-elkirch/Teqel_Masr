package com.example.teqelmasr.addEquipmentSell.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teqelmasr.model.ProductPost
import com.example.teqelmasr.model.RepositoryInterface

class AddProductViewModelFactory(private val repo: RepositoryInterface): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(AddProductViewModel::class.java)){
            AddProductViewModel(repo) as T

        }else{
            throw IllegalAccessException("Not found view model")
        }
    }

}