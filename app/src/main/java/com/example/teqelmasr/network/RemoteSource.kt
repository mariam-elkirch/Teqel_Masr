package com.example.teqelmasr.network

import com.example.teqelmasr.model.ProductItem
import retrofit2.Response

interface RemoteSource {

    suspend fun getMyProducts(): Response<ProductItem>
}