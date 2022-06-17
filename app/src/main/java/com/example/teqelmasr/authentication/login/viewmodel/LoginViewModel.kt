package com.example.teqelmasr.authentication.login.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
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
    var customer: LiveData<List<CustomerObj>> = _customer

/*    init {
        getCustomer()
    }*/
    fun getCustomer(){
        viewModelScope.launch {
            val customerResponse = repo.getCustomers()
            withContext(Dispatchers.IO){
                _customer.postValue(customerResponse.body()!!.customers.filter { customerObj ->
                    customerObj.last_name.equals(FirebaseAuth.getInstance().currentUser?.uid.toString())
                })
                }
            }
        }
    }

