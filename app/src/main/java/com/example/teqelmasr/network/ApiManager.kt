package com.example.teqelmasr.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiManager {
    private const val baseUrl: String = "\n" +
            "https://9b5b9a0ea57b3a651c30127a88671abb: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@tiqelmasr.myshopify.com/"
        fun getInstance(): Retrofit {
            return Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
}