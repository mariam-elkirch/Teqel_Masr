package com.example.teqelmasr.displaySparePart.viewModel

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
import java.net.SocketTimeoutException

class DisplaySparePartsViewModel(private val repository: RepositoryInterface) : ViewModel() {
    private val sparePartsMutableLiveData: MutableLiveData<ProductItem> = MutableLiveData()
    val sparePartsLiveData: LiveData<ProductItem> = sparePartsMutableLiveData

    fun fetchSpareParts() {
        Log.i("TAG", "inside fetchSpareParts")
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("TAG", "inside viewModelScope")
            try {
                val response = repository.fetchSpareParts()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        sparePartsMutableLiveData.postValue(response.body())
                    } else {
                        Log.e(
                            "DisplaySparePartsViewModel",
                            "Error fetching data in DisplaySparePartsViewModel ${response.message()}"
                        )
                    }
                }
            } catch (ex: SocketTimeoutException) {
                Log.e("TAG", ex.message.toString())
            }
        }
    }
}