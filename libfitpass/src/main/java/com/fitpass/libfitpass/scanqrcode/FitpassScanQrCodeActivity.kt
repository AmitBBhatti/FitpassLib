package com.fitpass.libfitpass.scanqrcode

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fitpass.libfitpass.R
import com.fitpass.libfitpass.scanqrcode.viewmodel.ScanViewModel
import com.fitpass.libfitpass.scanqrcode.viewmodelfactory.ScanViewModelFactory
import com.fitpass.libfitpass.base.constants.FontIconConstant
import com.fitpass.libfitpass.base.customview.CustomToastView
import com.fitpass.libfitpass.base.utilities.*
import com.fitpass.libfitpass.base.utilities.Util
import com.fitpass.libfitpass.databinding.ActivityFitpassScanQrCodeBinding
import com.fitpass.libfitpass.fontcomponent.FontAwesome
import com.fitpass.libfitpass.home.http_client.CommonRepository
import com.fitpass.libfitpass.scanqrcode.listeners.FitpassScanListener
import com.fitpass.libfitpass.scanqrcode.models.Workout
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.journeyapps.barcodescanner.*
import org.json.JSONObject
import java.io.InputStream


class FitpassScanQrCodeActivity : AppCompatActivity(),FitpassScanListener{
    var CAMERA_REQUEST_CODE: Int = 100
    var LOCATION_REQUEST_CODE: Int = 100
    var PICK_IMAGE_FROM_GALLERY: Int = 100
    private var capture: CaptureManager? = null

    lateinit var binding: ActivityFitpassScanQrCodeBinding
    lateinit var llFlash: LinearLayout
    lateinit var rlScanGalley: RelativeLayout
    lateinit var llRefreshLocation: LinearLayout
    lateinit var tvScanGallery: TextView
    lateinit var faFlash : FontAwesome
    var isFlash: Boolean = true
    var isFlashAvail: Boolean = true

    var workout_name: String = ""
    var start_time: String = ""
    var security_code: String = ""
    var latitude=""
    var longitude=""
    private lateinit var studioLogo:String
    private lateinit var studioName:String
    private lateinit var address:String
    private lateinit var activityId:String
    private var scanViewModel: ScanViewModel?=null
    var position:Int=0
    companion object{
        var user_schedule_id: String = "0"
        lateinit var tvStatus:TextView
        lateinit var llScanHelp:LinearLayout
        lateinit var rlIcon:RelativeLayout
        lateinit var faIcon:FontAwesome
        lateinit var flScan: LinearLayout
        lateinit var vf: ViewfinderView
        lateinit var bv: BarcodeView

    }
    private fun hideBottomBars(newScannerBinding:ActivityFitpassScanQrCodeBinding?,fullScreen: Boolean) {
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR  or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        newScannerBinding?.rlDetail!!.fitsSystemWindows=true
    }
    fun hideTitleBar(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideTitleBar()
        setCordinate()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fitpass_scan_qr_code)
        user_schedule_id="0"
       // flScan=binding.barcodeScanner
        hideBottomBars(binding,true)

        setPadding()

        setStatusBarColor()
        var commonRepository= CommonRepository(this,this)
        var scanModelFactories=  ScanViewModelFactory(commonRepository,this,this,this)
        binding.lifecycleOwner=this
        scanViewModel= ViewModelProvider(this,scanModelFactories!!).get(ScanViewModel::class.java)
        binding.scanviewmodel=scanViewModel
        binding?.lifecycleOwner=this@FitpassScanQrCodeActivity
        init()
        capture = CaptureManager(this, binding.barcodeScanner)
        capture!!.initializeFromIntent(intent, savedInstanceState)
        binding.barcodeScanner.decodeContinuous(callback)
        onCLick()
    }
    fun setStatusBarColor() {
        var fitpassConfig = FitpassConfig.getInstance()
        if (!fitpassConfig!!.getHeaderColor().isNullOrEmpty()) {
            FitpassConfigUtil.setStatusBarColor(this, this, fitpassConfig.getHeaderColor())
        }
    }
    fun init() {
        isFlashAvail =getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
         faFlash = binding?.root?.findViewById<FontAwesome>(R.id.faFlash)!!
        var faScanFromGallery = binding?.root?.findViewById<FontAwesome>(R.id.faScanFromGallery)
        var faRefreshLocation = binding?.root?.findViewById<FontAwesome>(R.id.faRefreshLocation)
        llFlash = binding?.root?.findViewById<LinearLayout>(R.id.llFlash)!!
        rlScanGalley = binding?.root?.findViewById<RelativeLayout>(R.id.rlScanGalley)!!
        llRefreshLocation = binding?.root?.findViewById<LinearLayout>(R.id.llRefreshLocation)!!
        tvScanGallery = binding?.root?.findViewById<TextView>(R.id.tvScanGallery)!!
        flScan = binding?.root?.findViewById<LinearLayout>(R.id.flScan)!!
        vf = binding?.root?.findViewById<ViewfinderView>(R.id.zxing_viewfinder_view)!!
        bv = binding?.root?.findViewById<BarcodeView>(R.id.zxing_barcode_surface)!!
        Util.setFantIcon(faFlash!!, FontIconConstant.FLASH_ICON_OFF)
        Util.setFantIcon(faRefreshLocation!!, FontIconConstant.REFERESH_LOC_ICON)
        Util.setFantIcon(faScanFromGallery!!, FontIconConstant.GALLEY_ICON)
        Util.setFantIcon(binding.faCross!!, FontIconConstant.CLOSE_ICON)
        if(!isFlashAvail){
            llFlash.visibility= View.GONE
        }
       /* try {
            if(intent.extras!!.getString("padding").equals("true")){
                setPadding1()
                binding.rlScanList.visibility=View.VISIBLE
            }
        }catch (e:Exception){

        }*/
        scanViewModel!!.scanWorkoutList.observe(this,{
           if(scanViewModel!!.scanWorkoutList.value!!.size>0){
             /*  finish()
               var intent = Intent(this, FitpassScanQrCodeActivity::class.java)
               intent.putExtra("padding","true")
               startActivity(intent)*/
           }
        })

    }

    fun setPadding() {
        var fitpassConfig = FitpassConfig.getInstance()
        var paddingDp: Int = fitpassConfig!!.getPadding();
        var density = getResources().getDisplayMetrics().density.toFloat()
        var paddingPixel = (paddingDp * density).toInt();
        binding.rlHeader.setPadding(paddingPixel, 0, paddingPixel, 0);

    }
    fun setPadding1() {

        var paddingDp: Int = 150;
        var density = getResources().getDisplayMetrics().density.toFloat()
        var paddingPixel = (paddingDp * density).toInt();
        // FitpassScanQrCodeActivity.flScan.setPadding(0, 0, 0, paddingPixel);

        var params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, paddingPixel);
         binding.flScan.setLayoutParams(params);
        //  FitpassScanQrCodeActivity.bv.setLayoutParams(params);
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun onCLick() {
        binding.rlCross.setOnClickListener {
            onBackPressed()
        }
        llFlash!!.setOnClickListener {

            if (isFlash) {
                isFlash = false
                binding.barcodeScanner.setTorchOn()
                Util.setFantIcon(faFlash!!, FontIconConstant.FLASH_ICON_ON)
            } else {
                isFlash = true
                binding.barcodeScanner.setTorchOff()
                Util.setFantIcon(faFlash!!, FontIconConstant.FLASH_ICON_OFF)
            }
        }
        rlScanGalley!!.setOnClickListener {
            openGallery()
        }
        tvScanGallery!!.setOnClickListener {
            openGallery()
        }
        llRefreshLocation!!.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE
                )
            }else{
                Log.d("checkPermission","grant")
                FitpassLocationUtil.refreshLocation(this)
            }

        }
        binding.tvShowQrCode.setOnClickListener {
            setCordinate()
            var intent=Intent(this,FitpassShowQrCodeActivity::class.java)
            intent.putExtra("user_schedule_id",user_schedule_id)
            intent.putExtra("latitude",latitude)
            intent.putExtra("longitude",longitude)
            intent.putExtra("workout_name",workout_name)
            intent.putExtra("start_time",start_time)
            intent.putExtra("security_code",security_code)
            intent.putExtra("studio_logo",studioLogo)
            intent.putExtra("studio_name",studioName)
            intent.putExtra("address",address)
            intent.putExtra("activity_id",activityId)
            startActivity(intent)
        }
    }



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()
        checkPermission()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkPermission() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )
        } else {
            capture!!.onResume()

        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("onRequestResult","onRequestPermissionsResult")
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED
            ) {
                alert("We need to allow the camera permission to scan the QR Code. Do you want to allow it.")
            }
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("onRequestResult","PERMISSION_GRANTED")
                capture!!.onResume()
            } else {
            }
        }
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED
            ) {
                alert("We need to allow the location permission. Do you want to allow it.")
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_DENIED
            ) {
                alert("We need to allow the location permission. Do you want to allow it.")
            }
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("onRequestResult","PERMISSION_GRANTED")
               FitpassLocationUtil.refreshLocation(this)
            } else {
            }
        }
    }

    fun alert(msg:String) {
        val builder1 = AlertDialog.Builder(this)
        builder1.setMessage(msg)
        builder1.setCancelable(false)
        builder1.setPositiveButton(
            "Yes"
        ) { dialog, id ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        builder1.setNegativeButton(
            "No"
        ) { dialog, id -> onBackPressed() }
        val alert11 = builder1.create()
        alert11.show()
    }

    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            Log.d("barcoderesult", result.text + "...")
            /*Toast.makeText(
                this@FitpassScanQrCodeActivity,
                result.getText() + "..",
                Toast.LENGTH_SHORT
            ).show();*/
            getScanDetail(result.text)
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {
            // Log.d("barcoderesultpossible","possibleResultPoints");
            //Toast.makeText(CustomScannerActivity.this, "possibleResultPoints", Toast.LENGTH_SHORT).show();
        }
    }

    override fun onResume() {
        super.onResume()
        // capture.onResume();
    }

    override fun onPause() {
        super.onPause()
        capture!!.onPause()

    }

    override fun onDestroy() {
        super.onDestroy()
        capture!!.onDestroy()

    }

    private fun openGallery() {
        val pickIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"
        startActivityForResult(pickIntent, PICK_IMAGE_FROM_GALLERY)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("CheckForLocation", "onActivityResult");
        if (requestCode == PICK_IMAGE_FROM_GALLERY) {
            var mImageCaptureUri = data?.data
            if (mImageCaptureUri != null) {
                try {
                    val inputStream: InputStream? =
                        contentResolver.openInputStream(mImageCaptureUri)
                    var bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
                    if (bitmap == null) {
                        Log.e("TAG", "uri is not a bitmap," + mImageCaptureUri.toString())
                        return
                    }
                    val width: Int = bitmap.getWidth()
                    val height: Int = bitmap.getHeight()
                    val pixels = IntArray(width * height)
                    bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
                    bitmap.recycle()
                    bitmap = null

                    val source = RGBLuminanceSource(width, height, pixels)
                    val bBitmap = BinaryBitmap(HybridBinarizer(source))
                    val reader = MultiFormatReader()
                    val result = reader.decode(bBitmap)
                    Log.d("QRCode", result.getText() + "...")
                    getScanDetail(result.getText())



                } catch (e: NotFoundException) {
                    CustomToastView.errorToasMessage(
                        this,
                        this,
                        "No QR Code detected. Please try again"
                    )

                }

            }
        }

    }
    fun getScanDetail(qrcode:String){
        /* latitude="28.6139390"
         longitude="77.20906]11"*/
        setCordinate()
        scanViewModel?.let {
            var request= JSONObject()
            //request.put("qr_code_string","de-2")
            request.put("qr_code_string",qrcode)
            request.put("latitude",latitude)
            request.put("longitude",longitude)
            request.put("user_schedule_id",user_schedule_id)
            Log.d("Api_Request",request.toString())
            if(user_schedule_id.equals("0")){
                it.getScanData(request,false)
            }else{
                it.getScanData(request,true)
            }

        }
    }

    override fun onScanClick(workout: Workout,studioName:String,logo:String,address:String,position:Int) {
        user_schedule_id=workout.user_schedule_id
        workout_name=workout.workout_name
        start_time=workout.start_time.toString()
        security_code=workout.security_code.toString()
        activityId=workout.activity_id.toString()
        this.studioName=studioName
        this.studioLogo=logo
        this.address=address
        this.position=position
        binding.llShowQr.visibility=View.VISIBLE

    }
    fun setCordinate(){
        latitude= FitpassPrefrenceUtil.getStringPrefs(this,FitpassPrefrenceUtil.LATITUDE,"0.0").toString()
        longitude=FitpassPrefrenceUtil.getStringPrefs(this,FitpassPrefrenceUtil.LONGITUDE,"0.0").toString()
    }
}