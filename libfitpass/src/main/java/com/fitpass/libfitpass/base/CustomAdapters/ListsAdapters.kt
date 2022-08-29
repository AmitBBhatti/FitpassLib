package com.fitpass.libfitpass.base.CustomAdapters

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.fitpass.libfitpass.scanqrcode.Adapers.FitpassScanListAdapter
import com.fitpass.libfitpass.scanqrcode.models.Workout
import com.fitpass.libfitpass.scanqrcode.viewmodel.ScanViewModel
import com.fitpass.libfitpass.home.adapters.FaqAdapter
import com.fitpass.libfitpass.home.adapters.Macro_Adapter
import com.fitpass.libfitpass.home.adapters.MenuAdapter
import com.fitpass.libfitpass.home.adapters.UpcomingAdapter
import com.fitpass.libfitpass.home.models.List
import com.fitpass.libfitpass.home.models.MacrosDetail
import com.fitpass.libfitpass.home.models.Product
import com.fitpass.libfitpass.home.models.SliderActivity
import com.fitpass.libfitpass.home.viewmodel.HomeViewModel
import com.fitpass.libfitpass.scanqrcode.listeners.FitpassScanListener


@BindingAdapter("menuItems","menuViewModel")
fun bindproductItemsActivity(recyclerView: RecyclerView, list: MutableLiveData<ArrayList<Product>>?, homeViewModel: HomeViewModel) {
    val adapter = getProductItemsAdapter(recyclerView,homeViewModel)
    if (list!=null&&list!!.value!=null)
    {
        adapter.updateItems(list)
    }
}
private fun getProductItemsAdapter(recyclerView: RecyclerView, homeViewModel: HomeViewModel): MenuAdapter {
    return if (recyclerView.adapter != null) {
        recyclerView.adapter as MenuAdapter
    } else {
        val bindableRecyclerAdapter = MenuAdapter(homeViewModel)
        recyclerView.layoutManager= LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = bindableRecyclerAdapter
        bindableRecyclerAdapter
    }
}

@BindingAdapter("faqItems","faqViewModel")
fun bindFaqItemsActivity(recyclerView: RecyclerView, list: MutableLiveData<ArrayList<List>>?, homeViewModel: HomeViewModel) {
    val adapter = getFaqItemsAdapter(recyclerView,homeViewModel)
    if (list!=null&&list!!.value!=null)
    {
        adapter.updateItems(list)
    }
}
private fun getFaqItemsAdapter(recyclerView: RecyclerView, homeViewModel: HomeViewModel): FaqAdapter {
    return if (recyclerView.adapter != null) {
        recyclerView.adapter as FaqAdapter
    } else {
        val bindableRecyclerAdapter = FaqAdapter(homeViewModel)
        recyclerView.layoutManager= LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = bindableRecyclerAdapter
        bindableRecyclerAdapter
    }
}

@BindingAdapter("upcomingItems","upcomingviewModel")
fun bindUpcomingIItemsActivity(vp: ViewPager, list: MutableLiveData<ArrayList<SliderActivity>>?, homeViewModel: HomeViewModel) {
    val adapter = getUpcomingIItemsAdapter(vp,homeViewModel)
    if (list!=null&&list!!.value!=null)
    {
        adapter.updateItems(list)

    }
}
private fun getUpcomingIItemsAdapter(vp: ViewPager, homeViewModel: HomeViewModel): UpcomingAdapter {
    return if (vp.adapter != null) {
        vp.adapter as UpcomingAdapter
    } else {
        val bindableRecyclerAdapter = UpcomingAdapter(vp.context,homeViewModel)
        vp.adapter = bindableRecyclerAdapter
        bindableRecyclerAdapter
    }
}

@BindingAdapter("macroItems","macroViewmodel")
fun bindMacroItemsActivity(recyclerView: RecyclerView, list: MutableLiveData<ArrayList<MacrosDetail>>?, homeViewModel: HomeViewModel) {
    val adapter = getMaroItemsAdapter(recyclerView,homeViewModel)
    if (list!=null&&list!!.value!=null)
    {
        adapter.updateItems(list)
    }
}
private fun getMaroItemsAdapter(recyclerView: RecyclerView, homeViewModel: HomeViewModel): Macro_Adapter {
    return if (recyclerView.adapter != null) {
        recyclerView.adapter as Macro_Adapter
    } else {
        val bindableRecyclerAdapter = Macro_Adapter(homeViewModel)
        recyclerView.layoutManager=  GridLayoutManager(recyclerView.context,2)
        recyclerView.adapter = bindableRecyclerAdapter
        bindableRecyclerAdapter
    }
}

@BindingAdapter("scanItems","scanViewmodel","fitpassScanListener")
fun bindScanItemsActivity(recyclerView: RecyclerView, list: MutableLiveData<ArrayList<Workout>>?, scanViewModel: ScanViewModel, fitpassScanListener: FitpassScanListener) {
    val adapter = getScanItemsAdapter(recyclerView,scanViewModel,fitpassScanListener)
    if (list!=null&&list!!.value!=null)
    {
        adapter.updateItems(list)
    }
}
private fun getScanItemsAdapter(recyclerView: RecyclerView, scanViewModel: ScanViewModel, fitpassScanListener:FitpassScanListener): FitpassScanListAdapter {
    return if (recyclerView.adapter != null) {
        recyclerView.adapter as FitpassScanListAdapter
    } else {
        val bindableRecyclerAdapter = FitpassScanListAdapter(scanViewModel,fitpassScanListener)
        recyclerView.layoutManager= LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = bindableRecyclerAdapter
        bindableRecyclerAdapter
    }
}