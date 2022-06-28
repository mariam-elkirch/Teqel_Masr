package com.example.teqelmasr.model

import kotlinx.parcelize.RawValue
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize

data class FilterObj(
    var priceRange: @RawValue IntRange,
    var categories: ArrayList<String>,
    var types: ArrayList<String>
):Parcelable
