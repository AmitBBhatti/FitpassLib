package com.fitpass.libfitpass.scanqrcode.viewmodelfactory

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fitpass.libfitpass.scanqrcode.viewmodel.ScanViewModel
import com.fitpass.libfitpass.home.http_client.CommonRepository
import com.fitpass.libfitpass.scanqrcode.listeners.FitpassScanListener

class ScanViewModelFactory(val commonRepository: CommonRepository, val context: Context, val activity: Activity,val fitpassScanListener: FitpassScanListener) :
    ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScanViewModel::class.java)) {
            return ScanViewModel(commonRepository,context,activity,fitpassScanListener) as T
        }
        throw IllegalArgumentException("Home viewModel argument exception")
    }

}