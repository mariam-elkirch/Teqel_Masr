package com.example.teqelmasr.model

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teqelmasr.displaySellerProducts.viewModel.MyProductsViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(sdk = [30])
@RunWith(AndroidJUnit4::class)
class UtilitiesTest : TestCase() {

    @Before
    fun obtainFirebaseInstance() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        FirebaseApp.initializeApp(context)
    }


    @Test
    fun isUserLoggedIn(){
        val isLoggedin : Boolean
        isLoggedin = Firebase.auth.currentUser != null

        assertEquals(isLoggedin,Utilities.isUserLoggedIn())
    }
}