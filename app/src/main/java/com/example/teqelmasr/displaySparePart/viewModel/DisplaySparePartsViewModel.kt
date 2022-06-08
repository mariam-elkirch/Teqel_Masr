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

class DisplaySparePartsViewModel(private val repository: RepositoryInterface) : ViewModel() {
    private val sparePartsMutableLiveData: MutableLiveData<ProductItem> = MutableLiveData()
    val sparePartsLiveData: LiveData<ProductItem> = sparePartsMutableLiveData

    private val CATEGORY = 289563082938


    fun fetchSpareParts() {
        Log.i("TAG", "fetchSpareParts: ViewModel")
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getProductsByCategory(productCategory = CATEGORY)
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
        }
    }
}