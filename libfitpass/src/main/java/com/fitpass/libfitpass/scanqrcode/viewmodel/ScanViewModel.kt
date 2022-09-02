package com.fitpass.libfitpass.scanqrcode.viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitpass.libfitpass.base.constants.FontIconConstant
import com.fitpass.libfitpass.base.customview.CustomToastView
import com.fitpass.libfitpass.base.customview.RetryListener
import com.fitpass.libfitpass.base.dataencription.RandomKeyGenrator
import com.fitpass.libfitpass.base.http_client.CustomLoader
import com.fitpass.libfitpass.base.utilities.FitpassConfig
import com.fitpass.libfitpass.base.utilities.FitpassPrefrenceUtil
import com.fitpass.libfitpass.base.utilities.Util
import com.fitpass.libfitpass.home.http_client.ApiConstants
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response
import com.fitpass.libfitpass.scanqrcode.models.FitpassScanResponse
import com.fitpass.libfitpass.scanqrcode.models.Results
import com.fitpass.libfitpass.scanqrcode.models.Workout
import com.fitpass.libfitpass.home.http_client.CommonRepository
import com.fitpass.libfitpass.home.http_client.HandleResponseListeners
import com.fitpass.libfitpass.scanqrcode.FitpassScanQrCodeActivity
import com.fitpass.libfitpass.scanqrcode.listeners.FitpassScanListener

class ScanViewModel(
    val commonRepository: CommonRepository,
    val context: Context,
    val activity: Activity, val fitpassScanListener: FitpassScanListener
) : ViewModel(), HandleResponseListeners,RetryListener {
    private var handleResponseListeners: HandleResponseListeners? = null
    var scanWorkoutList = MutableLiveData<ArrayList<Workout>>()
    var scanWorkOutResults = MutableLiveData<Results>()
    var scanViewModel: ScanViewModel? = null
    var isAttend: Boolean = false
    var isLoading: Boolean = false

    init {
        scanViewModel = this
        handleResponseListeners = this
    }

    fun getScanData(requestBody: JSONObject, isAttend1: Boolean) {
        viewModelScope.launch(Dispatchers.Main)
        {
            if (!isLoading) {
                isLoading=true
                isAttend = isAttend1
                val EncryptBodyKey = RandomKeyGenrator.getAlphaNumericString(16)
                RandomKeyGenrator.setRandomKey(EncryptBodyKey)
                val encryptedData = RandomKeyGenrator.encrptBodydata(requestBody.toString())
                withContext(Dispatchers.Main)
                {
                    CustomLoader.showLoaderDialog(activity, context)
                }
                async(Dispatchers.IO) {
                    commonRepository.getScanData(
                        ApiConstants.SCANQRCODE_API,
                        encryptedData,
                        3,
                        handleResponseListeners!!
                    )
                }.await()

            }
        }

    }

    override fun handleSuccess(response1: Response<JsonObject?>, api: String?) {
        val jsonObject = JSONObject(response1!!.body().toString())
        isLoading=false
        Log.d("homeResponse", jsonObject.toString())
        var gson = Gson()
        var response: FitpassScanResponse =
            gson.fromJson(jsonObject.toString(), FitpassScanResponse::class.java)
        viewModelScope.launch {
            if (isAttend) {
                //FitpassScanQrCodeActivity.user_schedule_id="0"
                FitpassScanQrCodeActivity.tvStatus.setText(response.message)
                if( FitpassScanQrCodeActivity.llScanHelp!=null){
                    FitpassScanQrCodeActivity.llScanHelp.visibility = View.GONE
                }
                FitpassPrefrenceUtil.setBooleanPrefs(context, FitpassPrefrenceUtil.ISLOAD_DASHBOARD_DATA,true)
                FitpassScanQrCodeActivity.rlIcon.background = Util.drawRectRadious("#51d071")
                Util.setFantIcon(FitpassScanQrCodeActivity.faIcon, FontIconConstant.ACTIVE_ICON)
            } else {
                scanWorkOutResults.value = response.results
                if (response.results.workout_list != null) {
                    scanWorkoutList.value = response.results.workout_list
                    //setPadding()
                }
            }

            CustomLoader.hideLoaderDialog(activity)
        }
    }

    override fun handleErrorMessage(response: String?, api: String?) {

        Log.d("handleErrorMessage", response.toString())
        CustomToastView.errorToastMessage(activity, context, response,true,this)
        CustomLoader.hideLoaderDialog(activity)
    }

    override fun onRetryClick(value: String) {
        isLoading=false
    }
    fun setPadding() {

        var paddingDp: Int = 150;
        var density = context.getResources().getDisplayMetrics().density.toFloat()
        var paddingPixel = (paddingDp * density).toInt();
       // FitpassScanQrCodeActivity.flScan.setPadding(0, 0, 0, paddingPixel);


        var params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, paddingPixel);
      //  FitpassScanQrCodeActivity.vf.setLayoutParams(params);
      //  FitpassScanQrCodeActivity.bv.setLayoutParams(params);
    }
}