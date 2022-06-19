package com.example.teqelmasr.network


import com.example.teqelmasr.model.*
import FavProducts
import FavouriteProduct
import com.example.teqelmasr.model.Customer
import retrofit2.Response
import retrofit2.http.Body

import com.example.teqelmasr.model.Product


import com.example.teqelmasr.model.ProductItem
import com.example.teqelmasr.model.ProductPost
import retrofit2.http.Path


interface RemoteSource {
    suspend fun storeProduct(product: ProductPost): Response<ProductItem>
    suspend fun getProductsByCategory(productCategory: Long) : Response<ProductItem>

    suspend fun getMyProducts(): Response<ProductItem>
    suspend fun getAllProducts(): Response<ProductItem>

    suspend fun deleteProduct(product: Product)

    suspend fun updateProduct(product: ProductPost)

    suspend fun postCustomer(customer: Customer)

    suspend fun updateCustomer(customer: Customer)

    suspend fun getCustomers(): Response<CustomersResponse>
    suspend fun getCustomer(): Response<CustomerItem>
    suspend fun addToFavorite(product: FavouriteProduct) : Response<FavouriteProduct>
    suspend fun deleteFavProduct(id: Long)
    suspend fun getFavProducts(): Response<FavProducts>
    suspend fun getSpecificProduct(id: Long): Response<OneProduct>
}