package com.fitpass.libfitpassapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.fitpass.libfitpass.base.utilities.FitpassConfig
import com.fitpass.libfitpass.home.FitpassDashboard


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var fitpassConfig= FitpassConfig.getInstance()
        fitpassConfig!!.setHeaderColor("#625986")
        fitpassConfig!!.setHeaderFontColor("#FFFFFF")
        fitpassConfig!!.setHeaderTitle("FITPASS")
        fitpassConfig!!.setPadding(16)
        var intent=Intent(this,FitpassDashboard::class.java)
        intent.putExtra("vendor_id", "183")
        intent.putExtra("policy_number", "11111111113111")
        intent.putExtra("member_id", "15143512435")
        startActivity(intent)
    }
    fun click(view: View) {
        var intent=Intent(this,FitpassDashboard::class.java)
        intent.putExtra("vendor_id", "183")
        intent.putExtra("policy_number", "11111111113111")
        intent.putExtra("member_id", "15143512435")
        startActivity(intent)
    }
}