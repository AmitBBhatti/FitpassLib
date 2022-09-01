package com.fitpass.libfitpass.base.fitpasscomparators

import com.fitpass.libfitpass.home.models.SliderActivity

class FitpassLowtoHighComparator:Comparator<SliderActivity> {
    override fun compare(o1: SliderActivity?, o2: SliderActivity?): Int {
        if(o1!!.order_by!=null&&o2!!.order_by!=null){
            if(o1!!.order_by<o2!!.order_by){
                return -1;
            } else {
                return 1;
            }
        }else{
            return -1;
        }
    }
}