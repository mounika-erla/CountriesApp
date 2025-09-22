package com.example.countriesapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.countriesapp.R
import com.example.countriesapp.data.model.Country

class CountryAdapter : ListAdapter<Country, CountryAdapter.CountryViewHolder>(CountryDiffCallback()) {

    class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameRegion: TextView = itemView.findViewById(R.id.countryNameRegion)
        val capital: TextView = itemView.findViewById(R.id.countryCapital)
        val code: TextView = itemView.findViewById(R.id.countryCode)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_country, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = getItem(position)
        holder.nameRegion.text = "${country.name ?: "N/A"}, ${country.region ?: "N/A"}"
        holder.code.text = country.code ?: "N/A"
        holder.capital.text = country.capital ?: "N/A"
        holder.itemView.contentDescription =
            "${country.name ?: "N/A"}, capital: ${country.capital ?: "N/A"}, region: ${country.region ?: "N/A"}, code: ${country.code ?: "N/A"}"
    }
}

class CountryDiffCallback : DiffUtil.ItemCallback<Country>() {
    override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean = oldItem.code == newItem.code
    override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean = oldItem == newItem
}
