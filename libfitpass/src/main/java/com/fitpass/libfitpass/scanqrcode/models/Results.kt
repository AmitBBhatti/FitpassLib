package com.fitpass.libfitpass.scanqrcode.models

data class Results(
    val address: String,
    val response_type: String,
    val studio_logo: String,
    val studio_name: String,
    val workout_list: ArrayList<Workout>
)