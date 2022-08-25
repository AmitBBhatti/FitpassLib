package com.fitpass.libfitpass.base.http_client

import com.google.gson.annotations.SerializedName

data class ErrorResponse( @SerializedName("code")
                          var code:Int,

                          @SerializedName("message")
                          var message:String,

                          @SerializedName("results")
                          var results:ErrorResult,

                          @SerializedName("status")
                          var status:String) {
}
