package com.example.teqelmasr.network

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
    @GET("admin/products.json")
    suspend fun getProducts(): Response<ProductItem>

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
        "X-Shopify-Shop-Api-Call-Limit: 40/40",
        "Retry-After: 2.0",
        "X-Shopify-Access-Token: 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"
    )
    @DELETE("admin/products/{productID}.json")
    suspend fun deleteProduct(
        @Path("productID") id: Long
    )

}