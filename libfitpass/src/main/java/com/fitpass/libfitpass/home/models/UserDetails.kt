package com.fitpass.libfitpass.home.models

data class UserDetails(
    val fitpass_id: String,
    val user_id: Int,
    val user_name: String,
    val secret_key: String,
    val app_key: String,
)