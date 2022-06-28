package com.example.teqelmasr.favourite.viewModel

import DraftOrder
import FavouriteProduct
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teqelmasr.model.OneProduct
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.RepositoryInterface
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddToFavoriteViewModel (private val repository: RepositoryInterface) : ViewModel() {
   // val errorMessage = MutableLiveData<String>()
   // private var _favProduct : MutableLiveData<FavouriteProduct> = MutableLiveData()
   // var favProduct : LiveData<FavouriteProduct> = _favProduct
   val user = Firebase.auth.currentUser
    private val favoriteListMutableLiveData :MutableLiveData<List<DraftOrder>> = MutableLiveData()
    val favListLiveData : LiveData<List<DraftOrder>> = favoriteListMutableLiveData
    private val productDetailsMutableLiveData :MutableLiveData<OneProduct> = MutableLiveData()
    val productDetailsLiveData : LiveData<OneProduct> = productDetailsMutableLiveData
    val favouriteResponse = MutableLiveData<FavouriteProduct>()

    init {
        getFavProduct()
    }

    fun getFavProduct() {
       viewModelScope.launch(Dispatchers.IO) {
           val response = repository.getFavProducts()
           withContext(Dispatchers.Main) {
               if (response.isSuccessful) {
                   favoriteListMutableLiveData.postValue(response.body()?.draftOrders?.filter { favProduct -> favProduct.email.equals(user?.email) })
               }else {
                   Log.e(
                       "DisplayRentEquipmentViewModel",
                       "Error fetching data in DisplayRentEquipmentViewModel ${response.message()}"
                   )
               }
           }
       }
   }
    fun getProductDetails(id : Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getSpecificProduct(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    productDetailsMutableLiveData.postValue(response.body())
                    Log.i("TAG", "getProductDetails: ${response.body()}")

                }else {
                    Log.e(
                        "DisplayRentEquipmentViewModel",
                        "Error fetching data in DisplayRentEquipmentViewModel ${response.message()}"
                    )
                }
            }
        }
    }
    fun deleteFavProduct(id: Long){
        viewModelScope.launch {
            repository.deleteFavProduct(id)
        }
    }
    fun addToFavorite(product: FavouriteProduct){
        viewModelScope.launch {
            // var  response = repository.addToFavorite(product).body()!!
            favouriteResponse.postValue(repository.addToFavorite(product).body())
        }

    }
}