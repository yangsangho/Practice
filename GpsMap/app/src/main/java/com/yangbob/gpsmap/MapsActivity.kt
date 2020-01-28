package com.yangbob.gpsmap

import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback
{

    private lateinit var mMap: GoogleMap
    private val polylineOptions = PolylineOptions().width(5f).color(Color.RED)

    private val REQUEST_ACCESS_FINE_LOCATION = 1000

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: MyLocationCallBack

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationInit()
    }

    override fun onMapReady(googleMap: GoogleMap)
    {
        mMap = googleMap

        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun locationInit()
    {
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        locationCallback = MyLocationCallBack()

        locationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
            fastestInterval = 5000
        }
    }

    override fun onResume()
    {
        super.onResume()
        permissionCheck(cancel = { showPermissionInfoDialog() }, ok = { addLocationListener() })
    }

    override fun onPause()
    {
        super.onPause()
        removeLocationListener()
    }

    private fun addLocationListener()
    {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun removeLocationListener()
    {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    inner class MyLocationCallBack : LocationCallback()
    {
        override fun onLocationResult(locationResult: LocationResult?)
        {
            super.onLocationResult(locationResult)

            val location = locationResult?.lastLocation
            location?.run {
                val latLng = LatLng(latitude, longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
                Log.d("TEST", "위도 : $latitude, 경도 : $longitude")

                polylineOptions.add(latLng)
                mMap.addPolyline(polylineOptions)
            }
        }
    }

    private fun permissionCheck(cancel: () -> Unit, ok: () -> Unit)
    {
        if (ContextCompat.checkSelfPermission(this,
                                              Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                                                    Manifest.permission.ACCESS_FINE_LOCATION)) cancel()
            else ActivityCompat.requestPermissions(this,
                                                   arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                                   REQUEST_ACCESS_FINE_LOCATION)
        }
        else ok()
    }

    private fun showPermissionInfoDialog()
    {
        AlertDialog.Builder(this).setMessage("현재 위치 정보를 얻으려면 권한이 필요합니다.")
            .setPositiveButton("허용") { _, _ ->
                ActivityCompat.requestPermissions(this,
                                                  arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                                  REQUEST_ACCESS_FINE_LOCATION)
            }.setNegativeButton("거부") { _, _ -> }.create().show()
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode)
        {
            REQUEST_ACCESS_FINE_LOCATION ->
            {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) addLocationListener()
                else Toast.makeText(this, "권한 거부됨.", Toast.LENGTH_LONG).show()
                return
            }
        }
    }
}
