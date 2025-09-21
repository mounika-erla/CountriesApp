package com.example.countriesapp.data.repository

import com.example.countriesapp.data.model.Country
import com.example.countriesapp.data.network.ApiService
import com.example.countriesapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CountryRepository(private val api: ApiService) {

    suspend fun fetchCountries(): Resource<List<Country>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getCountries()
                if (response.isSuccessful) {
                    val countries = response.body()?.filterNotNull() ?: emptyList()
                    Resource.Success(countries)
                } else {
                    Resource.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}
