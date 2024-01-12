package com.example.observationapp.dashboard.presentationlayer.ui.tools

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.observationapp.R
import com.example.observationapp.databinding.ColorPickerItemListBinding

class ColorPickerAdapter(context: Context) : RecyclerView.Adapter<ColorPickerAdapter.ViewHolder>() {


    private var context: Context? = null
    private var inflater: LayoutInflater? = null
    private var colorPickerColors: List<Int> = emptyList()
    private var onColorPickerClickListener: OnColorPickerClickListener? = null

    //    constructor(context: Context) : super() {
//        this.context = context
//        this.inflater = LayoutInflater.from(context)
//        if (colorPickerColors != null) {
//            this.colorPickerColors = colorPickerColors
//        }
//    }
    fun getDefaultColors(context: Context?): List<Int>? {
        val colorPickerColors = ArrayList<Int>()
        colorPickerColors.add(ContextCompat.getColor(context!!, R.color.blue_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.brown_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.green_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.orange_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.red_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.md_theme_onBackground))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.red_orange_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.sky_blue_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.violet_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.md_theme_onError))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.yellow_color_picker))
        colorPickerColors.add(ContextCompat.getColor(context, R.color.yellow_green_color_picker))
        return colorPickerColors
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ColorPickerAdapter.ViewHolder {
        //val view = inflater?.inflate(R.layout.color_picker_item_list, parent, false)
        val binding =
            ColorPickerItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ColorPickerAdapter.ViewHolder(binding)
    }

    class ViewHolder(colorPickerLayout: ColorPickerItemListBinding) :
        RecyclerView.ViewHolder(colorPickerLayout.root) {
        //val colorPickerView: View = itemView.findViewById(R.id.color_picker_view)

        init {
            colorPickerLayout.root.setOnClickListener {
                val onColorPickerClickListener = null
                val colorPickerColors = null
                onColorPickerClickListener.onColorPickerClick(colorPickerColors?.get(adapterPosition))
            }
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        colorPickerColors.get(position)?.let { holder.setBackgroundColor(it) }
    }

    override fun getItemCount(): Int = colorPickerColors.size
    fun setOnColorPickerClickListener(onColorPickerClickListener: OnColorPickerClickListener) {

    }

}

private fun Any.setBackgroundColor(get: Int) {
    TODO("Not yet implemented")
}

private fun buildColorPickerView(view: View, colorCode: Int) {
    view.visibility = View.VISIBLE
    val biggerCircle = ShapeDrawable(OvalShape())
    biggerCircle.intrinsicHeight = 20
    biggerCircle.intrinsicWidth = 20
    biggerCircle.bounds = Rect(0, 0, 20, 20)
    biggerCircle.paint.color = colorCode
    val smallerCircle = ShapeDrawable(OvalShape())
    smallerCircle.intrinsicHeight = 5
    smallerCircle.intrinsicWidth = 5
    smallerCircle.bounds = Rect(0, 0, 5, 5)
    smallerCircle.paint.color = Color.WHITE
    smallerCircle.setPadding(10, 10, 10, 10)
    val drawables = arrayOf<Drawable>(smallerCircle, biggerCircle)
    val layerDrawable = LayerDrawable(drawables)
    view.setBackgroundDrawable(layerDrawable)
}

fun setOnColorPickerClickListener(onColorPickerClickListener: OnColorPickerClickListener?) {
    val onColorPickerClickListener = onColorPickerClickListener
}

interface OnColorPickerClickListener {
    fun onColorPickerClick(colorCode: Int)
}


private fun Nothing?.onColorPickerClick(any: Any?) {
    TODO("Not yet implemented")
}
