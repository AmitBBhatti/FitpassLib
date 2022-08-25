package com.fitpass.libfitpass.base.http_client

import com.google.gson.annotations.SerializedName

data class ErrorResult(
    @SerializedName("unlock_fitfeast")
    var unlock_fitfeast:UnlockFitfeastModel,
    @SerializedName("cta_text")
    var cta_text:String,

    @SerializedName("image")
    var image:String)
