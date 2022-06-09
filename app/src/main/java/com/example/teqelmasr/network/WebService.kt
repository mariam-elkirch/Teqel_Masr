package com.example.teqelmasr.network

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

}