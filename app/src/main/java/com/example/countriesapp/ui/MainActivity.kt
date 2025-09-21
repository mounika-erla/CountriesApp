package com.example.countriesapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.countriesapp.CountryAdapter
import com.example.countriesapp.data.network.ApiService
import com.example.countriesapp.data.repository.CountryRepository
import com.example.countriesapp.databinding.ActivityMainBinding
import com.example.countriesapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    private val TAG = "CountriesApp"
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CountryAdapter
    private var viewModel: CountryViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        setupRecyclerView()

        // Setup Retrofit & Repository
        val apiService = Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/") // 👈 Your endpoint base
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        val repository = CountryRepository(apiService)
        val factory = CountryViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[CountryViewModel::class.java]

        // Collect data
        observeCountries()
    }

    private fun setupRecyclerView() {
        adapter = CountryAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
            setHasFixedSize(true)

            // Add divider between items
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    LinearLayoutManager.VERTICAL
                )
            )

            // Padding around list
            clipToPadding = false
            setPadding(16, 16, 16, 16)
        }
    }

    private fun observeCountries() {
        lifecycleScope.launch {
            viewModel?.countries?.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        showLoading(false)
                        val list = resource.data ?: emptyList()
                        adapter.submitList(list)
                        Log.d(TAG, "Loaded ${list.size} countries")
                    }
                    is Resource.Loading -> {
                        showLoading(true)
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        Log.e(TAG, "Error fetching countries: ${resource.message}")
                        Toast.makeText(
                            this@MainActivity,
                            "Failed to load countries. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
