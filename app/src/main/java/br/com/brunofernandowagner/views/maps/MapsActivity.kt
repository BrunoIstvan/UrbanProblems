package br.com.brunofernandowagner.views.maps

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.brunofernandowagner.R
import br.com.brunofernandowagner.models.Problem

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var problems: ArrayList<Problem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        if(intent.hasExtra("PROBLEMS")) {
            problems = intent.getParcelableArrayListExtra("PROBLEMS")
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the userLiveData will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the userLiveData has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if(::problems.isInitialized) {

            var lastPoint: LatLng? = null
            for(prob in problems) {

                if(prob.latitude != null && prob.latitude != 0.0 &&
                   prob.longitude != null && prob.longitude != 0.0) {
                    // Add a marker in Sydney and move the camera
                    val point = LatLng(prob.latitude!!, prob.longitude!!)
                    mMap.addMarker(MarkerOptions().position(point).title(prob.title))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(point))
                    lastPoint = point
                }

            }

            lastPoint?.let {
                val circle = CircleOptions()
                circle.center(it)
                circle.radius(1000.0)
                circle.fillColor(Color.argb(128, 0, 51, 102))
                circle.strokeWidth(1f)
                mMap.addCircle(circle)

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastPoint, 12f))
            }

        }

    }

}
