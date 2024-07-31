package com.weather.forecast.clearsky.model

import com.weather.forecast.clearsky.model.Current
import com.weather.forecast.clearsky.model.Forecast
import com.weather.forecast.clearsky.model.Location

data class WeatherModel(

    val current: Current,
    val forecast: Forecast,
    val location: Location
)