package com.fitpass.libfitpass.home.models
import kotlin.collections.List
data class SliderActivity(
    val action: String,
    val `data`: Data,
    val order_by: Int,
    val redirect_url: String,
    val cta_text: String,
    val label: String,
    val macros_details: ArrayList<MacrosDetail>,
    val text_message: String,
    val today_calorie_taken: TodayCalorieTaken,
    val url: String,
    val show_header: Boolean
)