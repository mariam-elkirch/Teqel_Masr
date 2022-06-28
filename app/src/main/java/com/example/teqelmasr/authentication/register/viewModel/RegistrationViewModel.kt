package com.example.teqelmasr.authentication.register.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teqelmasr.model.Customer
import com.example.teqelmasr.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationViewModel(private val repo: RepositoryInterface): ViewModel() {
    fun postCustomer(customer: Customer){
        viewModelScope.launch(Dispatchers.IO) { repo.postCustomer(customer) }
    }
}