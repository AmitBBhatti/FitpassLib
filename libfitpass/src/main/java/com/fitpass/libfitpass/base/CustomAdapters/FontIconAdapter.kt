package com.fitpass.libfitpass.base.CustomAdapters

import androidx.databinding.BindingAdapter
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
      //  textView!!.setText(getWorkoutImage("activityId!!".toInt()).toInt(16).toChar().toString())
        textView!!.setText("e941".toInt(16).toChar().toString())
    }

}
private fun getWorkoutImage(imageId: Int): String {
    return when (imageId) {
        1 -> "e901"
        2 -> "e903"
        3 -> "e906"
        4 -> "e90b"
        5 -> "e90c"
        6 -> "e90f"
        7 -> "e91f"
        8 -> "e921"
        9 -> "e92b"
        10 -> "e92f"
        11 -> "e937"
        12 -> "e938"
        13 -> "e939"
        14 -> "e941"
        15 -> "e942"
        16 -> "e96f"
        17 -> "e96d"
        18 -> "e96c"
        19 -> "e96e"
        else -> "e91f"
    }
}