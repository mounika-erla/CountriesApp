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
import com.example.countriesapp.data.network.ApiClient
import com.example.countriesapp.data.repository.CountryRepository
import com.example.countriesapp.databinding.ActivityMainBinding
import com.example.countriesapp.util.Resource
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CountryAdapter
    private var viewModel: CountryViewModel? = null
    private val TAG = "CountriesApp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupViewModel()
        observeCountries()
    }

    private fun setupRecyclerView() {
        adapter = CountryAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL))
            clipToPadding = false
            setPadding(16, 16, 16, 16)
        }
    }

    private fun setupViewModel() {
        val repository = CountryRepository(ApiClient.apiService)
        val factory = CountryViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[CountryViewModel::class.java]
    }

    private fun observeCountries() {
        lifecycleScope.launch {
            viewModel?.countries?.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        showLoading(false)
                        val list = resource.data ?: emptyList()
                        if (list.isEmpty()) {
                            binding.recyclerView.visibility = View.GONE
                            binding.emptyView.visibility = View.VISIBLE
                        } else {
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.emptyView.visibility = View.GONE
                            adapter.submitList(list)
                            Log.d(TAG, "Loaded ${list.size} countries")
                        }
                    }
                    is Resource.Loading -> {
                        showLoading(true)
                        binding.emptyView.visibility = View.GONE
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        binding.recyclerView.visibility = View.GONE
                        binding.emptyView.visibility = View.VISIBLE
                        binding.emptyView.text = "Failed to load countries"
                        Toast.makeText(this@MainActivity, "Failed to load countries.", Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "Error: ${resource.message}")
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
