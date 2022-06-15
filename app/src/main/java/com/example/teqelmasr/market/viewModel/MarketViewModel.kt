package com.example.teqelmasr.market.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.ProductItem
import com.example.teqelmasr.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MarketViewModel(private val repository: RepositoryInterface)  : ViewModel() {

    private val allProductsMutableLiveData: MutableLiveData<List<Product>> = MutableLiveData()
    val allProductsLiveData: LiveData<List<Product>> = allProductsMutableLiveData

    fun getAllProducts(){
        viewModelScope.launch {
            val response = repository.getAllProducts()

            withContext(Dispatchers.IO){
                allProductsMutableLiveData.postValue(response.body()?.products!!)
                Log.i("TAG", "getAllProducts: ${response.body()?.products!!.size}")
            }

        }
    }
}