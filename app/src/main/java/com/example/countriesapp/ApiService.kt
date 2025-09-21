package com.example.countriesapp

import com.example.countriesapp.data.model.Country
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("all")
    fun getCountries(): Call<List<Country>>
}
