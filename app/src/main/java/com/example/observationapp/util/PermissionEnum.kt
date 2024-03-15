package com.example.observationapp.util

import android.Manifest

enum class PermissionEnum(val permission: String) {
    CAMERA(Manifest.permission.CAMERA),
    WRITE_EXTERNAL_STORAGE(Manifest.permission.WRITE_EXTERNAL_STORAGE);
}