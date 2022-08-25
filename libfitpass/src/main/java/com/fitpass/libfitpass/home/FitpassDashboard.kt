package com.fitpass.libfitpass.home


import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.fitpass.libfitpass.base.utilities.FitpassConfig
import com.fitpass.libfitpass.base.utilities.FitpassConfigUtil
import com.fitpass.libfitpass.home.adapters.FaqAdapter
import com.fitpass.libfitpass.home.adapters.UpcomingAdapter
import com.fitpass.libfitpass.R
import com.fitpass.libfitpass.databinding.ActivityFitPassDashboardBinding
import com.fitpass.libfitpass.home.http_client.CommonRepository
import com.fitpass.libfitpass.home.viewmodel.HomeViewModel
import com.fitpass.libfitpass.home.viewmodelfactory.HomeViewModelFactory
import org.json.JSONObject


class FitpassDashboard : AppCompatActivity() {
    lateinit var binding: ActivityFitPassDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fit_pass_dashboard)
        setHeaderColor()
        var fm=supportFragmentManager
        var ft:FragmentTransaction=fm.beginTransaction()
        ft.add(R.id.container,HomeFragment())
        ft.commit()
    }
    fun setHeaderColor() {
        var fitpassConfig = FitpassConfig.getInstance()
        if (!fitpassConfig!!.getHeaderColor().isNullOrEmpty()) {
            FitpassConfigUtil.setStatusBarColor(this, this, fitpassConfig.getHeaderColor())
            binding.tvHeader.setBackgroundColor(Color.parseColor(fitpassConfig.getHeaderColor()))
        }
    }




}