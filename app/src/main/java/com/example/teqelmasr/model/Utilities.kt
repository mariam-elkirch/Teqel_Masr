package com.example.teqelmasr.model

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.io.IOException

class Utilities {


    companion object {


        fun getAddress(latLng: LatLng, context: Context): String {

            val geocoder = Geocoder(context)
            val addresses: List<Address>?
            val address: Address?
            var addressText = ""

            try {

                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

                if (null != addresses && !addresses.isEmpty()) {
                    address = addresses[0]

                    addressText = address.getAddressLine(0)
                }
            } catch (e: IOException) {
                Log.e("MapsActivity", e.localizedMessage)
            }

            return addressText
        }

        fun isUserLoggedIn(): Boolean {
            var isExist : Boolean
            if(Firebase.auth.currentUser != null)
             isExist = true
            else{
                isExist = false
            }
            return isExist
        }
    }
}