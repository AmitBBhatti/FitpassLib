package com.fitpass.libfitpass.scanqrcode.listeners

import com.fitpass.libfitpass.scanqrcode.models.Workout

interface FitpassScanListener {
    fun onScanClick(workout: Workout,studioName:String,logo:String,address:String,position:Int)
}