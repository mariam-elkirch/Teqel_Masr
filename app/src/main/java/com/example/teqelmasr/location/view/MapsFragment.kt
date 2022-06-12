package com.example.teqelmasr.location.view

import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.FragmentAddEquipmentSellBinding
import com.example.teqelmasr.databinding.FragmentDisplayEquipmentSellBinding
import com.example.teqelmasr.databinding.FragmentMapsBinding
import com.example.teqelmasr.home.HomeFragmentDirections
import com.example.teqelmasr.model.Utilities

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class MapsFragment : Fragment() , OnMapReadyCallback, GoogleMap.OnMarkerClickListener , View.OnClickListener {
   private lateinit var  currentMarker: Marker

    private lateinit var returnLocationToHome: String
    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapsBinding
    private lateinit var myLongitude: String
    private  var myLat: String =""
    private lateinit var type: String



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        mapFragment?.getMapAsync(this)
        //  mapFragment=

        binding.searchButton.setOnClickListener(this)
        binding.saveButton.setOnClickListener (this)

        return  binding.root
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        return p0.isInfoWindowShown
    }
    fun searchLocation(view: View) {
        currentMarker.remove()
        val geocoder = Geocoder(context)
        val addresses: List<Address>?
        val address: Address?
        val location: String
        location = binding.EditTextSearch.text.toString().trim()
        if (location == null || location == "") {
            Toast.makeText(context, "provide location", Toast.LENGTH_SHORT).show()
        } else {
            val geoCoder = Geocoder(context)
            try {
                addresses = geoCoder.getFromLocationName(location, 1)
                Log.d("Adddd", "nnnnn" + addresses)
                if (null != addresses && !addresses.isEmpty()) {
                    address = addresses[0]
                    Log.d("Ad2", "" + address.getAddressLine(0))
                    val latLng = LatLng(address.latitude, address.longitude)
                    Log.d("Ad3", "" + latLng)
                    placeMarkerOnMap(latLng)
                    markerDrag()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }
    private fun markerDrag() {

        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {

            override fun onMarkerDragStart(marker: Marker) {

            }
            override fun onMarkerDragEnd(marker: Marker) {
                Log.d("====", "latitude : " + marker.position.latitude)

                val newlatLng = LatLng(marker.position.latitude, marker.position.longitude)
                currentMarker.remove()
                placeMarkerOnMap(newlatLng)
            }

            override fun onMarkerDrag(marker: Marker) {

            }
        })
    }


    private fun placeMarkerOnMap(location: LatLng) {
        val markerOptions = MarkerOptions().position(location)
        val titleStr = context?.let { Utilities.getAddress(location, context = it) }
        markerOptions.title(titleStr)
        markerOptions.draggable(true)
        Log.d("MActivity", " " + titleStr + " ")
       binding.textLocation.setText(titleStr)

        currentMarker = mMap.addMarker(markerOptions)!!
        returnLocationToHome = titleStr!!
        myLat= location.latitude.toString()
        myLongitude=location.longitude.toString()

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
        Log.i("TAG", " " + returnLocationToHome + " ")

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.getUiSettings().setZoomControlsEnabled(true)
        mMap.setOnMarkerClickListener(this)
        val alex = LatLng(31.205753, 29.924526)
        placeMarkerOnMap(alex)


        onLongClick()

        markerDrag()
    }
    fun onLongClick(){
        //currentMarker.remove()
        mMap.setOnMapLongClickListener {
                latlng ->
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng))
            val getcoordinates = LatLng(latlng.latitude, latlng.longitude) //catch coordinates from
            currentMarker.remove()
            placeMarkerOnMap(getcoordinates)
            markerDrag()
        }
    }
    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.searchButton -> {
                searchLocation(v)
            }
            R.id.saveButton ->{
                Log.i("tag",returnLocationToHome+"latitudeeeeee")
                val action: NavDirections = MapsFragmentDirections.actionMapsFragmentToAddEquipmentSellFragment(returnLocationToHome)
                binding.root.findNavController().navigate(action)
            }
        }
    }}