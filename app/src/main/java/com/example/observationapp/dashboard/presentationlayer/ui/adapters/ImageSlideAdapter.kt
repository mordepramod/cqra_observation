package com.example.observationapp.dashboard.presentationlayer.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.observationapp.databinding.ImageSliderItemBinding

class ImageSlideAdapter(private val imageList: ArrayList<Int>) :
    RecyclerView.Adapter<ImageSlideAdapter.ItemViewHolder>() {
    class ItemViewHolder(private val itemV: ImageSliderItemBinding) :
        RecyclerView.ViewHolder(itemV.root) {
        fun bind(position: Int) {
            itemV.imageView.setImageResource(position)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ImageSliderItemBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(imageList[position])

    }
}