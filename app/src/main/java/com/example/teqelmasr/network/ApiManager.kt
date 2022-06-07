package com.example.teqelmasr.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiManager {
    private const val baseUrl: String = "https://55438284cbe2466283e0b7644e7acb3d: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@tiqil-masr.myshopify.com/"
        fun getInstance(): Retrofit {
            return Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
}