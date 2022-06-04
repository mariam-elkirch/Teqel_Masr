package com.example.teqelmasr.network


import retrofit2.Response
import retrofit2.http.Body

import com.example.teqelmasr.model.Product


import com.example.teqelmasr.model.ProductItem
import com.example.teqelmasr.model.ProductPost


interface RemoteSource {
    suspend fun storeProduct(product: ProductPost): Response<ProductItem>
    suspend fun getProductsByCategory(productCategory: Long) : Response<ProductItem>

    suspend fun getMyProducts(): Response<ProductItem>

    suspend fun deleteProduct(product: Product)

    suspend fun updateProduct(product: ProductPost)

    }