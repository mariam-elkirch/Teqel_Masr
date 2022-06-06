package com.example.teqelmasr.home

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate

import androidx.navigation.ui.NavigationUI



import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.ActivityHomeBinding
import com.example.teqelmasr.displaySellerProducts.view.DetailsSellerProductFragmentDirections
import com.example.teqelmasr.displaySellerProducts.view.DisplaySellerProductsFragment

import com.google.android.material.bottomnavigation.BottomNavigationView




class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomNavigationView = binding.bottomNav

        val navController: NavController = Navigation.findNavController(this,R.id.hostFragment)
        setupWithNavController(bottomNavigationView, navController)
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