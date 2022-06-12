package com.example.teqelmasr.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class FilterValues(
    var priceStart: Float? = 0f,
    var priceEnd: Float? = null,
    var types: Set<String>? = mutableSetOf()
) : Parcelable