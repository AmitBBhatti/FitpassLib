package com.fitpass.libfitpass.home.viewmodelfactory

import android.app.Activity
import android.content.Context
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.fitpass.libfitpass.home.http_client.CommonRepository
import com.fitpass.libfitpass.home.listeners.FitpassHomeListener
import com.fitpass.libfitpass.home.viewmodel.HomeViewModel

class HomeViewModelFactory (val commonRepository: CommonRepository, val context: Context, val activity: Activity,val vpUpcomming:ViewPager,val lldot:LinearLayout,val fitpassHomeListener: FitpassHomeListener) :
    ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(commonRepository,context,activity,vpUpcomming,lldot,fitpassHomeListener) as T
        }
        throw IllegalArgumentException("Home viewModel argument exception")
    }

}