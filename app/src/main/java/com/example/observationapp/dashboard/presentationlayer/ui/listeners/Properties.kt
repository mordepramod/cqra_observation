package com.example.observationapp.dashboard.presentationlayer.ui.listeners

import com.example.observationapp.dashboard.presentationlayer.ui.tools.PropertiesBSFragment

interface
Properties {
    fun onColorChanged(colorCode: Int)

    fun onOpacityChanged(opacity: Int)

    fun onBrushSizeChanged(brushSize: Int)
    fun alertDialog()
    fun showBottomSheetDialogFragment(mPropertiesBSFragment: PropertiesBSFragment?)
    fun TextStyleBuilder(): Any
    fun galleryAddPic(imagePath: String)
}