package com.fitpass.libfitpass.base.utilities;

import android.content.Context;
import android.widget.Toast;

import com.fitpass.libfitpass.R;


/**
 * Created by rsawh on 13-Sep-17.
 */

public class FitpassToastUtil {
    public static void showToastShort(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToastLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

    }
    public static void noInternet(Context context) {

        FitpassToastUtil.showToastLong(context,context.getResources().getString(R.string.no_internet));
    }
}

