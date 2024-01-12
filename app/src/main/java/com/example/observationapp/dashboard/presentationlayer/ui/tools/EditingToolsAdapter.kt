package com.example.observationapp.dashboard.presentationlayer.ui.tools

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.observationapp.R
import com.example.observationapp.dashboard.presentationlayer.ui.listeners.OnItemSelected

class EditingToolsAdapter() : RecyclerView.Adapter<EditingToolsAdapter.ViewHolder>() {

    private val mToolList = mutableListOf<ToolModel>()
    private lateinit var mOnItemSelected: OnItemSelected


    constructor(onItemSelected: OnItemSelected?) : this() {
        if (onItemSelected != null) {
            mOnItemSelected = onItemSelected
        }
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditingToolsAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_editing_tools, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: EditingToolsAdapter.ViewHolder, position: Int) {
        val item = mToolList[position]
        holder.txtTool?.setTextSize(if (item.mToolName == "Save") 20f else 12f) // Concise textSize assignment
        holder.txtTool?.text = item.mToolName
        holder.imgToolIcon?.setImageResource(item.mToolIcon)
    }

    override fun getItemCount(): Int = mToolList.size
    inner class ToolModel(
        val mToolName: String,
        val mToolIcon: Int,
        private val mToolType: ToolType
    )

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val colorPickerView: Any
            get() {
                TODO()
            }
        val imgToolIcon = itemView?.findViewById<ImageView>(R.id.imgToolIcon)
        val txtTool = itemView?.findViewById<TextView>(R.id.txtTool)

        init {
            itemView?.setOnClickListener {
                mOnItemSelected.onToolSelected(mToolList.get(layoutPosition).mToolType)
            }
        }
    }
}

