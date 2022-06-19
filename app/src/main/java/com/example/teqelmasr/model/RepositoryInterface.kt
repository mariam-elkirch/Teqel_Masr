package com.example.teqelmasr.model

import FavProducts
import FavouriteProduct
import retrofit2.Response

interface RepositoryInterface {

    suspend fun getMyProducts(): Response<ProductItem>
    suspend fun getAllProducts(): Response<ProductItem>
    suspend fun storeProduct(product: ProductPost): Response<ProductItem>
    suspend fun deleteProduct(product: Product)
    suspend fun getProductsByCategory(productCategory: Long) :  Response<ProductItem>

    suspend fun updateProduct(productPost: ProductPost)

    suspend fun postCustomer(customer: Customer)
    suspend fun getCustomer(): Response<CustomerItem>

    suspend fun updateCustomer(customer: Customer)

    suspend fun getCustomers(): Response<CustomersResponse>

    suspend fun addToFavorite(product: FavouriteProduct) : Response<FavouriteProduct>
    suspend fun deleteFavProduct(id: Long)
    suspend fun getFavProducts(): Response<FavProducts>
    suspend fun getSpecificProduct(id: Long): Response<OneProduct>

}