package com.example.teqelmasr.displayEquipmentSell.viewModel

import DraftOrder
import FavouriteProduct
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.ProductItem
import com.example.teqelmasr.model.RepositoryInterface
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DisplayEquipmentSellViewModel (private val repository: RepositoryInterface) : ViewModel() {
    private val sellEquipmentMutableLiveData: MutableLiveData<List<Product>> = MutableLiveData()
    val sellEquipmentLiveData: LiveData<List<Product>> =sellEquipmentMutableLiveData
    val favouriteResponse = MutableLiveData<FavouriteProduct>()
    private val favoriteListMutableLiveData :MutableLiveData<List<DraftOrder>> = MutableLiveData()
    val favListLiveData : LiveData<List<DraftOrder>> = favoriteListMutableLiveData
    private val allFavoriteListMutableLiveData :MutableLiveData<List<DraftOrder>> = MutableLiveData()
    val allFavListLiveData : LiveData<List<DraftOrder>> = allFavoriteListMutableLiveData
    val user = Firebase.auth.currentUser

    //private val collectionID = 271217655944
    init {
        fetchSellEquipments()
    }

    fun fetchSellEquipments() {
        viewModelScope.launch(Dispatchers.IO) {
           // val response = repository.getProductsByCategory(collectionID)
            val response = repository.getAllProducts()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    sellEquipmentMutableLiveData.postValue(response.body()!!.products!!
                        .filter { product -> product.tags == "equimentsell" })
                   // sellEquipmentMutableLiveData.postValue(response.body())
                } else {
                    Log.e(
                        "DisplaySellEquipmentViewModel",
                        "Error fetching data in DisplaySellEquipmentViewModel ${response.message()}"
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
    fun getFavProduct(productID : Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getFavProducts()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {


                    favoriteListMutableLiveData.postValue(response.body()?.draftOrders?.filter { fav -> fav.lineItems[0].productID == productID &&
                            fav.email == user?.email
                    })
                }else {
                    Log.e(
                        "DisplayRentEquipmentViewModel",
                        "Error fetching data in DisplayRentEquipmentViewModel ${response.message()}"
                    )
                }
            }
        }
    }
    fun getFavProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getFavProducts()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    allFavoriteListMutableLiveData.postValue(response.body()?.draftOrders?.filter { fav -> fav.email == user?.email })
                }else {
                    Log.e(
                        "DisplayRentEquipmentViewModel",
                        "Error fetching data in DisplayRentEquipmentViewModel ${response.message()}"
                    )
                }
            }
        }
    }
}