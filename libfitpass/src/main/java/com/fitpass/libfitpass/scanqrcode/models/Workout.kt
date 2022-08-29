package com.fitpass.libfitpass.scanqrcode.models

data class Workout(
    val activity_id: Int,
    val end_time: Int,
    val start_time: Int,
    val urc_updated_time: Int,
    val user_schedule_id: String,
    val workout_name: String,
    val workout_status: Int,
    val security_code: Int,
)