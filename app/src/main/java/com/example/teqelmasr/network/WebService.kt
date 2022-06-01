package com.example.teqelmasr.network

import com.example.teqelmasr.model.ProductItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface WebService {

    @Headers(
        "Accept: application/json",
        "X-Shopify-Access-Token: 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",
    )
    @GET("admin/products.json")
    suspend fun getProducts(@Query("tags") productType: String): Response<ProductItem>

    @Headers(
        "X-Shopify-Shop-Api-Call-Limit: 40/40",
         "Retry-After: 2.0",
        "X-Shopify-Access-Token: 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"
    )
    @POST("admin/products.json")

    suspend fun storeProduct(@Body product: ProductItem): Response<ProductItem>

    //get seller products
    @Headers(
        "Accept: application/json",
        "X-Shopify-Access-Token: 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",
    )
    @GET("admin/products.json")
    suspend fun getMyProducts(): Response<ProductItem>

}