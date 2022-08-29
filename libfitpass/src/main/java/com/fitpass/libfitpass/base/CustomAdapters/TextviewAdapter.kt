package com.fitpass.libfitpass.base.CustomAdapters

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.fitpass.libfitpass.base.utilities.Util
import com.fitpass.libfitpass.fontcomponent.FontAwesome

@BindingAdapter("concatvalue","value")
fun setConcatTextValue(textView: TextView?, concatvalue:String?,value:String?)
{
    if(!value.isNullOrEmpty()) {
        textView!!.setText(concatvalue+value)
    }else{
        textView!!.setText("")
    }

}

@BindingAdapter("mildate")
fun setmildate(textView: TextView?, date:Int?)
{
    if(date!=null){
        if(date!=0) {
            textView!!.setText(Util.convertMiliesToDD_MM_HH_MMDateTime2(date.toString(), true))
        }
    }

}
@BindingAdapter("date","outputformat")
fun setdate(textView: TextView?, date:Int?,outputformat:String)
{
    if(date!=null){
        if(date!=0){
            textView!!.setText(Util.convertMiliesToDate(date.toString(),true,outputformat))
        }


    }

}

@BindingAdapter("textdata")
fun setTextData(textView: TextView?,value:String?)
{
    if(value!=null) {
        textView!!.setText(value)
    }else{
        textView!!.setText("")
    }

}
@BindingAdapter("textvalue","defaultvalue","concatvalue1")
fun setTextData(textView: TextView?,value:String?,default: String,concatvalue1: String)
{
    if(!value.isNullOrEmpty()) {
        if(!value.equals("0")){
            textView!!.setText(value+concatvalue1)
        }else{
            textView!!.setText(default+concatvalue1)
        }
    }else{
        textView!!.setText(default+concatvalue1)
    }

}
@BindingAdapter("captextdata")
fun setcapTextData(textView: TextView?,value:String?)
{
    if(value!=null) {
        textView!!.setText(Util.captalizeString(value))
    }else{
        textView!!.setText("")
    }

}
