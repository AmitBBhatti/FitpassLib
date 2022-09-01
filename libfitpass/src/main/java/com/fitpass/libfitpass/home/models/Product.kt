package com.fitpass.libfitpass.home.models

data class Product(
    val bgcolor: String,
    val description: String,
    val font_color: String,
    val font_name: String,
    val redircet_url: String,
    val title: String,
    val location_permission: Boolean,
    val query_paramter: String,
    val show_header: Boolean=true
)