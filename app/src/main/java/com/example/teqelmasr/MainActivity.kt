package com.example.teqelmasr

import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.teqelmasr.helper.Constants
import com.example.teqelmasr.helper.MyConnectivityManager
import com.example.teqelmasr.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val connectivityManager =
            getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(
            MyConnectivityManager.networkRequest,
            MyConnectivityManager.networkCallback
        )
        sharedPref = applicationContext.getSharedPreferences("MyPref", MODE_PRIVATE)
        //Log.i("TAG", "usertype: ${sharedPref.getString(Constants.USER_TYPE, Constants.GUEST_TYPE)}")

        //supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))

        Handler().postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3000 is the delayed time in milliseconds.
    }



}