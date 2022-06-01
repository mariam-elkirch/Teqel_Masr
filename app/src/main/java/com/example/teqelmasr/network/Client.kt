package com.example.teqelmasr.network


import com.example.teqelmasr.model.ProductItem
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

    override suspend fun getProductsByCategory(productCategory: String) =
        productsService.getProducts(productCategory)


    override suspend fun getMyProducts(): Response<ProductItem> {
        val service = ApiManager.getInstance().create(WebService::class.java)
        return service.getMyProducts()
    }

    override suspend fun storeProduct(product: ProductItem): Response<ProductItem> {
        return productsService.storeProduct(product)
    }

}