package com.example.teqelmasr.network


import android.util.Log
import com.example.teqelmasr.model.Customer
import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.ProductItem


import com.example.teqelmasr.model.ProductPost

import retrofit2.Response

class Client : RemoteSource {
    /*override suspend fun getCurrentWeather(units: String, lat: String, lng: String, lang: String): WeatherResponse {
        val weatherService = RetrofitHelper.getInstance().create(WeatherService::class.java)
        return weatherService.getCurrentWeather(units, lat, lng, lang)
    }*/
    private val productsService = ApiManager.getInstance().create(WebService::class.java)

    companion object {
        private var instance: Client? = null
        fun getInstance(): Client {
            return instance ?: Client()
        }
    }

    override suspend fun storeProduct(product: ProductPost): Response<ProductItem> {
        Log.i("Tag",product.toString() + "imgggggg")
        val productsService = ApiManager.getInstance().create(WebService::class.java)
        return productsService.storeProduct(product)
    }

    override suspend fun getProductsByCategory(productCategory: Long) =
        productsService.getProducts(productCategory)

    override suspend fun getMyProducts(): Response<ProductItem> {
        val service = ApiManager.getInstance().create(WebService::class.java)
        return service.getMyProducts()
    }

    override suspend fun getAllProducts(): Response<ProductItem> = productsService.getAllProducts()


    override suspend fun deleteProduct(product: Product) {
        val service = ApiManager.getInstance().create(WebService::class.java)
        service.deleteProduct(product.variants?.get(0)?.product_id!!)
    }

    override suspend fun updateProduct(productPost: ProductPost) {
        val service = ApiManager.getInstance().create(WebService::class.java)
        val res = service.updateProduct(productPost.product?.variants?.get(0)?.product_id!!, productPost)
        Log.i("res", "updateProduct: ${res.raw()}")
    }

    override suspend fun postCustomer(customer: Customer) {
        val service = ApiManager.getInstance().create(WebService::class.java)
        val res = service.postCustomer(customer)

    }

}



