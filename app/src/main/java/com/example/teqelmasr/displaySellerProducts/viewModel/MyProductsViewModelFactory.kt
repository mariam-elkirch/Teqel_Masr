package com.example.teqelmasr.displaySellerProducts.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teqelmasr.model.RepositoryInterface

class MyProductsViewModelFactory(private val repo: RepositoryInterface): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(MyProductsViewModel::class.java)){
            MyProductsViewModel(repo) as T
        }else{
            throw IllegalAccessException("Not found view model")
        }
    }

}