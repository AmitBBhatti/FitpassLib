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
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fitpass.libfitpass.R
import com.fitpass.libfitpass.scanqrcode.viewmodel.ScanViewModel
import com.fitpass.libfitpass.scanqrcode.viewmodelfactory.ScanViewModelFactory
import com.fitpass.libfitpass.base.constants.FontIconConstant
import com.fitpass.libfitpass.base.customview.CustomToastView
import com.fitpass.libfitpass.base.utilities.FitpassConfig
import com.fitpass.libfitpass.base.utilities.FitpassConfigUtil
import com.fitpass.libfitpass.base.utilities.Util
import com.fitpass.libfitpass.databinding.ActivityFitpassScanQrCodeBinding
import com.fitpass.libfitpass.fontcomponent.FontAwesome
import com.fitpass.libfitpass.home.http_client.CommonRepository
import com.fitpass.libfitpass.scanqrcode.listeners.FitpassScanListener
import com.fitpass.libfitpass.scanqrcode.models.Workout
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CaptureManager
import org.json.JSONObject
import java.io.InputStream


class FitpassScanQrCodeActivity : AppCompatActivity(),FitpassScanListener{
    var CAMERA_REQUEST_CODE: Int = 100
    var PICK_IMAGE_FROM_GALLERY: Int = 100
    private var capture: CaptureManager? = null
    lateinit var binding: ActivityFitpassScanQrCodeBinding
    lateinit var llFlash: LinearLayout
    lateinit var rlScanGalley: RelativeLayout
    lateinit var llRefreshLocation: LinearLayout
    lateinit var tvScanGallery: TextView
    var isFlash: Boolean = true
    var isFlashAvail: Boolean = true
    var user_schedule_id: String = "0"
    var workout_name: String = ""
    var start_time: String = ""
    var security_code: String = ""
    var latitude=""
    var longitude=""

    private var scanViewModel: ScanViewModel?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fitpass_scan_qr_code)
        init()

        setStatusBarColor()
        var commonRepository= CommonRepository(this,this)
        var scanModelFactories=  ScanViewModelFactory(commonRepository,this,this,this)
        binding.lifecycleOwner=this
        scanViewModel= ViewModelProvider(this,scanModelFactories!!).get(ScanViewModel::class.java)
        binding.scanviewmodel=scanViewModel
        binding?.lifecycleOwner=this@FitpassScanQrCodeActivity
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
        var faFlash = binding?.root?.findViewById<FontAwesome>(R.id.faFlash)
        var faScanFromGallery = binding?.root?.findViewById<FontAwesome>(R.id.faScanFromGallery)
        var faRefreshLocation = binding?.root?.findViewById<FontAwesome>(R.id.faRefreshLocation)
        llFlash = binding?.root?.findViewById<LinearLayout>(R.id.llFlash)!!
        rlScanGalley = binding?.root?.findViewById<RelativeLayout>(R.id.rlScanGalley)!!
        llRefreshLocation = binding?.root?.findViewById<LinearLayout>(R.id.llRefreshLocation)!!
        tvScanGallery = binding?.root?.findViewById<TextView>(R.id.tvScanGallery)!!
        Util.setFantIcon(faFlash!!, FontIconConstant.FLASH_ICON)
        Util.setFantIcon(faRefreshLocation!!, FontIconConstant.REFERESH_LOC_ICON)
        Util.setFantIcon(faScanFromGallery!!, FontIconConstant.GALLEY_ICON)
        if(!isFlashAvail){
            llFlash.visibility= View.GONE
        }
    }


    fun onCLick() {
        llFlash!!.setOnClickListener {
            if (isFlash) {
                isFlash = false
                binding.barcodeScanner.setTorchOn()
            } else {
                isFlash = true
                binding.barcodeScanner.setTorchOff()
            }
        }
        rlScanGalley!!.setOnClickListener {
            openGallery()
        }
        tvScanGallery!!.setOnClickListener {
            openGallery()
        }
        llRefreshLocation!!.setOnClickListener {

        }
        binding.tvShowQrCode.setOnClickListener {
            var intent=Intent(this,FitpassShowQrCodeActivity::class.java)
            intent.putExtra("user_schedule_id",user_schedule_id)
            intent.putExtra("latitude",latitude)
            intent.putExtra("longitude",longitude)
            intent.putExtra("longitude",longitude)
            intent.putExtra("workout_name",workout_name)
            intent.putExtra("start_time",start_time.toString())
            intent.putExtra("security_code",security_code.toString())
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
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED
            ) {
                alert()
            }
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                capture!!.onResume()
            } else {
            }
        }
    }

    fun alert() {
        val builder1 = AlertDialog.Builder(this)
        builder1.setMessage("We need to allow the camera permission to scan the QR Code. Do you want to allow it.")
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
         latitude="28.6139390"
         longitude="77.20906]11"
        scanViewModel?.let {
            var request= JSONObject()
            //request.put("qr_code_string","de-2")
            request.put("qr_code_string",qrcode)
            request.put("latitude",latitude)
            request.put("longitude",longitude)
            request.put("user_schedule_id",user_schedule_id)
            Log.d("Api_Request",request.toString())
            it.getScanData(request)
        }
    }

    override fun onScanClick(workout: Workout) {

        user_schedule_id=workout.user_schedule_id
        workout_name=workout.workout_name
        start_time=workout.start_time.toString()
        security_code=workout.security_code.toString()
        binding.tvShowQrCode.visibility=View.VISIBLE
    }
}