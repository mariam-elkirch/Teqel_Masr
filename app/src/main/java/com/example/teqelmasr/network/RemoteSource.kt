package com.example.teqelmasr.network


import com.example.teqelmasr.model.*
import retrofit2.Response
import retrofit2.http.Body


interface RemoteSource {
    suspend fun storeProduct(product: ProductPost): Response<ProductItem>
    suspend fun getProductsByCategory(productCategory: Long) : Response<ProductItem>

    suspend fun getMyProducts(): Response<ProductItem>
    suspend fun getAllProducts(): Response<ProductItem>

    suspend fun deleteProduct(product: Product)

    suspend fun updateProduct(product: ProductPost)

    suspend fun postCustomer(customer: Customer)

    suspend fun getCustomers(): Response<CustomersResponse>
    suspend fun getCustomer(): Response<CustomerItem>

}