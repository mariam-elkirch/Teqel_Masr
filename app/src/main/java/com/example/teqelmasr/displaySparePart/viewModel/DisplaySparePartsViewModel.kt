package com.example.teqelmasr.displaySparePart.viewModel

import FavouriteProduct
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

class DisplaySparePartsViewModel(private val repository: RepositoryInterface) : ViewModel() {
    private val sparePartsMutableLiveData: MutableLiveData<List<Product>> = MutableLiveData()
    val sparePartsLiveData: LiveData<List<Product>> = sparePartsMutableLiveData
    val favouriteResponse = MutableLiveData<FavouriteProduct>()

    //private val filteredSparePartsMutableLiveData: MutableLiveData<List<Product>> =
 //       MutableLiveData()
   // val filteredSparePartsLiveData: LiveData<List<Product>> = filteredSparePartsMutableLiveData

    private val collectionID = 271217819784


    fun fetchSpareParts() {
        Log.i("TAG", "fetchSpareParts: ViewModel")
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getAllProducts()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    sparePartsMutableLiveData.postValue(response.body()!!.products!!
                        .filter { product -> product.tags == "spare" })
                } else {
                    //sparePartsMutableLiveData.postValue(null)
                    Log.e(
                        "DisplaySparePartsViewModel",
                        "Error fetching data in DisplaySparePartsViewModel ${response.message()}"
                    )
                }
            }
        }
    }
    fun addToFavorite(product: FavouriteProduct){
        viewModelScope.launch {
            // var  response = repository.addToFavorite(product).body()!!
            favouriteResponse.postValue(repository.addToFavorite(product).body())

        }

    }

    fun deleteFavProduct(product: FavouriteProduct){
        viewModelScope.launch {
            repository.deleteFavProduct(product?.draftOrder!!.id!!)
        }
    }

   /* fun fetchAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getAllProducts()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    filteredSparePartsMutableLiveData.postValue(response.body()!!.products!!
                        .filter { product -> product.tags == "spare" })
                }else{
                    Log.e(
                        "DisplaySparePartsViewModel",
                        "Error fetching data in DisplaySparePartsViewModel ${response.message()}"
                    )
                }
            }
        }
    }*/
}