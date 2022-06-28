package com.example.teqelmasr.authentication.register.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.teqelmasr.displaySellerProducts.viewModel.MyProductsViewModel
import com.example.teqelmasr.model.RepositoryInterface

class RegistrationViewModelFactory(private val repo: RepositoryInterface): ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(RegistrationViewModel::class.java)){
            RegistrationViewModel(repo) as T
        }else{
            throw IllegalAccessException("Not found view model")
        }    }
}