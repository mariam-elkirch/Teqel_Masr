package com.example.teqelmasr.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiManager {
    private const val baseUrl: String = "https://864a80a4ab6809ec5d6a16f6b3cf1b2d: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@teqelmasr.myshopify.com/"
        fun getInstance(): Retrofit {
            return Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
}