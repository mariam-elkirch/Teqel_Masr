package com.example.teqelmasr.model

import retrofit2.Response

interface RepositoryInterface {

    suspend fun getMyProducts(): Response<ProductItem>

    suspend fun fetchSpareParts() :  Response<ProductItem>

    suspend fun deleteProduct(product: Product)
}