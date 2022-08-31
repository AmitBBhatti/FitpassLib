package com.fitpass.libfitpass.home.listeners

import com.fitpass.libfitpass.home.models.Product

interface FitpassHomeListener {
    fun onScanClick()
    fun onMenuClick(data: Product)
}