package com.example.teqelmasr.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactInfo (


    val telephone: String? = null,

    var address: String? = null

): Parcelable