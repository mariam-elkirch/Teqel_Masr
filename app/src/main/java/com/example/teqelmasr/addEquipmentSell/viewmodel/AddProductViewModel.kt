package com.example.teqelmasr.addEquipmentSell.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teqelmasr.model.ProductItem
import com.example.teqelmasr.model.ProductPost
import com.example.teqelmasr.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AddProductViewModel(private val repo: RepositoryInterface, product: ProductPost): ViewModel() {
        val errorMessage = MutableLiveData<String>()
        private var _myProducts: MutableLiveData<ProductItem> = MutableLiveData()
        var myProducts: LiveData<ProductItem> = _myProducts

        init {
            postProduct(product)
        }
        fun postProduct(product: ProductPost){
            viewModelScope.launch {
                val myProductsRes = repo.storeProduct(product)
                withContext(Dispatchers.IO){
                    _myProducts.postValue(myProductsRes.body())
                    errorMessage.postValue(myProductsRes.raw().toString())
                }
            }
        }


    }
