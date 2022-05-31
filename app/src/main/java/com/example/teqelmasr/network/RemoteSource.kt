package com.example.teqelmasr.network

import com.example.teqelmasr.model.ProductItem
import retrofit2.Response
import retrofit2.http.Body

import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.ProductItem


interface RemoteSource {
    suspend fun storeProduct(@Body product: ProductItem): Response<ProductItem>
    suspend fun fetchSpareParts() : Response<ProductItem>
}