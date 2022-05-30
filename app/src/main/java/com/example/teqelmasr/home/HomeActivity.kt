package com.example.teqelmasr.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

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
        val navController: NavController = Navigation.findNavController(this,R.id.hostFragment)
        setupWithNavController(bottomNavigationView, navController)
    }
}