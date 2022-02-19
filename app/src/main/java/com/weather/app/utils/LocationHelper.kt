package com.weather.app.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class LocationHelper constructor(
    private val activity: Activity,
    private val listener: OnSuccessListener<Location>? = null,
    private val numOfUpdates: Int = 1,
    private val interval: Long = 1L,
    private val fastestInterval: Long = 1L,
    private val priority: Int = LocationRequest.PRIORITY_HIGH_ACCURACY,
    private val dialogTitle: String? = "Location Permission",
    private val dialogMessage: String? = "Please allow location permission",
) {
    private var locationCallback: LocationCallback?
    var fusedLocationClient: FusedLocationProviderClient
    private lateinit var location: Location
    private var isRunning = false
    private var isCallback = false

    init {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                location = locationResult.lastLocation
                if (!isCallback) {
                    listener!!.onSuccess(location)
                    isCallback = true
                }
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    }

    fun start() {
        Dexter.withContext(activity)
            .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            .withListener(permissionsListener)
            .withErrorListener { dexterError: DexterError ->
                Log.e("Error",
                    dexterError.toString())
            }
            .check()
    }

    fun stop() {
        if (locationCallback != null)
            fusedLocationClient.removeLocationUpdates(locationCallback!!)
        locationCallback = null
        isRunning = false
    }

    private var mLocationRequest: LocationRequest? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    private fun showEnableLocationSetting() {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        mLocationSettingsRequest = builder.build()
        val settingsClient = LocationServices.getSettingsClient(activity)
        settingsClient.checkLocationSettings(mLocationSettingsRequest!!)
    }

    private val permissionsListener: MultiplePermissionsListener =
        object : MultiplePermissionsListener {
            override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    //showEnableLocationSetting()

                    // Location permission check
                    if (ActivityCompat.checkSelfPermission(activity,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(activity,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener(activity,
                            OnSuccessListener {   // Checking Location Provider is enabled
                                val locationManager =
                                    activity.getSystemService(Activity.LOCATION_SERVICE) as LocationManager
                                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                }

                                // Checking if location received is not null then will be passed
                                /*if (location != null) {
                                    this@LocationHelper.location = location
                                    if (listener != null) listener!!.onSuccess(location)
                                    //return;
                                }*/

                                // if the location is null starting location request for one time to
                                // fetch one location update and will pass the location when fetched
                                mLocationRequest = LocationRequest.create()
                                mLocationRequest!!.setPriority(priority)
                                    .setInterval(interval).fastestInterval = fastestInterval
                                if (numOfUpdates > -1) mLocationRequest!!.numUpdates = numOfUpdates
                                if (ActivityCompat.checkSelfPermission(
                                        activity, Manifest.permission.ACCESS_FINE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.checkSelfPermission(
                                        activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED
                                ) {
                                    return@OnSuccessListener
                                }
                                fusedLocationClient.requestLocationUpdates(mLocationRequest!!,
                                    locationCallback!!, Looper.myLooper()!!)
                            })
                } else {
                    showEnableLocationSetting()
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                list: List<PermissionRequest>,
                permissionToken: PermissionToken,
            ) {
                AlertDialog.Builder(activity)
                    .setTitle(dialogTitle)
                    .setMessage(dialogMessage)
                    .setNegativeButton("Deny") { dialog: DialogInterface, _: Int ->
                        dialog.dismiss()
                        permissionToken.cancelPermissionRequest()
                    }
                    .setPositiveButton("Allow") { dialog: DialogInterface, _: Int ->
                        dialog.dismiss()
                        permissionToken.continuePermissionRequest()
                    }
                    .setOnDismissListener { permissionToken.cancelPermissionRequest() }
                    .show()
            }
        }
}