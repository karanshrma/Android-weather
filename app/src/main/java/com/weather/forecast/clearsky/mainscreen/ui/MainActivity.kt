package com.weather.forecast.clearsky.mainscreen.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.weather.forecast.clearsky.R
import com.weather.forecast.clearsky.getImage.ImageClient
import com.weather.forecast.clearsky.getImage.ImageResult
import com.weather.forecast.clearsky.getImage.ImgApiResponse
import com.weather.forecast.clearsky.mainscreen.viewmodel.MainViewModel
import com.weather.forecast.clearsky.network.ResultData
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var country: EditText
    private lateinit var search: Button
    private lateinit var loc: TextView
    private lateinit var temperature: TextView
    private lateinit var condition: TextView
    private lateinit var conditionImg: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        country = findViewById(R.id.enterCountryET)
        search = findViewById(R.id.getWeatheBTr)
        loc = findViewById(R.id.locationTV)
        temperature = findViewById(R.id.tempTV)
        condition = findViewById(R.id.conditonTv)
        conditionImg = findViewById(R.id.conditonImage)

        // Set up click listener
        search.setOnClickListener {
            viewModel.getWeatherData(country.text.toString()).observe(this, Observer { result ->
                when (result) {
                    is ResultData.Success -> {
                        result.data?.let { data ->
                            Log.d("TAG", "onCreate: $data")
                            loc.text = "Location: ${data.location.name}, ${data.location.country}"
                            temperature.text = "Temp: ${data.current.temp_c}°C"
                            condition.text = "Condition: ${data.current.condition.text}"

                            // Load image based on the condition
                            getImageOnImageView(data.current.condition.text)
                        }
                        Log.i("harry", result.toString())
                    }

                    is ResultData.Failed -> {
                        Log.d("TAG", "onCreate: failed ${result.message}")
                        loc.text = "Failed to load data: ${result.message}"
                    }

                    is ResultData.Loading -> {
                        Log.d("TAG", "onCreate: Loading")
                        loc.text = "Loading..."
                    }
                }
            })
        }
    }

    private fun getImageOnImageView(condition: String) {
        val call: Call<ImgApiResponse> = ImageClient.apiService.getImages(condition)
        call.enqueue(object : Callback<ImgApiResponse> {
            override fun onResponse(
                call: Call<ImgApiResponse>,
                response: Response<ImgApiResponse>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    apiResponse?.images?.firstOrNull()?.let { imageResult ->
                        loadImage(imageResult.src) // Load the first image URL using Glide
                    }
                } else {
                    // Handle the error
                    Log.e("TAG", "Error: ${response.message()}")
                }
            }

            override fun onFailure(
                call: Call<ImgApiResponse>,
                t: Throwable
            ) {
                // Handle the failure
                Log.e("TAG", "Failure: ${t.message}")
            }
        })
    }

    private fun loadImage(src: String) {
        Glide.with(this)
            .load(src)
            .fitCenter()
            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
            .into(conditionImg)
    }
}
