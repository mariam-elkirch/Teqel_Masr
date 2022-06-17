package com.example.teqelmasr.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddEditProduct (

    val Type: String? = null,
    val category: String? = null,
    val title: String? = null,
    val price: String? = null,
    val manfactory: String? = null,

    val telephone: String? = null,
    val describtion: String? = null,
    var address: String? = null,
    var imageUri: Uri? = null

): Parcelable