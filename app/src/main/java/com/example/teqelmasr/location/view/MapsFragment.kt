package com.example.teqelmasr.location.view

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.teqelmasr.R
import com.example.teqelmasr.addEquipmentSell.view.AddEquipmentSellFragmentArgs
import com.example.teqelmasr.databinding.FragmentAddEquipmentSellBinding
import com.example.teqelmasr.databinding.FragmentDisplayEquipmentSellBinding
import com.example.teqelmasr.databinding.FragmentMapsBinding
import com.example.teqelmasr.displayEquipmentSell.view.DetailsEquipmentSellFragmentArgs
import com.example.teqelmasr.helper.Constants
import com.example.teqelmasr.home.HomeFragmentDirections
import com.example.teqelmasr.model.AddEditProduct
import com.example.teqelmasr.model.LocationDetails
import com.example.teqelmasr.model.Utilities
import com.example.weathery.location.viewmodel.LocationViewModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    View.OnClickListener {
    private val args by navArgs<MapsFragmentArgs>()
    private lateinit var currentMarker: Marker

    private lateinit var returnLocationToHome: String
    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapsBinding
    private lateinit var myLongitude: String
    private var myLat: String = ""
    private lateinit var type: String
    private val LOCATION_PERMISSION_REQUEST_CODE: Int = 200
    lateinit var locationViewModel: LocationViewModel
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    val mylong = MutableLiveData<String>()
    var mylocation = LocationDetails("", "")
    var mylat = MutableLiveData<String>()
    var doublelong: Double = 0.0
    var doublelat: Double = 0.0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        mapFragment?.getMapAsync(this)
        //  mapFragment=

        sharedPreferences = requireContext().getSharedPreferences(
            "shared",
            Context.MODE_PRIVATE
        )
        editor = sharedPreferences.edit()


        binding.searchButton.setOnClickListener(this)
        binding.saveButton.setOnClickListener(this)

        return binding.root
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
        // currentMarker.remove()
        val markerOptions = MarkerOptions().position(location)
        val titleStr = context?.let { Utilities.getAddress(location, context = it) }
        markerOptions.title(titleStr)
        markerOptions.draggable(true)
        Log.d("MActivity", " " + titleStr + " ")
        binding.textLocation.setText(titleStr)

        currentMarker = mMap.addMarker(markerOptions)!!
        returnLocationToHome = titleStr!!
        myLat = location.latitude.toString()
        myLongitude = location.longitude.toString()

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
        Log.i("TAG", " " + returnLocationToHome + " ")

    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.getUiSettings().setZoomControlsEnabled(true)
        mMap.setOnMarkerClickListener(this)
        /*  mylocation.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
              doublelat = mylocation.value?.latitude?.toDouble() ?: 0.0
              doublelong = mylocation.value?.longitude?.toDouble() ?: 0.0

          })*/

        // doublelat = mylocation.latitude.toDouble()
        // doublelong = mylocation.longitude.toDouble()
        val sharedlat = sharedPreferences.getString("latitude", "31.205753")
        val sharedlong = sharedPreferences.getString("longitude", "29.924526")
        val doublelat: Double = sharedlat!!.toDouble()
        val doublelong: Double = sharedlong!!.toDouble()
        Log.i("tag", sharedlat + "shareddddd" + sharedlong)
        val alex = LatLng(doublelat, doublelong)
        placeMarkerOnMap(alex)
        onLongClick()

        markerDrag()


    }

    fun onLongClick() {
        //currentMarker.remove()
        mMap.setOnMapLongClickListener { latlng ->
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
            R.id.saveButton -> {
                Log.i("tag", returnLocationToHome + "latitudeeeeee")

                when (args.source) {

                    Constants.ADD_SOURCE -> {
                        var product = args.enteredProduct
                        Log.i("tag", "price" + product?.price + "ffff")
                        product?.address = returnLocationToHome


                        val action: NavDirections =
                            MapsFragmentDirections.actionMapsFragmentToAddEquipmentSellFragment(
                                product
                            )
                        binding.root.findNavController().navigate(action)
                    }
                    Constants.EDIT_SOURCE -> {
                        val product = args.currentProduct
                        Log.i("tag",args.imageUri+"image Map")
                        product?.variants?.get(0)?.option1 = returnLocationToHome
                        val action: NavDirections =
                            MapsFragmentDirections.actionMapsFragmentToEditSellerProductFragment(
                                args.currentProduct!!,args.imageUri,
                         AddEditProduct(imageUri = args.enteredProduct?.imageUri)
                            )
                        binding.root.findNavController().navigate(action)

                    }
                }
            }
        }
    }
}