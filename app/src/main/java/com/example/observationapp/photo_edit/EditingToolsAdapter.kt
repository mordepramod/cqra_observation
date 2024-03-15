package com.example.observationapp.photo_edit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.observationapp.R

class EditingToolsAdapter(private val mOnItemSelected: OnItemSelected) :
    RecyclerView.Adapter<EditingToolsAdapter.ViewHolder>() {
    private val mToolList: MutableList<ToolModel> = ArrayList()

    interface OnItemSelected {
        fun onToolSelected(toolType: ToolType)
    }

    internal inner class ToolModel(
        val mToolName: String,
        val mToolIcon: Int,
        val mToolType: ToolType
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_editing_tools, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mToolList[position]
        holder.txtTool.text = item.mToolName
        holder.imgToolIcon.setImageResource(item.mToolIcon)
    }

    override fun getItemCount(): Int {
        return mToolList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgToolIcon: ImageView = itemView.findViewById(R.id.imgToolIcon)
        val txtTool: TextView = itemView.findViewById(R.id.txtTool)

        init {
            itemView.setOnClickListener { _: View? ->
                mOnItemSelected.onToolSelected(
                    mToolList[layoutPosition].mToolType
                )
            }
        }
    }

    fun setList(context: Context) {
        mToolList.add(
            ToolModel(
                context.getString(R.string.shape),
                R.drawable.ic_oval,
                ToolType.SHAPE
            )
        )
        mToolList.add(
            ToolModel(
                context.getString(R.string.label_text),
                R.drawable.ic_text,
                ToolType.TEXT
            )
        )
        mToolList.add(
            ToolModel(
                context.getString(R.string.eraser),
                R.drawable.ic_eraser,
                ToolType.ERASER
            )
        )
        mToolList.add(
            ToolModel(
                context.getString(R.string.rotate),
                R.drawable.ic_redo,
                ToolType.ROTATE
            )
        )
        notifyItemRangeInserted(0, mToolList.size)
    }

    init {

    }
}