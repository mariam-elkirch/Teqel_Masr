package com.example.teqelmasr

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.teqelmasr.helper.Constants
import com.example.teqelmasr.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = applicationContext.getSharedPreferences("MyPref", MODE_PRIVATE)
        val lang =   sharedPref.getString("lang", "en")
        val config = this.resources?.configuration

        val locale = Locale(lang.toString())
        Log.i("tag",lang.toString()+"language")
        Locale.setDefault(locale)
        config?.setLocale(locale)

        this.createConfigurationContext(config!!)
        this.resources?.updateConfiguration(config,this.resources!!.displayMetrics)
        setContentView(R.layout.activity_main)

        //Log.i("TAG", "usertype: ${sharedPref.getString(Constants.USER_TYPE, Constants.GUEST_TYPE)}")

        //supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))

        Handler().postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3000 is the delayed time in milliseconds.
    }



}