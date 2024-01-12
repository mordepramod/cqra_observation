package com.example.observationapp.dashboard.presentationlayer.ui.listeners

import com.example.observationapp.dashboard.presentationlayer.ui.tools.ToolType

interface OnItemSelected {
    fun onToolSelected(toolType: ToolType)
}