package com.fitpass.libfitpass.base.utilities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*

object FitpassLocationUtil: GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    val REQUEST_CHECK_SETTINGS = 0x1
    var currlat :Double= 0.0
    var currlog :Double= 0.0
    private var fusedLocationClient: FusedLocationProviderClient? = null
    lateinit var context:Context
    fun refreshLocation(context:Context) {
        this.context=context
        val googleApiClient = GoogleApiClient.Builder(context)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this).build()
        googleApiClient.connect()
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = (30 * 1000).toLong()
        locationRequest.fastestInterval = (5 * 1000).toLong()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true) //this is the key ingredient
        val result =
            LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback { result ->
            val status = result.status
            val state = result.locationSettingsStates
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS ->                         // All location settings are satisfied. The client can initialize location
                    // requests here.
                    //  setLoginScreen();
                    getLocation()
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {}
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
            }
        }
    }

    fun getLocation() {
        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            fusedLocationClient!!.getLastLocation()
                .addOnSuccessListener { location -> // Got last known location. In some rare situations this can be null.

                    if (location != null) {
                        currlat = location.latitude
                        currlog = location.longitude
                        Log.d("currlatservice", "$currlat...")
                        Log.d("currlogservice", "$currlog...")
                        FitpassPrefrenceUtil.setStringPrefs(context,FitpassPrefrenceUtil.LATITUDE,currlat.toString())
                        FitpassPrefrenceUtil.setStringPrefs(context,FitpassPrefrenceUtil.LONGITUDE,currlog.toString())
                    } else {
                        getLocationfromClient()
                    }
                }
        } catch (e: Exception) {
        }
    }

    fun getLocationfromClient() {
        try {

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            locationRequest = LocationRequest.create()
            locationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            locationRequest!!.setInterval((20 * 1000).toLong())
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    try {
                        Log.d("location_client", locationResult.toString() + "")
                        if (locationResult == null) {
                            // return;
                        }
                        for (location in locationResult.locations) {
                            if (location != null) {
                                mFusedLocationClient?.removeLocationUpdates(locationCallback!!)
                                currlat = location.latitude
                                currlog = location.longitude
                                Log.d(
                                    "location_from_client",
                                    location.toString() + ""
                                )
                                Log.d("currlatservice", "$currlat...")
                                Log.d("currlogservice", "$currlog...")
                                FitpassPrefrenceUtil.setStringPrefs(context,FitpassPrefrenceUtil.LATITUDE,currlat.toString())
                                FitpassPrefrenceUtil.setStringPrefs(context,FitpassPrefrenceUtil.LONGITUDE,currlog.toString())

                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            mFusedLocationClient!!.requestLocationUpdates(locationRequest!!,
                locationCallback as LocationCallback, null)
        } catch (e: Exception) {
        }
    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

}