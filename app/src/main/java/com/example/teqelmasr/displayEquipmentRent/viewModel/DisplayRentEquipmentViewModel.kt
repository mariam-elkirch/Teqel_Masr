package com.example.teqelmasr.displayEquipmentRent.viewModel

import DraftOrder
import FavouriteProduct
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teqelmasr.helper.NetworkCheck
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.ProductItem
import com.example.teqelmasr.model.RepositoryInterface
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class DisplayRentEquipmentViewModel(private val repository: RepositoryInterface) : ViewModel() {
    private val rentEquipmentMutableLiveData: MutableLiveData<List<Product>> = MutableLiveData()
     val favouriteResponse = MutableLiveData<FavouriteProduct>()
    private val favoriteListMutableLiveData :MutableLiveData<List<DraftOrder>> = MutableLiveData()
    val rentEquipmentLiveData: LiveData<List<Product>> = rentEquipmentMutableLiveData
    val favListLiveData : LiveData<List<DraftOrder>> = favoriteListMutableLiveData
    private val allFavoriteListMutableLiveData :MutableLiveData<List<DraftOrder>> = MutableLiveData()
    val allFavListLiveData : LiveData<List<DraftOrder>> = allFavoriteListMutableLiveData

    val user = Firebase.auth.currentUser

    // val response : FavouriteProduct
/*    init {
        fetchRentEquipments()
    }*/

     fun fetchRentEquipments() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getAllProducts()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    rentEquipmentMutableLiveData.postValue(response.body()?.products?.filter { product -> product.tags == "equimentrent"})
                }else {
                    Log.e(
                        "DisplayRentEquipmentViewModel",
                        "Error fetching data in DisplayRentEquipmentViewModel ${response.message()}"
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