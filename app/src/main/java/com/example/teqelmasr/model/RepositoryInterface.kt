package com.example.teqelmasr.model

import retrofit2.Response

interface RepositoryInterface {

    suspend fun getMyProducts(): Response<ProductItem>
    suspend fun getAllProducts(): Response<ProductItem>
    suspend fun storeProduct(product: ProductPost): Response<ProductItem>
    suspend fun deleteProduct(product: Product)
    suspend fun getProductsByCategory(productCategory: Long) :  Response<ProductItem>

    suspend fun updateProduct(productPost: ProductPost)

    suspend fun postCustomer(customer: Customer)

    suspend fun getCustomers(): Response<CustomersResponse>


}