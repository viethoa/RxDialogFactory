package com.viethoa.dialog.extension

import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.viethoa.dialog.R
import com.viethoa.dialog.utils.isEqualOrMoreThanLollipop
import com.viethoa.dialog.utils.isEqualOrMoreThanMarshmallow

/**
 * status bar is transparent and the text is dark
 */
fun Window.lightStatusBar(@ColorRes statusBarColorId: Int? = null) {
    if (isEqualOrMoreThanMarshmallow()) {
        val winParams = attributes
        winParams.flags =
            winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        attributes = winParams
        decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        statusBarColorId?.let { statusBarColor = ContextCompat.getColor(context, it) }
    }
}

fun Window.lightStatusBarWithoutFullscreen(@ColorRes statusBarColorId: Int? = null) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val winParams = attributes
        winParams.flags =
            winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        attributes = winParams
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        statusBarColorId?.let { statusBarColor = ContextCompat.getColor(context, it) }
    }
}

fun Window.darkStatusBarWithColor(@ColorRes statusBarColorId: Int) {
    if (isEqualOrMoreThanLollipop()) {
        val winParams = attributes
        winParams.flags =
            winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        attributes = winParams
        decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        statusBarColor = ContextCompat.getColor(context, statusBarColorId)
    }
}

fun Window.fullScreen(statusBarColorId: Int) {
    if (isEqualOrMoreThanMarshmallow()) {
        lightStatusBar()
    } else {
        darkStatusBarWithColor(statusBarColorId)
    }
}

fun Window.hideStatusBar() {
    if (isEqualOrMoreThanLollipop()) {
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        statusBarColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.getColor(context, android.R.color.transparent)
        } else {
            ContextCompat.getColor(context, R.color.black_600)
        }
    }
    when {
        isEqualOrMoreThanMarshmallow() -> decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        isEqualOrMoreThanLollipop() -> decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
}

fun Window.dimBehind() {
    addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    setDimAmount(0.5f)
}

fun Window.changeStatusBarColor(@ColorRes statusBarColorId: Int) {
    if (isEqualOrMoreThanLollipop()) {
        statusBarColor = ContextCompat.getColor(context, statusBarColorId)
    }
}