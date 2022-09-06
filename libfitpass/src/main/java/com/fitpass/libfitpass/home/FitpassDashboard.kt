package com.fitpass.libfitpass.home


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.fitpass.libfitpass.R
import com.fitpass.libfitpass.base.constants.ConfigConstants
import com.fitpass.libfitpass.base.utilities.*
import com.fitpass.libfitpass.databinding.ActivityFitPassDashboardBinding
import com.fitpass.libfitpass.home.adapters.UpcomingAdapter
import com.fitpass.libfitpass.home.http_client.CommonRepository
import com.fitpass.libfitpass.home.listeners.FitpassHomeListener
import com.fitpass.libfitpass.home.models.Product
import com.fitpass.libfitpass.home.models.SliderActivity
import com.fitpass.libfitpass.home.viewmodel.HomeViewModel
import com.fitpass.libfitpass.home.viewmodelfactory.HomeViewModelFactory
import com.fitpass.libfitpass.scanqrcode.FitpassScanQrCodeActivity
import com.google.gson.Gson
import org.json.JSONObject
import java.util.*


class FitpassDashboard : AppCompatActivity(), FitpassHomeListener {
    lateinit var binding: ActivityFitPassDashboardBinding
    private var homeViewModel: HomeViewModel? = null
    var PERMISSION_CODE: Int = 101
    var LOCATION_PERMISSION_CODE: Int = 102
    var LOCATION_PERMISSION_CODE1: Int = 103
    var LOCATION_PERMISSION_CODE2: Int = 104
    var weburl: String = ""
    var message: String = ""
    var vendorId: String = ""
    var policyNo: String = ""
    var memberId: String = ""
    var latitude: String = ""
    var longitude: String = ""
    var show_header: Boolean = true

    // lateinit var sliderWorkoutata:SliderActivity
    var workoutdata = ""
    val slideractivitylist = ArrayList<SliderActivity>()

    private val activitiesConfig =
        "[{\"image_id\":\"activity_2\",\"activity_color\":\"#815E0D\",\"start_color\":\"#998046\",\"end_color\":\"#815E0D\",\"activity_id\":\"2\"}," +
                "{\"image_id\":\"activity_4\",\"activity_color\":\"#FBBB74\",\"start_color\":\"#F5BF40\",\"end_color\":\"#FBBB74\",\"activity_id\":\"4\"}," +
                "{\"image_id\":\"activity_15\",\"activity_color\":\"#6E1F7A\",\"start_color\":\"#AD68B7\",\"end_color\":\"#6E1F7A\",\"activity_id\":\"15\"}," +
                "{\"image_id\":\"activity_1\",\"activity_color\":\"#4DC349\",\"start_color\":\"#80DB7E\",\"end_color\":\"#4DC349\",\"activity_id\":\"1\"}," +
                "{\"image_id\":\"activity_6\",\"activity_color\":\"#6345C6\",\"start_color\":\"#9F6EDC\",\"end_color\":\"#6345C6\",\"activity_id\":\"6\"}," +
                "{\"image_id\":\"activity_14\",\"activity_color\":\"#59B1D7\",\"start_color\":\"#10BDDB\",\"end_color\":\"#59B1D7\",\"activity_id\":\"14\"}," +
                "{\"image_id\":\"activity_13\",\"activity_color\":\"#1C1162\",\"start_color\":\"#675EA4\",\"end_color\":\"#1C1162\",\"activity_id\":\"13\"}," +
                "{\"image_id\":\"activity_16\",\"activity_color\":\"#D04A31\",\"start_color\":\"#F5908C\",\"end_color\":\"#D04A31\",\"activity_id\":\"16\"}," +
                "{\"image_id\":\"activity_3\",\"activity_color\":\"#5E8CEB\",\"start_color\":\"#879AF5\",\"end_color\":\"#5E8CEB\",\"activity_id\":\"3\"}," +
                "{\"image_id\":\"activity_12\",\"activity_color\":\"#8A2E14\",\"start_color\":\"#A85D4A\",\"end_color\":\"#8A2E14\",\"activity_id\":\"12\"}," +
                "{\"image_id\":\"activity_9\",\"activity_color\":\"#41CCC6\",\"start_color\":\"#5FEAE5\",\"end_color\":\"#41CCC6\",\"activity_id\":\"9\"}," +
                "{\"image_id\":\"activity_10\",\"activity_color\":\"#C734E0\",\"start_color\":\"#E981FB\",\"end_color\":\"#C734E0\",\"activity_id\":\"10\"}," +
                "{\"image_id\":\"activity_11\",\"activity_color\":\"#8F40E6\",\"start_color\":\"#B47DF1\",\"end_color\":\"#8F40E6\",\"activity_id\":\"11\"}," +
                "{\"image_id\":\"activity_7\",\"activity_color\":\"#DA4361\",\"start_color\":\"#EF78BA\",\"end_color\":\"#DA4361\",\"activity_id\":\"7\"}," +
                "{\"image_id\":\"activity_8\",\"activity_color\":\"#EAC644\",\"start_color\":\"#EBE382\",\"end_color\":\"#EAC644\",\"activity_id\":\"8\"}," +
                "{\"image_id\":\"activity_5\",\"activity_color\":\"#D04A31\",\"start_color\":\"#F5908C\",\"end_color\":\"#D04A31\",\"activity_id\":\"5\"}," +
                "{\"image_id\":\"activity_17\",\"activity_color\":\"#DA4361\",\"start_color\":\"#EF78BA\",\"end_color\":\"#DA4361\",\"activity_id\":\"17\"}," +
                "{\"image_id\":\"activity_18\",\"activity_color\":\"#EAC644\",\"start_color\":\"#EBE382\",\"end_color\":\"#EAC644\",\"activity_id\":\"18\"}," +
                "{\"image_id\":\"activity_19\",\"activity_color\":\"#41CCC6\",\"start_color\":\"#5FEAE5\",\"end_color\":\"#41CCC6\",\"activity_id\":\"19\"}]"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fit_pass_dashboard)
        binding.rlAlert.visibility = View.GONE
        FitpassPrefrenceUtil.setBooleanPrefs(this, FitpassPrefrenceUtil.ISLOAD_DASHBOARD_DATA, true)
        FitpassPrefrenceUtil.setStringPrefs(
            this,
            FitpassPrefrenceUtil.ACTIVITY_DATA,
            activitiesConfig
        )
        setHeader()

        binding.tvWishMesg.setText(getWishMessage())
        setPadding()
        checkLocationPermission(false)
        var commonRepository = CommonRepository(this, this)
        var viewModelFactories = HomeViewModelFactory(
            commonRepository,
            this,
            this,
            binding.vpUpcomming,
            binding.llDots,
            this
        )
        homeViewModel = ViewModelProvider(this, viewModelFactories!!).get(HomeViewModel::class.java)
        binding.lifecycleOwner = this
        binding.homedata = homeViewModel

        binding.llScan.setOnClickListener {
            workoutdata = ""
            try {
                if (slideractivitylist.size == 1) {
                    if (!slideractivitylist.get(0).data.workout_status.equals("3")) {
                        var gson = Gson()
                        workoutdata = gson.toJson(slideractivitylist.get(0))
                    }
                }
            }catch (e:Exception){

            }
            checkPermission()
        }
        homeViewModel!!.sliderList.observe(this, {

            slideractivitylist.clear()
            for (data in it) {
                if (data.action.equals(ConfigConstants.WORKOUT_ACTION)) {
                    slideractivitylist.add(data)
                }
            }
            setUpcommingData(homeViewModel!!.sliderList)
        })
        homeViewModel!!.homeresponse.observe(this, {

        })
    }

    fun getIntentData(): Boolean {
        var bundle = intent.extras
        if (bundle != null) {
            if (!bundle.containsKey("vendor_id")) {
                FitpassToastUtil.showToastLong(this, resources.getString(R.string.vendormsg))
                 onBackPressed()
                return false
            } else if (!bundle.containsKey("policy_number")) {
                FitpassToastUtil.showToastLong(this, resources.getString(R.string.policymsg))
                onBackPressed()
                return false
            } else if (!bundle.containsKey("member_id")) {
                FitpassToastUtil.showToastLong(this, resources.getString(R.string.membermsg))
                onBackPressed()
                return false
            } else {
                vendorId = bundle.getString("vendor_id")!!
                policyNo = bundle.getString("policy_number")!!
                memberId = bundle.getString("member_id")!!
                return true
            }
        } else {
            FitpassToastUtil.showToastLong(this, resources.getString(R.string.vendormsg))
            onBackPressed()
            return false
        }
    }

    fun setUpcommingData(items: MutableLiveData<ArrayList<SliderActivity>>) {
        val adapter = UpcomingAdapter(this, homeViewModel!!, this)
        adapter.updateItems(items)
        binding.vpUpcomming.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        binding.rlAlert.visibility = View.GONE
        if(getIntentData()) {
            if (FitpassPrefrenceUtil.getBooleanPrefs(
                    this,
                    FitpassPrefrenceUtil.ISLOAD_DASHBOARD_DATA,
                    false
                )
            ) {
                FitpassPrefrenceUtil.setBooleanPrefs(
                    this,
                    FitpassPrefrenceUtil.ISLOAD_DASHBOARD_DATA,
                    false
                )
                homeViewModel?.let {
                    var request = JSONObject()
                    request.put("vendor_id", vendorId)
                    request.put("policy_number", policyNo)
                    request.put("member_id", memberId)
                    if (FitpassNetworkUtil.checkInternetConnection(this)) {
                        it.getHomeData(request)
                    }

                }
            }
        }

    }

    fun gpsAlert(title: String, msg: String) {
        binding.rlAlert.visibility = View.VISIBLE
        binding.alertpopup.tvTitle.setText(title)
        binding.alertpopup.tvMsg.setText(msg)
        binding.alertpopup.tvCancel.setOnClickListener {
            binding.rlAlert.visibility = View.GONE
        }
        binding.alertpopup.tvSetting.setOnClickListener {
            startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    fun getWishMessage(): String {
        val c: Calendar = Calendar.getInstance()
        val timeOfDay: Int = c.get(Calendar.HOUR_OF_DAY)
        if (timeOfDay >= 0 && timeOfDay < 12) {
            message = "Good Morning!"
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            message = "Good Afternoon!"
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            message = "Good Evening";
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            message = "Good Evening1"
        }
        return message!!
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkPermission() {

        val manager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gpsAlert(
                resources.getString(R.string.turnonlocation),
                resources.getString(R.string.locationmsg)
            )
        } else if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.CAMERA
                ),
                PERMISSION_CODE
            )
        } else if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                LOCATION_PERMISSION_CODE1
            )

        } else {
            Log.d("checkPermission", "grant")
            openScanActivity()
        }
    }

    fun setHeader() {

        var fitpassConfig = FitpassConfig.getInstance()
        if (!fitpassConfig!!.getHeaderColor().isNullOrEmpty()) {
            FitpassConfigUtil.setStatusBarColor(this, this, fitpassConfig.getHeaderColor())
            try {
                binding.tvHeader.setBackgroundColor(Color.parseColor(fitpassConfig.getHeaderColor()))
            } catch (e: Exception) {
            }
        }
        if (!fitpassConfig!!.getHeaderTitle().isNullOrEmpty()) {
            try {
                binding.tvHeader.setText(fitpassConfig!!.getHeaderTitle())
            } catch (e: Exception) {
            }
        }
        if (!fitpassConfig!!.getHeaderFontColor().isNullOrEmpty()) {
            try {
                binding.tvHeader.setTextColor(Color.parseColor(fitpassConfig!!.getHeaderFontColor()))
            } catch (e: Exception) {
            }
        }

    }

    fun setPadding() {
        var fitpassConfig = FitpassConfig.getInstance()
        var paddingDp: Int = fitpassConfig!!.getPadding();
        var density = getResources().getDisplayMetrics().density.toFloat()
        var paddingPixel = (paddingDp * density).toInt();
        binding.rlFitpassid.setPadding(paddingPixel, 0, paddingPixel, 0);
        binding.rvMenu.setPadding(paddingPixel, 0, paddingPixel, 0);
        var paddingDp1: Int = fitpassConfig!!.getPadding() - 5;
        var paddingPixel1 = (paddingDp1 * density).toInt();
        binding.vpUpcomming.setPadding(paddingPixel1, 0, paddingPixel1, 0);
        var paddingDp2: Int = fitpassConfig!!.getPadding() - 4;
        var paddingPixel2 = (paddingDp2 * density).toInt();
        binding.rvFaq.setPadding(paddingPixel, 0, paddingPixel, 0);

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("onRequestResult", requestCode.toString() + "...")
        if (requestCode == PERMISSION_CODE) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED
            ) {
                alert(
                    resources.getString(R.string.turnoncamera),
                    resources.getString(R.string.cameramsg)
                )

            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("onRequestResult", "PERMISSION_GRANTED")
                checkPermission()

            }
        } else if (requestCode == LOCATION_PERMISSION_CODE1) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_DENIED
            ) {
                alert(
                    resources.getString(R.string.turnonlocation),
                    resources.getString(R.string.locationmsg)
                )
            } else if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                == PackageManager.PERMISSION_DENIED
            ) {
                alert(
                    resources.getString(R.string.turnonlocation),
                    resources.getString(R.string.locationmsg)
                )
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("onRequestResult", "PERMISSION_GRANTED")
                openScanActivity()

            }
        } else if (requestCode == LOCATION_PERMISSION_CODE) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_DENIED
            ) {
                alert(
                    resources.getString(R.string.turnonlocation),
                    resources.getString(R.string.locationmsg)
                )
            } else if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_DENIED
            ) {
                alert(
                    resources.getString(R.string.turnonlocation),
                    resources.getString(R.string.locationmsg)
                )
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("onRequestResult", "PERMISSION_GRANTED")
                FitpassLocationUtil.refreshLocation(this)
                encodeBase64Url(true)
            }
        }else  if (requestCode == LOCATION_PERMISSION_CODE2) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_DENIED
            ) {
                alert(
                    resources.getString(R.string.turnonlocation),
                    resources.getString(R.string.locationmsg)
                )
            } else if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_DENIED
            ) {
                alert(
                    resources.getString(R.string.turnonlocation),
                    resources.getString(R.string.locationmsg)
                )
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                FitpassLocationUtil.refreshLocation(this)
               openMap(true)
            }
        }

    }

    fun openScanActivity() {

        var intent = Intent(this, FitpassScanQrCodeActivity::class.java)
        if (!workoutdata.isNullOrEmpty()) {
            intent.putExtra("sliderWorkoutata", workoutdata)
        }
        startActivity(intent)
    }


    fun alert(title: String, msg: String) {
        binding.rlAlert.visibility = View.VISIBLE
        binding.alertpopup.tvTitle.setText(title)
        binding.alertpopup.tvMsg.setText(msg)
        binding.alertpopup.tvCancel.setOnClickListener {
            binding.rlAlert.visibility = View.GONE
        }
        binding.alertpopup.tvSetting.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", this.packageName, null)
            intent.data = uri
            startActivity(intent)
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onScanClick(item: SliderActivity) {
        var gson = Gson()
        workoutdata = gson.toJson(item)
        checkPermission()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDirectionClick(item: SliderActivity) {
         latitude=item.data.latitude
         longitude=item.data.longitude
        val manager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gpsAlert(
                resources.getString(R.string.turnonlocation),
                resources.getString(R.string.locationmsg)
            )
        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                LOCATION_PERMISSION_CODE2
            )

        } else {
            openMap(false)
        }
    }
    fun openMap(isHold:Boolean){
        if (isHold) {
            Handler().postDelayed(Runnable {
                openMap()
            }, 3000)
        } else {
            openMap()
        }
    }
    fun openMap()
    {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=" + FitpassPrefrenceUtil.getStringPrefs(this, FitpassPrefrenceUtil.LATITUDE, "0.0") + "," + FitpassPrefrenceUtil.getStringPrefs(this, FitpassPrefrenceUtil.LONGITUDE, "0.0"))
        )
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMenuClick(data: Product) {
        weburl = data.redircet_url
        this.show_header = data.show_header
        if (data.location_permission) {
            checkLocationPermission(true)
        } else {
            openWebActivity()
        }

    }

    fun openWebActivity() {
        var intent = Intent(this, FitpassWebViewActivity::class.java)
        intent.putExtra("url", weburl)
        intent.putExtra("show_header", show_header)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkLocationPermission(isopenWeb: Boolean) {
        val manager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gpsAlert(
                resources.getString(R.string.turnonlocation),
                resources.getString(R.string.locationmsg)
            )
        } else if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (isopenWeb) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    LOCATION_PERMISSION_CODE
                )
            }
        } else {
            FitpassLocationUtil.refreshLocation(this)
            if (isopenWeb) {
                encodeBase64Url(false)
            }
        }
    }


    fun encodeBase64Url(isHold: Boolean) {
        /*https://fitpass-pwa.web.app?querystring=latitude===28.6456&&longitude===77.2024&&header_font_color===#000000&&header_bgcolor===#ffffff&&user_id=1*/
        if (isHold) {
            Handler().postDelayed(Runnable {
                genBase64WithUrl()
                openWebActivity()
            }, 3000)
        } else {
            genBase64WithUrl()
            openWebActivity()
        }


    }

    fun genBase64WithUrl() {
        var fitpassConfig = FitpassConfig.getInstance()
        var concaturl: String = "latitude===" + FitpassPrefrenceUtil.getStringPrefs(
            this,
            FitpassPrefrenceUtil.LATITUDE,
            "0.0"
        ) +
                "&&&longitude===" + FitpassPrefrenceUtil.getStringPrefs(
            this,
            FitpassPrefrenceUtil.LONGITUDE,
            "0.0"
        ) +
                "&&&header_font_color===" + fitpassConfig!!.getHeaderFontColor() +
                "&&&header_bgcolor===" + fitpassConfig.getHeaderColor() +
                "&&&user_id===" + FitpassPrefrenceUtil.getStringPrefs(
            this,
            FitpassPrefrenceUtil.USER_ID,
            ""
        ) +
                "&&&padding===" + fitpassConfig!!.getPadding()
        Log.d("weburl", weburl)
        val data1: ByteArray = concaturl.encodeToByteArray()
        val base64: String = Base64.encodeToString(data1, Base64.DEFAULT)
        weburl = weburl + "?querystring=" + base64
        Log.d("weburl", weburl)
    }
}