package com.fitpass.libfitpass.home.models

import com.fitpass.libfitpass.home.models.OtherInfo

data class Data(
    val description: String,
    val heading: String,
    val height: String,
    val img: String,
    val open_in_app: Boolean,
    val url: String,
    val user_age: String,
    val waist: String,
    val weight: String,
    val activity_id: String,
    val address: String,
    val date_of_workout: String,
    val display_date_time: String,
    val display_endtime: Boolean,
    val end_time: Int,
    val latitude: String,
    val longitude: String,
    val ongoing_workout: String,
    val other_info: OtherInfo,
    val qr_code_number: String,
    val security_code: String,
    val start_time: Int,
    val studio_id: String,
    val studio_logo: String,
    val studio_name: String,
    val urc_updated_time: Int,
    val user_schedule_id: String,
    val workout_id: String,
    val workout_name: String,
    val workout_status: String,
    val workout_time: String,
    val workout_time_id: String,
    val show_header: Boolean
)