package com.fitpass.libfitpass.home.models

data class Results(
    val faq: Faq,
    val product_list: ArrayList<Product>,
    val slider_activity: ArrayList<SliderActivity>,
    val user_details: UserDetails
)