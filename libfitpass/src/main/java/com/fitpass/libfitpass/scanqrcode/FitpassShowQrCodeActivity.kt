package com.fitpass.libfitpass.scanqrcode

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.fitpass.libfitpass.R
import com.fitpass.libfitpass.base.constants.FontIconConstant
import com.fitpass.libfitpass.base.utilities.FitpassConfig
import com.fitpass.libfitpass.base.utilities.FitpassConfigUtil
import com.fitpass.libfitpass.base.utilities.Util
import com.fitpass.libfitpass.databinding.ActivityShowQrCodeBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder


class FitpassShowQrCodeActivity : AppCompatActivity() {
     lateinit var binding:ActivityShowQrCodeBinding
    private lateinit var strEncrypted:String
    private lateinit var securityCode:String
    private lateinit var latitude:String
    private lateinit var longitude:String
    private lateinit var userScheduleId:String
    private lateinit var workoutName:String

    private lateinit var studioLogo:String
    private lateinit var studioName:String
    private lateinit var address:String
    private lateinit var activityId:String
    var startTime:Long=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_qr_code)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_qr_code)
        binding.header1.tvHeader.setText(resources.getString(R.string.yourqrcode))
        Util.setFantIcon(binding.header1.faBack,FontIconConstant.ARROW_BACK_ICON)
        Util.setFantIcon(binding.faLogo,FontIconConstant.LOGO_ICON)
        getData()
        val currenttime = System.currentTimeMillis() / 1000
        createQrCode(currenttime.toString())
        onClick()
        setStatusBarColor()
        setPadding()

    }

    fun setPadding() {
        var fitpassConfig = FitpassConfig.getInstance()
        var paddingDp: Int = fitpassConfig!!.getPadding();
        var density = getResources().getDisplayMetrics().density.toFloat()
        var paddingPixel = (paddingDp * density).toInt();
        binding.llDetail.setPadding(paddingPixel, 0, paddingPixel, 0);
        binding.header1.rlHeader.setPadding(paddingPixel, 0, paddingPixel, 0);

    }
    fun setStatusBarColor() {
        var fitpassConfig = FitpassConfig.getInstance()
        if (!fitpassConfig!!.getHeaderColor().isNullOrEmpty()) {
            FitpassConfigUtil.setStatusBarColor(this, this, fitpassConfig.getHeaderColor())
        }
    }
    fun onClick(){
        binding.header1.faBack.setOnClickListener {
            onBackPressed()
        }
    }
    private fun getData()
    {
        securityCode = intent.getStringExtra("security_code")!!
        latitude = intent.getStringExtra("latitude")!!
        longitude = intent.getStringExtra("longitude")!!
        userScheduleId = intent.getStringExtra("user_schedule_id")!!
        workoutName = intent.getStringExtra("workout_name")!!
        studioLogo = intent.getStringExtra("studio_logo")!!
        studioName = intent.getStringExtra("studio_name")!!
        address = intent.getStringExtra("address")!!
        startTime = intent.getStringExtra("start_time")!!.toLong()
        activityId = intent.getStringExtra("activity_id")!!
        binding.tvstudioName.setText(studioName)
        binding.tvAddress.setText(address)
        Glide.with(this).load(studioLogo).placeholder(resources.getDrawable(R.drawable.placeholder)).into(binding.ivLogo)
        binding.tvWorkoutname.setText(workoutName)
        binding.tvWorkoutdate.setText(Util.convertMiliesToDate(startTime.toString(),true,"dd MMM, hh:mm aa"))
        if(!activityId.isNullOrEmpty()){
            binding.faWorkoutIcon!!.setText(Util.getWorkoutImage(activityId!!.toInt()).toInt(16).toChar().toString())
        }
    }

    private fun createQrCode(current_time:String)
    {
        val textToSend = StringBuilder()
        textToSend.append("security_code=$securityCode&user_schedule_id=$securityCode&qrcode_create_time=$current_time&latitude=$latitude&longitude=$longitude")
        val multiFormatWriter = MultiFormatWriter()
        try {

            Log.d("llQrcode",getWindowManager().getDefaultDisplay().getWidth().toString())
            var screenWidth:Int=getWindowManager().getDefaultDisplay().getWidth().toString().toInt()
            var fitpassConfig = FitpassConfig.getInstance()
            var paddingDp: Int = fitpassConfig!!.getPadding();
            var density = getResources().getDisplayMetrics().density.toFloat()
            var paddingPixel = (paddingDp * density).toInt();
            securityCode = Util.encrptDataWithSecretekey(this,textToSend.toString()).toString()
            //var width=
            val bitMatrix = multiFormatWriter.encode(securityCode, BarcodeFormat.QR_CODE, screenWidth-paddingPixel ,screenWidth-paddingPixel)
            val barcodeEncoder: BarcodeEncoder = BarcodeEncoder()
            val bitmap: Bitmap = barcodeEncoder.createBitmap(bitMatrix)
            binding?.qrCodeImage?.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }
}