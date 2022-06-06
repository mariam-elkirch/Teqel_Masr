package com.example.teqelmasr.model

import kotlinx.parcelize.RawValue
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize

data class FilterObj(
    val priceRange: @RawValue IntRange,
    val categories: ArrayList<String>,
    val types: ArrayList<String>
):Parcelable
