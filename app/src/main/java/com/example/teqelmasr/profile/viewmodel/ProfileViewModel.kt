package com.example.teqelmasr.profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teqelmasr.model.CustomerItem
import com.example.teqelmasr.model.CustomerObj
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.RepositoryInterface
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel (private val repository: RepositoryInterface) : ViewModel() {
    private val customerMutableLiveData: MutableLiveData<List<CustomerObj>> = MutableLiveData()
    val customerLiveData: LiveData<List<CustomerObj>> =customerMutableLiveData


    init {
        fetchCustomers()
    }

    fun fetchCustomers() {
        viewModelScope.launch(Dispatchers.IO) {
            // val response = repository.getProductsByCategory(collectionID)
            val response = repository.getCustomer()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    customerMutableLiveData.postValue(response.body()!!.customers!!
                        .filter { customer -> customer.last_name == FirebaseAuth.getInstance().currentUser?.uid.toString() })
               //   Log.i("tag",response.body().toString())
                    // sellEquipmentMutableLiveData.postValue(response.body())
                } else {
                    Log.e(
                        "DisplayCustomrViewModel",
                        "Error fetching data in Customer ${response.message()}"
                    )
                }
            }
        }
    }
}