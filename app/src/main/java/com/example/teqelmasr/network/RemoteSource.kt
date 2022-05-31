package com.example.teqelmasr.network

import com.example.teqelmasr.model.ProductItem
import retrofit2.Response
import retrofit2.http.Body

interface RemoteSource {
    suspend fun storeProduct(@Body product: ProductItem): Response<ProductItem>
}