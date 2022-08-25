package com.fitpass.libfitpass.base.utilities

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

object FitpassConfigUtil {
    fun setStatusBarColor(context:Context,activity: AppCompatActivity,statusBarcolor:String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                val window: Window = activity.window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.setStatusBarColor(Color.parseColor(statusBarcolor))
            } catch (e: Exception) {
                e.message
            }
        }
    }
}