package com.weather.forecast.clearsky.getImage

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ImageClient {
    private const val BASE_URL = "https://lexica.art/api/v1/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: LexicaApi = retrofit.create(LexicaApi::class.java)
}
