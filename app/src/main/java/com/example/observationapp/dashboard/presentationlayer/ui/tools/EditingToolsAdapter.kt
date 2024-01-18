package com.example.observationapp.dashboard.presentationlayer.ui.tools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.observationapp.R
import com.example.observationapp.dashboard.presentationlayer.ui.listeners.OnItemSelected
import com.example.observationapp.databinding.RowEditingToolsBinding

class EditingToolsAdapter() : RecyclerView.Adapter<EditingToolsAdapter.ViewHolder>() {

    private val mToolList = mutableListOf<ToolModel>()
    private lateinit var mOnItemSelected: OnItemSelected

    init {
        mToolList.addAll(
            listOf(
                ToolModel("Save", R.drawable.ic_save, ToolType.SAVE),
                ToolModel("Brush", R.drawable.ic_brush, ToolType.BRUSH),
                ToolModel("Text", R.drawable.ic_text, ToolType.TEXT),
                ToolModel("Eraser", R.drawable.ic_eraser, ToolType.ERASER),
                ToolModel("Undo", R.drawable.ic_undo, ToolType.UNDO),
                ToolModel("Redo", R.drawable.ic_redo, ToolType.REDO)
            )
        )
    }

    fun setInterface(onItemSelected: OnItemSelected?) {
        if (onItemSelected != null) {
            mOnItemSelected = onItemSelected
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binind =
            RowEditingToolsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binind)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mToolList[position]
        holder.bind(item)
        holder.binding.root.setOnClickListener {
            mOnItemSelected.onToolSelected(mToolList[position].mToolType)
        }
    }

    override fun getItemCount(): Int = mToolList.size
    inner class ToolModel(
        val mToolName: String,
        val mToolIcon: Int,
        val mToolType: ToolType
    )

    class ViewHolder(val binding: RowEditingToolsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ToolModel) {
            binding.txtTool.textSize =
                if (item.mToolName == "Save") 20f else 12f // Concise textSize assignment
            binding.txtTool.text = item.mToolName
            binding.imgToolIcon.setImageResource(item.mToolIcon)
        }
    }
}

