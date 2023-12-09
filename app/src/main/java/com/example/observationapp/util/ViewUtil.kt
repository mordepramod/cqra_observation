package com.example.observationapp.util

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar

var toast: Toast? = null
fun Context.showShortToast(message: String) {
    if (toast != null) {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast?.show()
    } else {
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast?.show()
    }
}

fun Context.showLongToast(message: String) {
    if (toast != null) {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        toast?.show()
    } else {
        toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        toast?.show()
    }
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.inVisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.isVisible(isVisible: Boolean) {
    this.isVisible = isVisible
}

fun View.showShortSnackBar(message: String, action: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    snack.setTextMaxLines(6)
    snack.action()
    snack.show()
}