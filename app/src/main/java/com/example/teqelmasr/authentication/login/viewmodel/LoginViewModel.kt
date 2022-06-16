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

    private var _customer: MutableLiveData<List<CustomerObj>> = MutableLiveData()
    var customer: MutableLiveData<List<CustomerObj>> = _customer

    init {
        getCustomer()
    }
    private fun getCustomer(){
        viewModelScope.launch {
            val customerResponse = repo.getCustomers()
            withContext(Dispatchers.IO){
                customer.postValue(customerResponse.body()!!.customers.filter { customerObj ->
                    customerObj.last_name.equals(FirebaseAuth.getInstance().currentUser?.uid.toString())
                })
                }
            }
        }
    }

