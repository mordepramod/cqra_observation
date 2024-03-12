package com.example.observationapp.observation.observation_history.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.observationapp.dashboard.datalayer.ICardViewClickListener
import com.example.observationapp.databinding.ObservationHistoryItemBinding
import com.example.observationapp.models.ObservationHistory

class ObservationHistoryAdapter :
    RecyclerView.Adapter<ObservationHistoryAdapter.ItemViewHolder>() {
    private lateinit var listener: ICardViewClickListener
    private var list = arrayListOf<ObservationHistory>()

    class ItemViewHolder(
        val itemV: ObservationHistoryItemBinding,
        private val listener: ICardViewClickListener
    ) :
        RecyclerView.ViewHolder(itemV.root) {
        fun bindData(position: Int) {

            itemV.materialCardObservation.setOnClickListener {
                listener.onItemClick(position)
            }


        }

    }

    fun setListener(clickListener: ICardViewClickListener) {
        listener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ObservationHistoryItemBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding, listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindData(position)
        holder.itemV.tvObservationTitleDescription.text = "${(list[position].description).trim()}"
        holder.itemV.tvObservationRemark.text = "${list[position].remark}"
        holder.itemV.tvObservationLocation.text = "${list[position].location}"
        holder.itemV.tvObservationDate.text = "${list[position].observation_date}"
    }

    fun setData(it: List<ObservationHistory>) {
        list.clear()
        list.addAll(it)
        notifyItemRangeChanged(0, list.size)
    }
}