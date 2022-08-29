package com.fitpass.libfitpass.scanqrcode

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.fitpass.libfitpass.R
import com.fitpass.libfitpass.base.utilities.Util
import com.fitpass.libfitpass.databinding.ActivityShowQrCodeBinding
import com.fitpass.libfitpass.scanqrcode.models.Workout
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
    var startTime:Long=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_qr_code)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_qr_code)
        getData()
        binding.header1.tvHeader.setText(resources.getString(R.string.yourqrcode))
        binding.tvWorkoutname.setText(workoutName)

        val currenttime = System.currentTimeMillis() / 1000
        createQrCode(currenttime.toString())
        onClick()

    }
    fun onClick(){
        binding.header1.tvBack.setOnClickListener {
            onBackPressed()
        }
    }
    private fun getData()
    {
        /* intent.putExtra("user_schedule_id",user_schedule_id)
            intent.putExtra("latitude",latitude)
            intent.putExtra("longitude",longitude)
            intent.putExtra("longitude",longitude)
            intent.putExtra("workout_name",workout.workout_name)
            intent.putExtra("start_time",workout.start_time)*/
        securityCode = intent.getStringExtra("security_code")!!
        latitude = intent.getStringExtra("latitude")!!
        longitude = intent.getStringExtra("longitude")!!
        userScheduleId = intent.getStringExtra("user_schedule_id")!!
        latitude = intent.getStringExtra("latitude")!!
        longitude = intent.getStringExtra("longitude")!!
        workoutName = intent.getStringExtra("workout_name")!!
        startTime = intent.getStringExtra("start_time")!!.toLong()
    }

    private fun createQrCode(current_time:String)
    {
        val textToSend = StringBuilder()
        textToSend.append("security_code=$securityCode&user_schedule_id=$securityCode&qrcode_create_time=$current_time&latitude=$latitude&longitude=$longitude")
        val multiFormatWriter = MultiFormatWriter()
        try {
            securityCode = Util.encrptdata(textToSend.toString()).toString()
            val bitMatrix = multiFormatWriter.encode(securityCode, BarcodeFormat.QR_CODE, 600, 600)
            val barcodeEncoder: BarcodeEncoder = BarcodeEncoder()

            val bitmap: Bitmap = barcodeEncoder.createBitmap(bitMatrix)
            binding?.qrCodeImage?.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }

}