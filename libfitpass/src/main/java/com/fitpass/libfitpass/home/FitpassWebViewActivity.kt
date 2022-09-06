package com.fitpass.libfitpass.home

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fitpass.libfitpass.R
import com.fitpass.libfitpass.base.http_client.CustomLoader
import com.fitpass.libfitpass.base.utilities.FitpassConfig
import com.fitpass.libfitpass.base.utilities.FitpassConfigUtil
import com.fitpass.libfitpass.base.utilities.FitpassPrefrenceUtil
import com.fitpass.libfitpass.databinding.ActivityFitpassWebViewBinding
import java.lang.Exception

class FitpassWebViewActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding:ActivityFitpassWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_fitpass_web_view);
        setHeader()
        var url=intent.extras!!.getString("url")
        var show_header=intent.extras!!.getBoolean("show_header")
         if(show_header){
             binding.rlHeader.visibility=View.VISIBLE
         }else{
             binding.rlHeader.visibility=View.GONE
         }
        binding.webview.setWebViewClient(WebViewClientDemo(this,this));
        binding.webview.setWebChromeClient(WebChromeClientDemo());
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.getSettings().setDomStorageEnabled(true);
        binding.webview.loadUrl(url!!);
        binding.tvBack.setOnClickListener(this);
    }

    fun setHeader() {
        var fitpassConfig = FitpassConfig.getInstance()
        if (!fitpassConfig!!.getHeaderColor().isNullOrEmpty()) {
            FitpassConfigUtil.setStatusBarColor(this, this, fitpassConfig.getHeaderColor())
           try {
               binding.tvHeader.setBackgroundColor(Color.parseColor(fitpassConfig.getHeaderColor()))
           }catch (e:Exception){

           }
        }
        if (!fitpassConfig!!.getHeaderTitle().isNullOrEmpty()) {
            binding.tvHeader.setText(fitpassConfig!!.getHeaderTitle())
        }
        if (!fitpassConfig!!.getHeaderFontColor().isNullOrEmpty()) {
            try {
                binding.tvHeader.setTextColor(Color.parseColor(fitpassConfig!!.getHeaderFontColor()))
            }catch (e:Exception){

            }
        }
    }
   override fun onClick(view: View) {
        when (view.getId()) {
            R.id.tvBack -> onBackPressed()
        }
    }
    override fun onBackPressed() {
        if (binding.webview.canGoBack()) {
            binding.webview.goBack()
        } else {
            super.onBackPressed()
        }
    }
    private class WebViewClientDemo(context1: Context,activity1: Activity) : WebViewClient() {
         var context:Context=context1
         var activity:Activity=activity1
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view!!.loadUrl(url!!);
            Log.d("url1",url+"..");
            return true;
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
           // CustomLoader.showLoaderDialog(activity,context)
            Log.d("url2",url+"..");
            if(url!!.contains("events.fitpass.dev")){
                FitpassPrefrenceUtil.setBooleanPrefs(context,FitpassPrefrenceUtil.ISLOAD_DASHBOARD_DATA,true)
                activity.finish()
            }
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            Log.d("url3",url+"..");
           // CustomLoader.hideLoaderDialog(activity)
            if(url!!.contains("events.fitpass.dev")){
                activity.finish()
            }
        }
    }
    private class WebChromeClientDemo : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
        }
    }
}