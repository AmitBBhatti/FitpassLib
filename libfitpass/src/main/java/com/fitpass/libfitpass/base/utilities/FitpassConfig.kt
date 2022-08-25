package com.fitpass.libfitpass.base.utilities

class FitpassConfig {
    var headerColor:String=""
    var padding:Int=16
    @JvmName("setheaderColor1")
    fun setHeaderColor(headerColor:String){
        this.headerColor=headerColor

    }
    @JvmName("getheaderColor1")
    fun getHeaderColor(): String{
        return headerColor
    }
    @JvmName("setPadding1")
    fun setPadding(padding:Int){
        this.padding=padding
    }
    @JvmName("getPadding1")
    fun getPadding():Int{
        return padding
    }
    companion object {
        private var INSTANCE: FitpassConfig? = null
        fun getInstance(): FitpassConfig? {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = FitpassConfig()
                    }
                }
            }
            return INSTANCE
        }
    }



}