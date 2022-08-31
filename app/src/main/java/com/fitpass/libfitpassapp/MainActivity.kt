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
        fitpassConfig!!.setHeaderFontColor("#ffffff")
        fitpassConfig!!.setHeaderTitle("FITPASS")
        fitpassConfig!!.setPadding(16)
        var intent=Intent(this,FitpassDashboard::class.java)
        startActivity(intent)
    }
    fun click(view: View) {
        var intent=Intent(this,FitpassDashboard::class.java)
        startActivity(intent)
    }
}