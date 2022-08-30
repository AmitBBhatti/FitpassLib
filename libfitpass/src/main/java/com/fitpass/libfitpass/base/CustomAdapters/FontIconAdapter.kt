package com.fitpass.libfitpass.base.CustomAdapters

import androidx.databinding.BindingAdapter
import com.fitpass.libfitpass.base.utilities.Util
import com.fitpass.libfitpass.fontcomponent.FontAwesome


@BindingAdapter("fonticon")
fun setfonticon(textView: FontAwesome?, value:String?)
{
    if(value!=null) {
        textView!!.setText(value.toInt(16).toChar().toString())
    }

}

@BindingAdapter("intfonticon")
fun setIntfonticon(textView: FontAwesome?, image:Int?)
{
    textView!!.setText(image!!.toChar().toString())

}

@BindingAdapter("workouticon")
fun setWorkouticon(textView: FontAwesome?, activityId:String?)
{
    if(activityId!=null) {
        textView!!.setText(Util.getWorkoutImage(activityId!!.toInt()).toInt(16).toChar().toString())
       // textView!!.setText("e941".toInt(16).toChar().toString())
    }

}

