package com.fitpass.libfitpass.base.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.core.app.ActivityCompat;

/**
 * Created by rsawh on 14-Sep-17.
 */

public class FitpassDeviceInfoUtil {
    Context context;

    public FitpassDeviceInfoUtil(Context context) {
        this.context = context;
    }

    public static String getAppVersion(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        String version = pInfo.versionName;
        return version;
    }

    public static String getMobileOs(Context context) {
        String osVersion = android.os.Build.VERSION.RELEASE; // e.g. myVersion := "1.6"
        int sdkVersion = android.os.Build.VERSION.SDK_INT; // e.g. sdkVersion := 8;
        return osVersion;
    }

    public static String getMobileName(Context context) {
        String mobileName = android.os.Build.MODEL;
        return mobileName;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }


}
