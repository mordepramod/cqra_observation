package com.example.observationapp.dashboard.presentationlayer.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.observationapp.R
import com.example.observationapp.dashboard.datalayer.ICardViewClickListener
import com.example.observationapp.databinding.CardLayoutBinding
import com.example.observationapp.models.Module

class DashboardCardRecyclerAdapter :
    RecyclerView.Adapter<DashboardCardRecyclerAdapter.ItemViewHolder>() {
    private lateinit var listener: ICardViewClickListener
    private var list = arrayListOf<Module>()
    private lateinit var mContext: Context

    class ItemViewHolder(
        val itemV: CardLayoutBinding,
        private val listener: ICardViewClickListener
    ) :
        RecyclerView.ViewHolder(itemV.root) {
        fun bindData(position: Int) {

            itemV.root.setOnClickListener {
                listener.onItemClick(position)
            }


        }

    }

    fun setListener(clickListener: ICardViewClickListener) {
        listener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardLayoutBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding, listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindData(position)
        holder.itemV.tvCard.text = list[position].module_name
        if (list[position].module_id == "6") {
            holder.itemV.ivCard.setImageDrawable(
                AppCompatResources.getDrawable(
                    mContext,
                    R.drawable.observation_icon
                )
            )
        } else if (list[position].module_id == "2") {
            holder.itemV.ivCard.setImageDrawable(
                AppCompatResources.getDrawable(
                    mContext,
                    R.drawable.icon_project
                )
            )
        } else if (list[position].module_id == "1") {
            holder.itemV.ivCard.setImageDrawable(
                AppCompatResources.getDrawable(
                    mContext,
                    R.drawable.developer_icon
                )
            )
        }
    }

    fun setData(it: List<Module>) {
        list.clear()
        list.addAll(it)
        notifyItemRangeChanged(0, list.size)
    }

    fun setContext(requireContext: Context) {
        mContext = requireContext
    }
}