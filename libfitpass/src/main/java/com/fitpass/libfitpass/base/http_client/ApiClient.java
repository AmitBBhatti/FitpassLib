package com.fitpass.libfitpass.base.http_client;

import android.content.Context;
import android.util.Log;

import com.fitpass.libfitpass.base.dataencription.RandomKeyGenrator;
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
    static int parameterCount=0;
   /* public static  String getBaseUrl() {
        if (Const.APP_ENVIRONMENT == 1)
            return "https://api-fitpass.in/";
            //   return "https://api.fitpass.dev/";
        else
            return "https://api.fitpass.dev/";
    }*/

    public static Retrofit getApiClient(Context mContext,int paraCount ) {

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
            /*(
    [app_key] => lBnRJTc7CAJ8AixpqtilPB9FE2qU6EZg6YBkbugE
    [auth_key] => CadoXbi9bfU9WqnLLJM4FfXYo1bkMXbA5jTfB3tH
    [device_name] => DEVICE_NAME
    [device_os] => device_os
    [sdk_version] => sdk_version
    [dynamic_secrety_key] =>
    [user_id] =>
)*/
          JSONObject payloadObject=new JSONObject();
          try {
              payloadObject.put("app_key","lBnRJTc7CAJ8AixpqtilPB9FE2qU6EZg6YBkbugE");
              payloadObject.put("auth_key","CadoXbi9bfU9WqnLLJM4FfXYo1bkMXbA5jTfB3tH");
              payloadObject.put("device_name","DEVICE_NAME");
              payloadObject.put("device_os","device_os");
              payloadObject.put("sdk_version","sdk_version");
              payloadObject.put("dynamic_secrety_key","");
              payloadObject.put("user_id", FitpassPrefrenceUtil.INSTANCE.getStringPrefs(context,FitpassPrefrenceUtil.INSTANCE.getUSER_ID(),""));
          }catch (Exception e){

          }

            Log.d("payloadObjectdecrp", RandomKeyGenrator.encrptBodydata(payloadObject.toString())+"....");
            Log.d("payloadObject",payloadObject.toString());




            Request tokenRequest = request.newBuilder()
                    .addHeader("Content-Type", "application/json")
                    //.addHeader("Content-Type", "application/text")
                    //.addHeader("X-DEVICE-TYPE","web")
                    .addHeader("X-SDK-VERSION","1.0.0")
                   // .addHeader("X-FITPASS-PAYLOAD", "+Hgf1ov1gG+z7dxcNhPManXmspO6WfvvH1d2+3h0g5JdADhA7ANBLZxDdQGA8X8qpqsCvAIG9kUU0EdgoaHTSylH1n2e9zftCq0PcmxuHU9mmT41Ak6KJ3rc9r6ZotXAe1EDgaioyz/yg9ErJGSKXfq9zPk1QBAbO/mMXwalvgeTiIxtM2/MqyIn3YCaB5DfhcqFFuVRPpYmIdmMhFtZ2qfaE3b2htdbldEIqG0AFc7c6zaStLw0Ic1+NVjIFb+6Ru9WLlFkVi7rAqYFqy2LcwQ7H6AKZvI/yl/anpQzXgMeqViwrNkm9nYy+urbvM1A+lhBfO0XPiiuB/Y0O8zA+wd1LceXkCjtRC4E5CMzmW37QXikikE94nT65eVupGHkhZjdogUnjoiYjdWZbUQCw/AuF+U+4EaqjK9iUz2rEJM=").build();
                    .addHeader("X-FITPASS-PAYLOAD", RandomKeyGenrator.encrptBodydata(payloadObject.toString())).build();
            return chain
                    .proceed(tokenRequest);
        }
    }


}



