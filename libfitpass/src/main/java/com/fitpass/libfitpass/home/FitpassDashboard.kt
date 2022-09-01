package com.fitpass.libfitpass.home


import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fitpass.libfitpass.R
import com.fitpass.libfitpass.base.utilities.*
import com.fitpass.libfitpass.databinding.ActivityFitPassDashboardBinding
import com.fitpass.libfitpass.home.http_client.CommonRepository
import com.fitpass.libfitpass.home.listeners.FitpassHomeListener
import com.fitpass.libfitpass.home.models.Product
import com.fitpass.libfitpass.home.viewmodel.HomeViewModel
import com.fitpass.libfitpass.home.viewmodelfactory.HomeViewModelFactory
import com.fitpass.libfitpass.scanqrcode.FitpassScanQrCodeActivity
import org.json.JSONObject
import java.lang.Exception
import java.util.*

class FitpassDashboard : AppCompatActivity(), FitpassHomeListener {
    lateinit var binding: ActivityFitPassDashboardBinding
    private var homeViewModel: HomeViewModel? = null
    var PERMISSION_CODE: Int = 101
    var LOCATION_PERMISSION_CODE: Int = 102
    var weburl: String = ""
    var message: String = ""
    var show_header: Boolean = true

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fit_pass_dashboard)
        setHeader()
        binding.tvWishMesg.setText(getWishMessage())
        /* var fm=supportFragmentManager
         var ft:FragmentTransaction=fm.beginTransaction()
         ft.add(R.id.container,HomeFragment())
         ft.commit()*/
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
        homeViewModel?.let {
            var request = JSONObject()
            request.put("vendor_id", "183")
            request.put("policy_number", "11111111113111")
            request.put("member_id", "15143512435")
            // it.getHomeData(Request("15143512435","11111111113111","183"))
            it.getHomeData(request)
        }
        binding.llScan.setOnClickListener {
            checkPermission()
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
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                PERMISSION_CODE
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
                alert(resources.getString(R.string.cameramsg))
            } else if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_DENIED
            ) {
                alert(resources.getString(R.string.locationmsg))
            } else if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                == PackageManager.PERMISSION_DENIED
            ) {
                alert(resources.getString(R.string.locationmsg))
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("onRequestResult", "PERMISSION_GRANTED")
                openScanActivity()

            }
        }
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_DENIED
            ) {
                alert(resources.getString(R.string.locationmsg))
            } else if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_DENIED
            ) {
                alert(resources.getString(R.string.locationmsg))
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("onRequestResult", "PERMISSION_GRANTED")
                FitpassLocationUtil.refreshLocation(this)
                encodeBase64Url()
            }
        }

    }

    fun openScanActivity() {
        var intent = Intent(this, FitpassScanQrCodeActivity::class.java)
        startActivity(intent)
    }


    fun alert(msg: String) {
        val builder1 = AlertDialog.Builder(this)
        builder1.setMessage(msg)
        builder1.setCancelable(false)
        builder1.setPositiveButton(
            "Yes"
        ) { dialog, id ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", this.packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        builder1.setNegativeButton(
            "No"
        ) { dialog, id -> onBackPressed() }
        val alert11 = builder1.create()
        alert11.show()


    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onScanClick() {
        checkPermission()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMenuClick(data: Product) {
        weburl = data.redircet_url
        Log.d("show_header", show_header.toString())

        this.show_header = data.show_header

        Log.d("show_header", show_header.toString())
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
        if (ActivityCompat.checkSelfPermission(
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
                encodeBase64Url()
            }
        }
    }

    fun encodeBase64Url() {
        /*https://fitpass-pwa.web.app?querystring=latitude===28.6456&&longitude===77.2024&&header_font_color===#000000&&header_bgcolor===#ffffff&&user_id=1*/
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
                Log.d("weburl", weburl)
        val data1: ByteArray = concaturl.encodeToByteArray()
        val base64: String = Base64.encodeToString(data1, Base64.DEFAULT)
        weburl = weburl + "?querystring=" + base64
        Log.d("weburl", weburl)
        openWebActivity()
    }
}