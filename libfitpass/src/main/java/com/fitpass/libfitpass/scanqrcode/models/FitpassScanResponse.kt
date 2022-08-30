package com.fitpass.libfitpass.scanqrcode.models

data class FitpassScanResponse(
    val code: Int,
    val message: String,
    val results: Results
)