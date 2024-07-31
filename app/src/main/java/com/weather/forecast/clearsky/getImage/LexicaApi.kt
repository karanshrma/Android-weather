package com.weather.forecast.clearsky.getImage

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LexicaApi {
    @GET("search")
    fun getImages(@Query("q") query: String): Call<ImgApiResponse>
}
