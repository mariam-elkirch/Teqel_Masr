package com.example.teqelmasr.model

import retrofit2.Response

interface RepositoryInterface {

    suspend fun fetchSpareParts() :  Response<ProductItem>
}