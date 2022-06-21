package com.example.teqelmasr.displaySellerProducts.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.ProductItem
import com.example.teqelmasr.model.RepositoryInterface
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyProductsViewModel(private val repo: RepositoryInterface): ViewModel() {

    private var _myProducts: MutableLiveData<ArrayList<Product>?>? = MutableLiveData()
    var myProducts: MutableLiveData<ArrayList<Product>?>? = _myProducts

/*    init {
        getMyProducts()
    }*/
    fun getMyProducts(){
        viewModelScope.launch {
            val myProductsRes = repo.getMyProducts()
            withContext(Dispatchers.IO){
                _myProducts?.postValue(myProductsRes.products?.filter {
                    it.vendor.equals(FirebaseAuth.getInstance().currentUser?.uid.toString())

                } as ArrayList<Product>?)
            }
        }
    }

    fun deleteProduct(product: Product){
        viewModelScope.launch(Dispatchers.IO) { repo.deleteProduct(product) }
    }


}