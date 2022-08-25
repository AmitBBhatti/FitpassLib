package com.fitpass.libfitpass.home.http_client

import com.google.gson.JsonObject
import retrofit2.Response

interface HandleResponseListeners {
    fun handleSuccess(response1: Response<JsonObject?>, api: String?)
    fun handleErrorMessage(response: String?, api: String?)
}