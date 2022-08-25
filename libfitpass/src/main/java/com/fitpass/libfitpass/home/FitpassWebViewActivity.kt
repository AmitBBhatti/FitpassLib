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
import com.fitpass.libfitpass.databinding.ActivityFitpassWebViewBinding

class FitpassWebViewActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding:ActivityFitpassWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_fitpass_web_view);
        setHeaderColor()
        var url=intent.extras!!.getString("url")
        Log.d("url",url+"..");
        binding.webview.setWebViewClient(WebViewClientDemo(this,this));
        binding.webview.setWebChromeClient(WebChromeClientDemo());
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.getSettings().setDomStorageEnabled(true);
        binding.webview.loadUrl(url!!);
        binding.tvBack.setOnClickListener(this);
    }
    fun setHeaderColor() {
        var fitpassConfig = FitpassConfig.getInstance()
        if (!fitpassConfig!!.getHeaderColor().isNullOrEmpty()) {
            FitpassConfigUtil.setStatusBarColor(this, this, fitpassConfig.getHeaderColor())
            binding.tvHeader.setBackgroundColor(Color.parseColor(fitpassConfig.getHeaderColor()))
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
            return true;
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            CustomLoader.showLoaderDialog(activity,context)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            CustomLoader.hideLoaderDialog(activity)
        }

    }
    private class WebChromeClientDemo : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
        }

    }
}