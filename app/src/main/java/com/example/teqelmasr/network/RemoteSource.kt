package com.example.teqelmasr.network


import retrofit2.Response
import retrofit2.http.Body

import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.ProductItem


interface RemoteSource {
    suspend fun storeProduct(@Body product: ProductItem): Response<ProductItem>
    suspend fun getProductsByCategory(productCategory: String) : Response<ProductItem>

    suspend fun getMyProducts(): Response<ProductItem>
}