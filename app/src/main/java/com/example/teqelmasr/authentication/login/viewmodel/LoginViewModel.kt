package com.example.teqelmasr.authentication.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teqelmasr.model.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val repo: RepositoryInterface): ViewModel() {

    private var _customers: MutableLiveData<CustomerObj> = MutableLiveData()
    var customers: MutableLiveData<CustomerObj> = _customers

    fun getCustomers(){
        viewModelScope.launch {
            val customers = repo.getCustomers()
            withContext(Dispatchers.IO){
                _customers.postValue(customers.body()?.customers?.filter {
                    it.last_name.equals(FirebaseAuth.getInstance().currentUser?.uid)
                }?.get(0))
            }
        }
    }
}