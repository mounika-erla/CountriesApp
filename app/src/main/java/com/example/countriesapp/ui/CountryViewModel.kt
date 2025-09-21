package com.example.countriesapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countriesapp.data.model.Country
import com.example.countriesapp.data.repository.CountryRepository
import com.example.countriesapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CountryViewModel(
    private val repository: CountryRepository
) : ViewModel() {

    private val _countries = MutableStateFlow<Resource<List<Country>>>(Resource.Loading())
    val countries: StateFlow<Resource<List<Country>>> = _countries

    init {
        getCountries()
    }

    private fun getCountries() {
        viewModelScope.launch {
            _countries.value = Resource.Loading()
            _countries.value = repository.fetchCountries()
        }
    }
}
