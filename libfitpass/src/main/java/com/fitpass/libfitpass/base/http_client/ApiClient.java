package com.fitpass.libfitpass.base.http_client;

import android.content.Context;
import android.util.Log;

import com.fitpass.libfitpass.base.constants.ConfigConstants;
import com.fitpass.libfitpass.base.dataencription.RandomKeyGenrator;
import com.fitpass.libfitpass.base.utilities.FitpassDeviceInfoUtil;
import com.fitpass.libfitpass.base.utilities.FitpassPrefrenceUtil;
import com.fitpass.libfitpass.base.utilities.Util;
import com.fitpass.libfitpass.home.http_client.ApiConstants;

import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    static Context context;
    //private static DataStore ds = DataStore.getInstance();
    static int parameterCount = 0;
   /* public static  String getBaseUrl() {
        if (Const.APP_ENVIRONMENT == 1)
            return "https://api-fitpass.in/";
            //   return "https://api.fitpass.dev/";
        else
            return "https://api.fitpass.dev/";
    }*/

    public static Retrofit getApiClient(Context mContext, int paraCount) {

        context = mContext;
        parameterCount = paraCount;
        Retrofit retrofit = null;


        if (retrofit == null) {
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            HashMap<String, String> headers = new HashMap<>();
            //  CustomRequestInterceptor customRequestInterceptor=new CustomRequestInterceptor(headers);

            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().addInterceptor(new HeaderIntercepter());
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(new NetworkConnectionInterceptor(mContext));
            clientBuilder.addInterceptor(loggingInterceptor);
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiConstants.BASET_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(clientBuilder.build())
                    .build();

        }
        return retrofit;
    }


    public static class HeaderIntercepter implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            JSONObject payloadObject = new JSONObject();
            String dynamicSecretKey = RandomKeyGenrator.getRandomKey();
            try {
                if (FitpassPrefrenceUtil.INSTANCE.getStringPrefs(context, FitpassPrefrenceUtil.INSTANCE.getAPP_KEY(), "").isEmpty()) {
                    payloadObject.put("app_key", ConfigConstants.Companion.getAPP_KEY());
                } else {
                    payloadObject.put("app_key", FitpassPrefrenceUtil.INSTANCE.getStringPrefs(context, FitpassPrefrenceUtil.INSTANCE.getAPP_KEY(), ""));
                }
                payloadObject.put("auth_key", ConfigConstants.Companion.getAUTH_KEY());
                payloadObject.put("device_name", FitpassDeviceInfoUtil.getMobileName(context));
                payloadObject.put("device_os", FitpassDeviceInfoUtil.getMobileOs(context));
                payloadObject.put("sdk_version", ConfigConstants.Companion.getSDK_VERSION());
                payloadObject.put("dynamic_secrety_key", dynamicSecretKey);
                payloadObject.put("user_id", FitpassPrefrenceUtil.INSTANCE.getStringPrefs(context, FitpassPrefrenceUtil.INSTANCE.getUSER_ID(), ""));
            } catch (Exception e) {

            }
            Log.d("payloadObject", payloadObject.toString());
            String encripteddata=RandomKeyGenrator.encrptBodydata(payloadObject.toString());
            Log.d("payloadObjectdecrp",  encripteddata);
            Request tokenRequest = request.newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("X-SDK-VERSION", ConfigConstants.Companion.getSDK_VERSION())
                    .addHeader("X-FITPASS-PAYLOAD", encripteddata).build();
            return chain
                    .proceed(tokenRequest);
        }
    }


}



