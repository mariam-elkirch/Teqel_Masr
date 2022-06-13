package com.example.teqelmasr.displayEquipmentRent.viewModel

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

class DisplayRentEquipmentViewModel(private val repository: RepositoryInterface) : ViewModel() {
    private val rentEquipmentMutableLiveData: MutableLiveData<List<Product>> = MutableLiveData()
    val rentEquipmentLiveData: LiveData<List<Product>> = rentEquipmentMutableLiveData

    init {
        fetchRentEquipments()
    }

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
}