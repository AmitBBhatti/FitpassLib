package com.fitpass.libfitpass.home.listeners

import com.fitpass.libfitpass.home.models.Product
import com.fitpass.libfitpass.home.models.SliderActivity

interface FitpassHomeListener {
    fun onScanClick(item: SliderActivity)
    fun onDirectionClick(item: SliderActivity)
    fun onMenuClick(data: Product)
}