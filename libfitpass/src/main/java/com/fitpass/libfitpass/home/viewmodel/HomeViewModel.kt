package com.fitpass.libfitpass.home.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.viewpager.widget.ViewPager
import com.fitpass.libfitpass.R
import com.fitpass.libfitpass.base.constants.ConfigConstants
import com.fitpass.libfitpass.base.customview.CustomToastView
import com.fitpass.libfitpass.base.dataencription.RandomKeyGenrator
import com.fitpass.libfitpass.base.fitpasscomparators.FitpassLowtoHighComparator
import com.fitpass.libfitpass.base.http_client.CustomLoader
import com.fitpass.libfitpass.base.utilities.FitpassPrefrenceUtil
import com.fitpass.libfitpass.home.FitpassWebViewActivity
import com.fitpass.libfitpass.home.http_client.ApiConstants
import com.fitpass.libfitpass.home.http_client.CommonRepository
import com.fitpass.libfitpass.home.http_client.HandleResponseListeners
import com.fitpass.libfitpass.home.listeners.FitpassHomeListener
import com.fitpass.libfitpass.home.models.*
import com.fitpass.libfitpass.home.models.List
import com.fitpass.libfitpass.scanqrcode.FitpassScanQrCodeActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response
import java.util.*

class HomeViewModel(
    val commonRepository: CommonRepository,
    val context: Context,
    val activity: Activity,
    val vpUpcomming: ViewPager,
    val llDots: LinearLayout,
    val fitpassHomeListener: FitpassHomeListener
) : ViewModel(), HandleResponseListeners {
    var homeresponse = MutableLiveData<HomeResponse>()
    var productList = MutableLiveData<ArrayList<Product>>()
    var faqList = MutableLiveData<ArrayList<List>>()
    var sliderList = MutableLiveData<ArrayList<SliderActivity>>()
    var macroList = MutableLiveData<ArrayList<MacrosDetail>>()
    private var handleResponseListeners: HandleResponseListeners? = null
    var homeViewModel: HomeViewModel? = null
    var isScanQrCode = MutableLiveData<Boolean>()

    init {
        isScanQrCode.value = false
        homeViewModel = this
        handleResponseListeners = this@HomeViewModel
    }

    fun getHomeData(requestBody: JSONObject) {
        viewModelScope.launch(Dispatchers.Main)
        {
            val dynamicSecretKey = RandomKeyGenrator.generate16DigitRandom()
           // val EncryptBodyKey = RandomKeyGenrator.getAlphaNumericString(16)
            val EncryptBodyKey = RandomKeyGenrator.getAlphaNumericString(16)
            RandomKeyGenrator.setRandomKey(dynamicSecretKey)
            val encryptedData = RandomKeyGenrator.encrptBodydataWithRandomKey(requestBody.toString())
            withContext(Dispatchers.Main)
            {
                CustomLoader.showLoaderDialog(activity, context)
            }
            async(Dispatchers.IO) {
                commonRepository.getHomeData(
                    ApiConstants.HOME_API,
                    encryptedData,
                    3,
                    handleResponseListeners!!
                )
            }.await()

        }

    }

    override fun handleSuccess(response1: Response<JsonObject?>, api: String?) {
        val jsonObject = JSONObject(response1!!.body().toString())
        Log.d("homeResponse", jsonObject.toString())
        var gson = Gson()
        var response: HomeResponse = gson.fromJson(jsonObject.toString(), HomeResponse::class.java)
        viewModelScope.launch {
            homeresponse.value = response
            if (response.results.slider_activity != null) {
                FitpassPrefrenceUtil.setStringPrefs(context, FitpassPrefrenceUtil.APP_KEY, homeresponse!!.value!!.results.user_details.app_key.toString())
                FitpassPrefrenceUtil.setStringPrefs(context, FitpassPrefrenceUtil.USER_ID, homeresponse!!.value!!.results.user_details.user_id.toString())
                FitpassPrefrenceUtil.setStringPrefs(context, FitpassPrefrenceUtil.SECRET_KEY, homeresponse!!.value!!.results.user_details.secret_key.toString())
                val slideractivitylist = ArrayList<SliderActivity>()
                for (data in response.results.slider_activity) {
                    if (data.action.equals(ConfigConstants.WORKOUT_ACTION)) {
                        isScanQrCode.value = true
                        slideractivitylist.add(data)
                    } else if (data.action.equals(ConfigConstants.NOTICE_ACTION)) {
                        if (!data?.data?.img.isNullOrEmpty()) {
                            slideractivitylist.add(data)
                        }

                    } else if (data.action.equals(ConfigConstants.MEAL_LOG_ACTION)) {
                        slideractivitylist.add(data)
                        macroList.value = data.macros_details

                    } else if (data.action.equals(ConfigConstants.HRA_COMPLETE_ACTION)) {
                        slideractivitylist.add(data)
                        if (!data?.data?.img.isNullOrEmpty()) {
                            slideractivitylist.add(data)
                        }
                    }
                }
                Collections.sort(slideractivitylist, FitpassLowtoHighComparator())
                if(sliderList.value!=null){
                    sliderList.value!!.clear()
                }
                sliderList.value = slideractivitylist
                setupPagerIndidcatorDots(0)
                setViewPagerListener()
            }
            if (response.results.product_list != null) {
                productList.value = response.results.product_list
            }
            if (response.results.faq.list != null) {
                faqList.value = response.results.faq.list
            }
            CustomLoader.hideLoaderDialog(activity)
        }
    }

    override fun handleErrorMessage(response: String?, api: String?) {
        Log.d("handleErrorMessage", response.toString())
        CustomToastView.errorToasMessage(activity, context, response)
        CustomLoader.hideLoaderDialog(activity)

    }

    fun setViewPagerListener() {

        vpUpcomming.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {

                setupPagerIndidcatorDots(position)
            }
        })
        setupPagerIndidcatorDots(0)
    }

    private fun setupPagerIndidcatorDots(selectedPos: Int) {
        llDots.removeAllViews()
        var density = context.getResources().getDisplayMetrics().density.toFloat()
        var paddingPixel4 = (4 * density).toInt();
        var paddingPixel46 = (46 * density).toInt();
        var paddingPixel5 = (2 * density).toInt();
        for (i in 0 until sliderList.value!!.size) {
            var imageView = ImageView(context)
            val params1: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                paddingPixel4, paddingPixel4
            )
            params1.setMargins(paddingPixel5, 0, paddingPixel5, 0)
            val params2: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                paddingPixel46, paddingPixel4
            )
            params2.setMargins(paddingPixel5, 0, paddingPixel5, 0)
            if (i == selectedPos) {
                imageView.layoutParams = params2
                imageView.setBackground(context.getResources().getDrawable(R.drawable.gray_react))
            } else {
                imageView.layoutParams = params1
                imageView.setBackground(context.getResources().getDrawable(R.drawable.gray_circle))
            }
            llDots.addView(imageView)
            //binding.llDots.bringToFront()
        }
    }

    fun upCommingActions(action: String, url: String?,showHeader:Boolean?) {
        openWebActivity(url,showHeader)
    }

    fun openWebActivity(url: String?,showHeader:Boolean?) {
        if (!url.isNullOrEmpty()) {
            var intent = Intent(context, FitpassWebViewActivity::class.java)
            intent.putExtra("url", url)
            intent.putExtra("show_header", showHeader)
            context.startActivity(intent)
        }
    }


    fun openScanActivity() {
        var intent = Intent(context, FitpassScanQrCodeActivity::class.java)
        context.startActivity(intent)
    }


}