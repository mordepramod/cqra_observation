package com.example.observationapp.dashboard.presentationlayer.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.observationapp.databinding.CardLayoutBinding

class DashboardCardRecyclerAdapter :
    RecyclerView.Adapter<DashboardCardRecyclerAdapter.ItemViewHolder>() {
    class ItemViewHolder(itemV: View) : RecyclerView.ViewHolder(itemV) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardLayoutBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

    }
}