package com.example.teqelmasr.displayEquipmentSell.viewModel

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

class DisplayEquipmentSellViewModel (private val repository: RepositoryInterface) : ViewModel() {
    private val sellEquipmentMutableLiveData: MutableLiveData<ProductItem> = MutableLiveData()
    val sellEquipmentLiveData: LiveData<ProductItem> =sellEquipmentMutableLiveData

    private val collectionID = 289563181242
    init {
        fetchSellEquipments()
    }

    private fun fetchSellEquipments() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getProductsByCategory(collectionID)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    sellEquipmentMutableLiveData.postValue(response.body())
                } else {
                    Log.e(
                        "DisplaySellEquipmentViewModel",
                        "Error fetching data in DisplaySellEquipmentViewModel ${response.message()}"
                    )
                }
            }
        }
    }
}