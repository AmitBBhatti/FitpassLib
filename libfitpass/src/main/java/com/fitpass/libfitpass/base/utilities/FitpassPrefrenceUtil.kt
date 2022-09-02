package com.fitpass.libfitpass.base.utilities

import android.content.Context
import android.content.SharedPreferences




object FitpassPrefrenceUtil {
    const val PREFERENCE_NAME = "fitpasspref"
    var USER_ID = "userid"
    var SECRET_KEY = "secret_key"
    var LATITUDE = "latitude"
    var LONGITUDE = "longitude"
    var ISLOAD_DASHBOARD_DATA = "isload"
    private fun getSharedPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    fun setBooleanPrefs(context: Context, param: String?, value: Boolean) {
        getSharedPrefs(context).edit().putBoolean(param, value).commit()
    }

    fun getBooleanPrefs(context: Context, param: String?, defaultvalue: Boolean): Boolean {
        return getSharedPrefs(context).getBoolean(param, defaultvalue)
    }


    fun setStringPrefs(context: Context, param: String?, value: String?) {
        getSharedPrefs(context).edit().putString(param, value).commit()
    }

    fun getStringPrefs(context: Context, param: String?, defaultvalue: String?): String? {
        return getSharedPrefs(context).getString(param, defaultvalue)
    }


    fun setIntPrefs(context: Context, param: String?, value: Int) {
        getSharedPrefs(context).edit().putInt(param, value).commit()
    }

    fun getIntPrefs(context: Context, param: String?, defaultvalue: Int): Int {
        return getSharedPrefs(context).getInt(param, defaultvalue)
    }

    fun clearpref(context: Context) {
        getSharedPrefs(context).edit().clear().commit()
    }
}