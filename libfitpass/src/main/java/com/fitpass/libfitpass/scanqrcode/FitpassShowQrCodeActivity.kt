package com.fitpass.libfitpass.scanqrcode

import android.R.attr.bitmap
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.fitpass.libfitpass.R
import com.fitpass.libfitpass.base.constants.FontIconConstant
import com.fitpass.libfitpass.base.utilities.FitpassConfig
import com.fitpass.libfitpass.base.utilities.FitpassConfigUtil
import com.fitpass.libfitpass.base.utilities.Util
import com.fitpass.libfitpass.databinding.ActivityShowQrCodeBinding
import com.fitpass.libfitpass.scanqrcode.activitymodels.ActivityConfig
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
        var activitlist:ArrayList<ActivityConfig>
        activitlist= Util.getActivityConfigList(this)!!
        for(data in activitlist){
            if(data.activity_id.equals(activityId)) {
                binding.faWorkoutIcon.setTextColor(Color.parseColor(data.end_color))
            }
        }
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
            var manager=getSystemService(WINDOW_SERVICE) as WindowManager
            var display=manager.defaultDisplay
            var point=Point()
            display.getSize(point)
            var width=point.x
            var height=point.y
            var dimen:Int=0
            if(width>height){
                dimen=width
            }else {
                dimen=height
            }
            // dimen=dimen*3/4
            Log.d("dimen",dimen.toString())
            Log.d("llQrcode",getWindowManager().getDefaultDisplay().getWidth().toString())
            var screenWidth:Int=getWindowManager().getDefaultDisplay().getWidth().toString().toInt()
            var fitpassConfig = FitpassConfig.getInstance()
            var paddingDp: Int = fitpassConfig!!.getPadding();
            var density = getResources().getDisplayMetrics().density.toFloat()
            var paddingPixel = (paddingDp * density).toInt();
            var widthPixel = (dimen * density).toInt();
            Log.d("dimen1",widthPixel.toString())
            securityCode = Util.encrptDataWithSecretekey(this,textToSend.toString()).toString()
            // val bitMatrix = multiFormatWriter.encode(securityCode, BarcodeFormat.QR_CODE, screenWidth-paddingPixel ,screenWidth-paddingPixel)
            val bitMatrix = multiFormatWriter.encode(securityCode, BarcodeFormat.QR_CODE, 600 ,600)
            val barcodeEncoder: BarcodeEncoder = BarcodeEncoder()
            val bitmap: Bitmap = barcodeEncoder.createBitmap(bitMatrix)
            /* val qrgEncoder = QRGEncoder(inputValue, null, QRGContents.Type.TEXT, dimen)
             qrgEncoder.setColorBlack(Color.RED)
             qrgEncoder.setColorWhite(Color.BLUE)
             try {
                 // Getting QR-Code as Bitmap
                 bitmap = qrgEncoder.getBitmap()
                 // Setting Bitmap to ImageView
                 qrImage.setImageBitmap(bitmap)
             } catch (e: WriterException) {
                 Log.v(TAG, e.toString())
             }*/
            binding?.qrCodeImage?.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }
}