package com.fitpass.libfitpass.base.CustomAdapters

import android.util.Log
import android.view.View
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter

@BindingAdapter("setScanVisibility")
fun setcapTextData(cv: CardView?, value:Boolean)
{
    Log.d("setScanVisibility",value.toString())
    if(value){
        cv!!.visibility= View.VISIBLE
    }else{
        cv!!.visibility= View.GONE
    }

}