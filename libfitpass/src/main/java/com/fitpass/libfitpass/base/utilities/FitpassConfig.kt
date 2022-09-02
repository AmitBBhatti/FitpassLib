package com.fitpass.libfitpass.base.utilities

class FitpassConfig {
    var headerColor:String="#625986"
    var headerFontColor:String="#FFFFFF"
    var headerTitle:String="FITPASS"
    var padding:Int=16
    @JvmName("setHeaderFontColor1")
    fun setHeaderFontColor(headerFontColor:String){
        if(Util.isHexColor(headerFontColor)) {
            this.headerFontColor = headerFontColor
        }

    }
    @JvmName("getHeaderFontColor1")
    fun getHeaderFontColor(): String{
        return headerFontColor
    }

    @JvmName("setheaderColor1")
    fun setHeaderColor(headerColor:String){
        if(Util.isHexColor(headerColor)){
            this.headerColor=headerColor
        }


    }
    @JvmName("getheaderColor1")
    fun getHeaderColor(): String{
        return headerColor
    }
    @JvmName("setHeaderTitle1")
    fun setHeaderTitle(headerTitle:String){
        if(!headerTitle.isNullOrEmpty()){
            this.headerTitle=headerTitle
        }
    }
    @JvmName("getHeaderTitle1")
    fun getHeaderTitle(): String{
        return headerTitle
    }
    @JvmName("setPadding1")
    fun setPadding(padding:Int){
        if(padding>=5&&padding<=30){
            this.padding=padding
        }

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