package com.example.teqelmasr.home

import android.app.Activity
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.core.app.ActivityCompat.recreate

import androidx.navigation.ui.NavigationUI



import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.teqelmasr.R
import com.example.teqelmasr.databinding.ActivityHomeBinding

import com.google.android.material.bottomnavigation.BottomNavigationView




class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomNavigationView = binding.bottomNav
         bottomNavigationView.setBackgroundColor(Color.GRAY)
        val navController: NavController = Navigation.findNavController(this,R.id.hostFragment)
        setupWithNavController(bottomNavigationView, navController)
    }
}