package com.example.observationapp.dashboard.presentationlayer.ui.tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.observationapp.R
import com.example.observationapp.dashboard.presentationlayer.ui.activity.PhotoEditorActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Properties

class PropertiesBSFragment : BottomSheetDialogFragment(), SeekBar.OnSeekBarChangeListener {


    private var mProperties: Properties? = null

    fun PropertiesBSFragment() {
        // Required empty public constructor
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_bottom_properties_dialog, container, false)
        }

        fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val rvColor = view.findViewById<RecyclerView>(R.id.rvColors)
            val sbOpacity = view.findViewById<SeekBar>(R.id.sbOpacity)
            val sbBrushSize = view.findViewById<SeekBar>(R.id.sbSize)
            sbOpacity.setOnSeekBarChangeListener(this)
            sbBrushSize.setOnSeekBarChangeListener(this)
            val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            rvColor.layoutManager = layoutManager
            rvColor.setHasFixedSize(true)
            val colorPickerAdapter = activity?.let { ColorPickerAdapter(it) }
            colorPickerAdapter?.setOnColorPickerClickListener(object : OnColorPickerClickListener {
                fun onColorPickerClickListener(colorCode: Int) {
                    if (mProperties != null) {
                        dismiss()
                        mProperties!!.onColorChanged(colorCode)
                    }
                }

                override fun onColorPickerClick(colorCode: Int) {
                    TODO("Not yet implemented")
                }
            })
            rvColor.adapter = colorPickerAdapter
        }

        fun setPropertiesChangeListener(properties: Properties) {
            mProperties = properties
        }

    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar?.id) {
            R.id.sbOpacity -> mProperties?.onOpacityChanged(progress)
            R.id.sbSize -> mProperties?.onBrushSizeChanged(progress)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        TODO("Not yet implemented")
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        TODO("Not yet implemented")
    }

    fun setPropertiesChangeListener(photoEditorActivity: PhotoEditorActivity) {

    }
}

private fun Properties?.onBrushSizeChanged(progress: Int) {

}

private fun Properties.onColorChanged(colorCode: Int) {

}

private fun Properties?.onOpacityChanged(progress: Int) {
    TODO("Not yet implemented")
}
