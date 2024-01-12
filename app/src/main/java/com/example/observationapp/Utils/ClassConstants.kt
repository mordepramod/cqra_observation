package com.example.observationapp.Utils

class ClassConstants {


    object imageCount {
        operator fun inc(): ClassConstants.imageCount {

            return TODO("Provide the return value")
        }

        var imageCount = 0

    }

    object totalImageCount {
        val totalImageCount = 2

    }



   

    companion object {
        val IMAGE_EDIT = 102
        val PERMISSION_REQUEST_CODE=105
        val CAMERA_REQUEST = 101
        val GALLERY_REQUEST = 100

    }
}