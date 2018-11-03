package net.diogoparreira.musicapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: MusicRecyclerViewAdapter
    private val musicList = mutableListOf<MusicEntryData>()
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    val LOCATION_PERMISSION_REQUEST_ID = 0
    private lateinit var locationManager: LocationManager
    private lateinit var geocoder: Geocoder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        adapter = MusicRecyclerViewAdapter(musicList)
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter

        swipeRefreshLayout = findViewById(R.id.swiperefresh)
        swipeRefreshLayout.setOnRefreshListener { updateMusicList() }

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        geocoder = Geocoder(MusicApplication.instance.applicationContext, Locale.getDefault())

        updateMusicList()

    }

    private fun updateMusicList() {
        swipeRefreshLayout.isRefreshing = true

        MusicApplication.instance.getMusicList(getCountry(), object: MusicListCallback {
            override fun onReceiveList(musicList: List<MusicEntryData>) {
                adapter.musicList = musicList
                adapter.notifyDataSetChanged()
                swipeRefreshLayout.isRefreshing = false
            }

        })
    }

    private fun getCountry(): String {
        if (checkLocationPermission()) {
            val location: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if(location != null) {
                val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if(addresses.isNotEmpty()) {
                    return addresses[0].countryName
                }
            }
        }
        Toast.makeText(MusicApplication.instance.applicationContext,"Unable to get location from GPS, using default device location",
            Toast.LENGTH_LONG).show()
        return Locale.getDefault().displayCountry
    }

    // In case it has no location permission it requests it
    private fun checkLocationPermission(): Boolean {

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder(this)
                    .setTitle(R.string.title_location_permission)
                    .setMessage(R.string.text_location_permission)
                    .setPositiveButton(R.string.ok) { _, i ->
                        ActivityCompat.requestPermissions(this@MainActivity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            LOCATION_PERMISSION_REQUEST_ID)
                    }
                    .create()
                    .show()
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_ID)
            }
            return false
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_ID -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        updateMusicList()
                    }
                }
                return
            }
        }
    }
}
