package com.example.teqelmasr.network


import FavProducts
import FavouriteProduct
import android.util.Log
import com.example.teqelmasr.model.*


import com.example.teqelmasr.model.ProductPost

import retrofit2.Response
import retrofit2.create

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

    override suspend fun getCustomer(): Response<CustomerItem> = productsService.GetCustomer()

    override suspend fun getCustomers(): Response<CustomersResponse> {
        val service = ApiManager.getInstance().create(WebService::class.java)
        return service.getCustomers()
    }

    override suspend fun addToFavorite(product: FavouriteProduct) : Response<FavouriteProduct> {
        val service = ApiManager.getInstance().create(WebService::class.java)
        val res = service.addToFavorite(product)
        Log.i("TAG", "addToFavorite: ${res.code()}")
        return res
    }
    override suspend fun deleteFavProduct(product: FavouriteProduct){
        val service = ApiManager.getInstance().create(WebService::class.java)
        val res = service.deleteFavProduct(product!!.draftOrder!!.id!!)
        Log.i("TAG", "deleteFavProduct: ${res.code()}")
    }

    override suspend fun getFavProducts(): Response<FavProducts> {
        val service = ApiManager.getInstance().create(WebService::class.java)
        return  service.getFavProducts()
    }

}



