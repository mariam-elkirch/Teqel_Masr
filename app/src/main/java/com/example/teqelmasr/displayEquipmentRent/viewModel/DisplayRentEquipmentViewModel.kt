package com.example.teqelmasr.displayEquipmentRent.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teqelmasr.model.ProductItem
import com.example.teqelmasr.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DisplayRentEquipmentViewModel(private val repository: RepositoryInterface) : ViewModel() {
    private val rentEquipmentMutableLiveData: MutableLiveData<ProductItem> = MutableLiveData()
    val rentEquipmentLiveData: LiveData<ProductItem> = rentEquipmentMutableLiveData

    private val collectionID = 271217721480
    init {
        fetchRentEquipments()
    }

    private fun fetchRentEquipments() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getProductsByCategory(collectionID)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    rentEquipmentMutableLiveData.postValue(response.body())
                } else {
                    Log.e(
                        "DisplayRentEquipmentViewModel",
                        "Error fetching data in DisplayRentEquipmentViewModel ${response.message()}"
                    )
                }
            }
        }
    }
}