package com.example.teqelmasr.network

import com.example.teqelmasr.model.Product
import com.example.teqelmasr.model.ProductItem
import retrofit2.Response

class Client : RemoteSource {
    /*override suspend fun getCurrentWeather(units: String, lat: String, lng: String, lang: String): WeatherResponse {
        val weatherService = RetrofitHelper.getInstance().create(WeatherService::class.java)
        return weatherService.getCurrentWeather(units, lat, lng, lang)
    }*/
     companion object{
            private var instance: Client? = null
            fun getInstance(): Client{
                return  instance?: Client()
            }
        }

    override suspend fun fetchSpareParts() : Response<ProductItem> {
        val productsService = ApiManager.getInstance().create(WebService::class.java)
        return productsService.getProducts()

    }

}