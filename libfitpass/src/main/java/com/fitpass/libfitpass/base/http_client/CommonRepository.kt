package com.fitpass.libfitpass.home.http_client

import android.app.Activity
import android.content.Context
import android.util.Log
import com.fitpass.libfitpass.base.customview.CustomToastView
import com.fitpass.libfitpass.base.dataencription.RandomKeyGenrator
import com.fitpass.libfitpass.base.http_client.ApiClient
import com.fitpass.libfitpass.base.http_client.CustomLoader
import com.fitpass.libfitpass.base.http_client.ErrorResponse
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class CommonRepository(val mContext: Context, val mActivity: Activity) {
    lateinit var handleResponseListeners: HandleResponseListeners

    suspend fun getHomeData(
        endPointUrl: String,
        request: String,
        paramCount: Int,
        handleResponseListeners: HandleResponseListeners) {
        this.handleResponseListeners = handleResponseListeners
        Log.d("Data_encrypted",request+"..")
       // Log.d("Data_decripted", RandomKeyGenrator.decrptBODYFile(request).toString()+"..")
        val apiService = ApiClient.getApiClient(mContext, paramCount).create(ApiInterface::class.java)
         var jsonObject = apiService.getHomeDataApi(request)
        var response = JsonObject()
        response = jsonObject?.body()!!
        Log.e("server response", jsonObject.toString())
        handleResponse(jsonObject, endPointUrl)
    }

    fun handleResponse(data: Response<JsonObject?>?, url: String) {
        if (data != null && data!!.isSuccessful) {
            Log.e("response Received", data?.body().toString())
            val response = JSONObject(data!!.body().toString())
            if (response != null) {
                if (response!!.has("code")) {
                    var responseCode = response!!.optInt("code")
                    if (responseCode == 401 && responseCode == 502) {
                        // FitpassAplicationActivity().userLogout()
                    } else if (responseCode == 200) {
                        handleResponseListeners.handleSuccess(data, url)

                    } else if (responseCode == 412) {
                        handleResponseListeners.handleErrorMessage(
                            response?.optString("message"),
                            url
                        )
                    } else {
                        handleResponseListeners.handleErrorMessage(
                            response?.optString("message"),
                            url
                        )
                    }
                }
            }

        } else {

            if (data != null) {
                if (data!!.code() == 401 || data!!.code() > 500) {
                    //FitpassAplicationActivity.getInstance().userLogout()
                } else if (data?.code() == 412) {
                    val errorBody: ResponseBody = data!!.errorBody()!!
                    val error: ErrorResponse =
                        Gson().fromJson(errorBody.charStream(), ErrorResponse::class.java)
                    var JsonObject = Gson().toJson(error)
                    var erroJsonObject = JSONObject(JsonObject)

                    CustomToastView.errorToasMessage(
                        mActivity,
                        mContext,
                        "" + erroJsonObject.getString("message")
                    )


                } else {
                    val errorBody: ResponseBody = data!!.errorBody()!!
                    val error: ErrorResponse =
                        Gson().fromJson(errorBody.charStream(), ErrorResponse::class.java)
                    var JsonObject = Gson().toJson(error)
                    var erroJsonObject = JSONObject(JsonObject)
                    // handleResponseListeners.handleErrorMessage(erroJsonObject?.optString("message"),url)

                    CustomToastView.errorToasMessage(
                        mActivity,
                        mContext,
                        "" + erroJsonObject.getString("message")
                    )


                }
            }

        }

    }
}