package com.example.countriesapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.countriesapp.data.repository.CountryRepository

class CountryViewModelFactory(
    private val api: CountryRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CountryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CountryViewModel(api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
