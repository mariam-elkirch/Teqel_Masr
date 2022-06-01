package com.example.teqelmasr.model

import retrofit2.Response

interface RepositoryInterface {

    suspend fun getMyProducts(): Response<ProductItem>


    suspend fun getProductsByCategory(productCategory: String) :  Response<ProductItem>
}