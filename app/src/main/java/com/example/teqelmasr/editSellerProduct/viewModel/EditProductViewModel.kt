package com.example.teqelmasr.editSellerProduct.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.ProductPost
import com.example.teqelmasr.model.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditProductViewModel(private val repo: RepositoryInterface): ViewModel() {

    fun updateProduct(productPost: ProductPost){
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateProduct(productPost)
        }
    }
}