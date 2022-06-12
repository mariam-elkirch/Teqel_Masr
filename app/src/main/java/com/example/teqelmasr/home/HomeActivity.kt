package com.example.teqelmasr.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat.recreate

import androidx.navigation.ui.NavigationUI



import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.ActivityHomeBinding

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolBar)
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        getSupportActionBar()?.setHomeButtonEnabled(true)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)


        if(!(isNetworkAvailable())){
            val snackBar = Snackbar.make(
                findViewById(R.id.home_activity),
                getString(R.string.no_internet),
                Snackbar.LENGTH_SHORT
            )
            snackBar.setAction(getString(R.string.enable_connection)) {
                val panelIntent = Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
                startActivityForResult(panelIntent, 0)
            }
            snackBar.show()
        }
        val bottomNavigationView = binding.bottomNav
         bottomNavigationView.setBackgroundColor(Color.rgb(0,71,122))

        val navigationDrawerView = binding.navView

        val navController: NavController = Navigation.findNavController(this,R.id.hostFragment)

        setupActionBarWithNavController(navController,binding.drawerLayout)
        navigationDrawerView.setupWithNavController(navController)

        setupWithNavController(bottomNavigationView, navController)
    }

    private fun isNetworkAvailable(): Boolean {
        var isConnected = false
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                isConnected = when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }

        } else {
            if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting) {
                isConnected = true
            }
        }
        return isConnected
    }

    override fun onBackPressed() {
        super.onBackPressed()
/*        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.hostFragment, DisplaySellerProductsFragment())

        fragmentTransaction.commit()*/

        //Log.i("TAG", "onBackPressed: ${binding.root.findNavController().currentDestination}")

        //val fragmentInstance = supportFragmentManager.findFragmentById(R.id.hostFragment)
        //fragmentInstance?.onCreate(null)
        //Toast.makeText(this, "${fragmentInstance?.onCreate(null)}", Toast.LENGTH_SHORT).show()
/*
        if(fragmentInstance is DisplaySellerProductsFragment){
            Toast.makeText(this, "${fragmentInstance.id}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show()
        }*/
//        val action: NavDirections = DetailsSellerProductFragmentDirections.actionDetailsSellerProductFragmentToDisplaySellerProductsFragment(null)
//        binding.root.findNavController().navigate(action)
    }
}