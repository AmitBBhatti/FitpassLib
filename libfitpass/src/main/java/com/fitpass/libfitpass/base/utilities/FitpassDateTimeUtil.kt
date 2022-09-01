package com.fitpass.libfitpass.base.utilities

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object FitpassDateTimeUtil {
    fun changedateformat(
        date: String?,
        inputformat: String?,
        outputformat: String?
    ): String? {
        val inputFormat: DateFormat = SimpleDateFormat(inputformat)
        val outputFormat: DateFormat = SimpleDateFormat(outputformat)
        var rawDate: Date? = null
        try {
            rawDate = inputFormat.parse(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return outputFormat.format(rawDate)
    }
    fun getCurrentDate(): String? {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = Date()
        return dateFormat.format(date)
    }

    fun getCurrentTime(): String? {
        val dateFormat: DateFormat = SimpleDateFormat("HH.mm")
        val date = Date()
        return dateFormat.format(date)
    }

}