package com.fitpass.libfitpass.base.utilities

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.widget.TextView
import com.fitpass.libfitpass.fontcomponent.FontAwesome
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


object Util {
    fun setimage(textView: FontAwesome, image: Int) {
        textView?.text = image.toChar().toString()
    }

    fun drawCircle(backgroundColor: String): GradientDrawable? {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.OVAL
        shape.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
        shape.setColor(Color.parseColor(backgroundColor))
        return shape
    }

    fun convertMiliesToDD_MM_HH_MMDateTime2(
        milies: String,
        isMiliConversionRequired: Boolean
    ): String? {
        var formatter: DateFormat? = null
        formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat("dd MMM, yyyy hh:mm aa")
        } else {
            SimpleDateFormat("dd MMM, yyyy  hh:mm aa")
        }
        val milliSeconds = milies.toLong()
        println(milliSeconds)
        val calendar = Calendar.getInstance()
        if (isMiliConversionRequired) {
            calendar.timeInMillis = milliSeconds * 1000
        } else {
            calendar.timeInMillis = milliSeconds
        }
        val endDate = formatter.format(calendar.time)
        println(endDate)
        val amToAm = endDate.replace("am", "AM")
        return amToAm.replace("pm", "PM")
    }
    fun captalizeString(str: String):String{
        val output = str.substring(0, 1).uppercase(Locale.getDefault()) + str.substring(1)
       return output
    }
    fun concatString(str: String,cancatvalue:String):String{
        return cancatvalue+str
    }
    fun setFantIcon(textView: FontAwesome, image: Int) {
        textView.text = image.toChar().toString()
    }
}