package com.fitpass.libfitpass.home.http_client

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {
    @POST(ApiConstants.HOME_API)
    suspend fun getHomeDataApi(@Body request: String?): Response<JsonObject?>?
    @POST(ApiConstants.SCANQRCODE_API)
    suspend fun getScanDataApi(@Body request: String?): Response<JsonObject?>?
}