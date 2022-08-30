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
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import com.fitpass.libfitpass.base.utilities.FitpassConfig
import com.fitpass.libfitpass.base.utilities.FitpassConfigUtil

import com.fitpass.libfitpass.R

import com.fitpass.libfitpass.home.http_client.CommonRepository
import com.fitpass.libfitpass.home.listeners.FitpassHomeListener
import com.fitpass.libfitpass.home.viewmodel.HomeViewModel
import com.fitpass.libfitpass.home.viewmodelfactory.HomeViewModelFactory
import com.fitpass.libfitpass.scanqrcode.FitpassScanQrCodeActivity
import org.json.JSONObject
import com.fitpass.libfitpass.databinding.ActivityFitPassDashboardBinding

class FitpassDashboard : AppCompatActivity(), FitpassHomeListener {
    lateinit var binding: ActivityFitPassDashboardBinding
    private var homeViewModel: HomeViewModel?=null
    var PERMISSION_CODE:Int=101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fit_pass_dashboard)
        setHeaderColor()
       /* var fm=supportFragmentManager
        var ft:FragmentTransaction=fm.beginTransaction()
        ft.add(R.id.container,HomeFragment())
        ft.commit()*/
        setPadding()
        var commonRepository= CommonRepository(this,this)
        var viewModelFactories=  HomeViewModelFactory(commonRepository,this,this, binding.vpUpcomming, binding.llDots,this)
        homeViewModel= ViewModelProvider(this,viewModelFactories!!).get(HomeViewModel::class.java)
        binding.lifecycleOwner=this
        binding.homedata=homeViewModel
        homeViewModel?.let {
            var request= JSONObject()
            request.put("vendor_id","183")
            request.put("policy_number","11111111113111")
            request.put("member_id","15143512435")
            // it.getHomeData(Request("15143512435","11111111113111","183"))
            it.getHomeData(request)
        }
        binding.llScan.setOnClickListener {
            checkPermission()
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun checkPermission() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_CODE
            )
        }else{
            Log.d("checkPermission","grant")
            openScanActivity()
        }


    }
    fun setHeaderColor() {
        var fitpassConfig = FitpassConfig.getInstance()
        if (!fitpassConfig!!.getHeaderColor().isNullOrEmpty()) {
            FitpassConfigUtil.setStatusBarColor(this, this, fitpassConfig.getHeaderColor())
            binding.tvHeader.setBackgroundColor(Color.parseColor(fitpassConfig.getHeaderColor()))
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
        binding.vpUpcomming.setPadding(paddingPixel1, 0, 0, 0);
        var paddingDp2: Int = fitpassConfig!!.getPadding() - 4;
        var paddingPixel2 = (paddingDp2 * density).toInt();
        binding.rvFaq.setPadding(paddingPixel, 0, paddingPixel, 0);
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("onRequestResult",PERMISSION_CODE.toString()+"...")
        if (requestCode == PERMISSION_CODE) {
             if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                 == PackageManager.PERMISSION_DENIED) {
                 alert("We need to allow the camera permission to scan the QR Code. Do you want to allow it.")
             }else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                 == PackageManager.PERMISSION_DENIED
             ) {
                 alert("We need to allow the location permission. Do you want to allow it.")
             }
            else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                 == PackageManager.PERMISSION_DENIED
             ) {
                 alert("We need to allow the location permission. Do you want to allow it.")
             }
            else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("onRequestResult","PERMISSION_GRANTED")
                openScanActivity()

            }
        }

    }
    fun openScanActivity() {
        var intent = Intent(this, FitpassScanQrCodeActivity::class.java)
            startActivity(intent)
    }


    fun alert(msg:String) {
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

}