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


class MainActivity : AppCompatActivity() {
/*    val sharedPref: SharedPreferences =
        applicationContext.getSharedPreferences("MyPref", MODE_PRIVATE)*/
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPref = applicationContext.getSharedPreferences("MyPref", MODE_PRIVATE)
        Log.i("TAG", "Auth: ${FirebaseAuth.getInstance().currentUser?.uid}")
        Log.i("TAG", "PREF: ${sharedPref.getBoolean(Constants.LOGIN_FLAG, false)}")
        Log.i("TAG", "PREF: ${sharedPref.getString(Constants.USER_TYPE, "no_type")}")

        //supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))

        Handler().postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra(Constants.IS_SELLER, isSeller())
            startActivity(intent)
            finish()
        }, 3000) // 3000 is the delayed time in milliseconds.
    }

    private fun isSeller(): Boolean {
        return (sharedPref.getBoolean(
            Constants.LOGIN_FLAG,
            false
        ) && sharedPref.getString(Constants.USER_TYPE, Constants.BUYER_TYPE)
            .equals(Constants.SELLER_TYPE))
    }


}