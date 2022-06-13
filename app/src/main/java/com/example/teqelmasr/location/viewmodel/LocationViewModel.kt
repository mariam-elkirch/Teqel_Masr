package com.example.weathery.location.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.weathery.location.viewmodel.LocationLiveData

class LocationViewModel (application: Application) : AndroidViewModel(application) {

    private val locationLiveData = LocationLiveData(application)
    internal fun getLocationLiveData() = locationLiveData
}