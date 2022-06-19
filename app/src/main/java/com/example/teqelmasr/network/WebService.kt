package com.example.teqelmasr.network

import com.example.teqelmasr.model.*
import FavProducts
import FavouriteProduct
import com.example.teqelmasr.model.Customer
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.ProductItem
import com.example.teqelmasr.model.ProductPost
import retrofit2.Response
import retrofit2.http.*

interface WebService {

    @Headers(
        "Accept: application/json",
        "X-Shopify-Access-Token: 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",
    )
    @GET("admin/collections/{CollectionID}/products.json")
    suspend fun getProducts(@Path("CollectionID") id: Long): Response<ProductItem>

    @Headers(
        "X-Shopify-Shop-Api-Call-Limit: 40/40",
         "Retry-After: 2.0",
        "X-Shopify-Access-Token: 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"
    )
    @POST("admin/products.json")

    suspend fun storeProduct(@Body product: ProductPost): Response<ProductItem>

    //get seller products
    @Headers(
        "Accept: application/json",
        "X-Shopify-Access-Token: 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",
    )
    @GET("admin/products.json")
    suspend fun getMyProducts(): Response<ProductItem>

    @Headers(
        "Accept: application/json",
        "X-Shopify-Access-Token: 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",
    )
    @GET("admin/products.json")
    suspend fun getAllProducts(): Response<ProductItem>


    //delete seller product
    @Headers(
        "X-Shopify-Shop-Api-Call-Limit: 40/40",
        "Retry-After: 2.0",
        "X-Shopify-Access-Token: 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"
    )
    @DELETE("admin/products/{productID}.json")
    suspend fun deleteProduct(@Path("productID") id: Long)


    //Update seller product
    @Headers(
        "X-Shopify-Shop-Api-Call-Limit: 40/40",
        "Retry-After: 2.0",
        "X-Shopify-Access-Token: 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"
    )
    @PUT("admin/products/{productID}.json")
    suspend fun updateProduct(@Path("productID") id: Long, @Body product: ProductPost): Response<ProductItem>


    @Headers(
        "X-Shopify-Shop-Api-Call-Limit: 40/40",
        "Retry-After: 2.0",
        "X-Shopify-Access-Token: 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"
    )
    @POST("admin/customers.json")

    suspend fun postCustomer(@Body customer: Customer): Response<Customer>

    @Headers(
        "X-Shopify-Shop-Api-Call-Limit: 40/40",
        "Retry-After: 2.0",
        "X-Shopify-Access-Token: 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"
    )
    @PUT("admin/customers/{customer_id}.json")
    suspend fun updateCustomer(@Path("customer_id") id: Long, @Body customer: Customer): Response<Customer>

    //get customers
    @Headers(
        "X-Shopify-Shop-Api-Call-Limit: 40/40",
        "Retry-After: 2.0",
        "X-Shopify-Access-Token: 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"
    )
    @GET("admin/customers.json")
    suspend fun getCustomers(): Response<CustomersResponse>
    @Headers(
        "X-Shopify-Shop-Api-Call-Limit: 40/40",
        "Retry-After: 2.0",
        "X-Shopify-Access-Token: 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"
    )
    @GET("admin/customers.json")

    suspend fun GetCustomer(): Response<CustomerItem>

    @Headers(
        "X-Shopify-Shop-Api-Call-Limit: 40/40",
        "Retry-After: 2.0",
        "X-Shopify-Access-Token: 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"
    )
    @POST("admin/draft_orders.json")

    suspend fun addToFavorite(@Body product: FavouriteProduct): Response<FavouriteProduct>

    @Headers(
        "X-Shopify-Shop-Api-Call-Limit: 40/40",
        "Retry-After: 2.0",
        "X-Shopify-Access-Token: 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"
    )
    @DELETE("admin/draft_orders/{productID}.json")
    suspend fun deleteFavProduct(@Path("productID") id: Long): Response<FavouriteProduct>

    @Headers(
        "Accept: application/json",
        "X-Shopify-Access-Token: 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",
    )
    @GET("admin/draft_orders.json")
    suspend fun getFavProducts(): Response<FavProducts>

    @Headers(
        "X-Shopify-Shop-Api-Call-Limit: 40/40",
        "Retry-After: 2.0",
        "X-Shopify-Access-Token: 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"
    )
    @GET("admin/products/{ProductID}.json")
    suspend fun getSpecificProduct(@Path("ProductID") id: Long): Response<OneProduct>
}

