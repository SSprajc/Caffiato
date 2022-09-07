package hr.algebra.caffiato

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import hr.algebra.caffiato.models.Deal4View

class LocationFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private val LINEA = LatLng(45.81433511923153, 15.945858911804908)
    private val OSC = LatLng(45.81032469981096, 15.940697113536043)

    private var markerLinea: Marker? = null
    private var markerOldSchoolCaffe: Marker? = null

    private lateinit var tvCaffeName: TextView
    private lateinit var tvCaffeAddress: TextView
    private lateinit var rvDeals: RecyclerView

    private lateinit var mapView: MapView
    private lateinit var mMap: GoogleMap

    //private lateinit var binding: FragmentLocationBinding

    /*private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

     */

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //binding = FragmentLocationBinding.inflate(layoutInflater, container, false)
        //mapView = binding.idMap
        val v: View = inflater.inflate(R.layout.fragment_location, container, false)

        tvCaffeName = v.findViewById(R.id.tvCaffe)
        tvCaffeAddress = v.findViewById(R.id.tvCaffeAddress)
        rvDeals = v.findViewById(R.id.rvLocationDeals)

        mapView = v.findViewById(R.id.idMap)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return v
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onMapReady(gMap: GoogleMap) {
        mMap = gMap
        markerLinea = mMap.addMarker(
            MarkerOptions()
                .position(LINEA)
                .title("Linea")
        )

        markerOldSchoolCaffe = mMap.addMarker(
            MarkerOptions()
                .position(OSC)
                .title("Old School Cafe")
        )
        mMap.setOnMarkerClickListener(this)
        //45.81433511923153, 15.945858911804908
        //val linea = LatLng(45.81433511923153, 15.945858911804908)
        //45.81032469981096, 15.940697113536043
        //val oldSchoolCaffe = LatLng(45.81032469981096, 15.940697113536043)
        /*mMap.addMarker(MarkerOptions().position(linea).title("Linea"))
        mMap.addMarker(MarkerOptions().position(oldSchoolCaffe).title("Old School Caffe"))
         */
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LINEA, 13.5f))
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        fillRecyclerView(marker.title)
        return false
    }

    private fun fillRecyclerView(title: String?) {
        val deals = mutableListOf<Deal4View>()
        val caffes = MainActivity.caffes
        caffes.forEach { caffe ->
            if (caffe.name == title) {
                caffe.dealList.forEach {
                    deals.add(
                        Deal4View(
                            it.name,
                            caffe.name,
                            caffe.address.streetName + " " + caffe.address.streetNumber,
                            it.points,
                            it.price,
                            it.date
                        )
                    )
                }
            }
        }
        if (!deals.isNullOrEmpty()) {
            tvCaffeName.text = deals[0].caffeName
            tvCaffeAddress.text = deals[0].address
            rvDeals.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = LocationDealAdapter(requireContext(), deals)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }


}