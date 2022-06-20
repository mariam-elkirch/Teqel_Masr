package com.example.teqelmasr.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.NavigationUI
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation

import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.teqelmasr.R
import com.example.teqelmasr.authentication.login.LoginActivity
import com.example.teqelmasr.authentication.login.viewmodel.LoginViewModel
import com.example.teqelmasr.authentication.login.viewmodel.LoginViewModelFactory
import com.example.teqelmasr.databinding.ActivityHomeBinding
import com.example.teqelmasr.displaySellerProducts.view.DisplaySellerProductsFragment
import com.example.teqelmasr.helper.Constants
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val TAG = "HomeActivity"
    private val user = Firebase.auth.currentUser
    private lateinit var sharedPref: SharedPreferences
    private lateinit var viewModel: LoginViewModel
    private val factory by lazy {
        LoginViewModelFactory(
            Repository.getInstance(
                Client.getInstance(),
                this
            )
        )
    }

    /* override fun onResume() {
         super.onResume()
         binding.navView.getHeaderView(0).findViewById<TextView>(R.id.name_text).text =
             sharedPref.getString(Constants.USER_NAME, Constants.GUEST_TYPE)
         Log.i(TAG, "USER_NAME FROM SHARED PREF: ${sharedPref.getString(Constants.USER_NAME, Constants.GUEST_TYPE)}")
     }*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG, "onCreate: ")

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = applicationContext.getSharedPreferences("MyPref", MODE_PRIVATE)

        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]



        setSupportActionBar(binding.toolBar)
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolBar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
            setDisplayShowTitleEnabled(false)
        }

        binding.sininText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)

            startActivity(intent)
        }
        binding.signinicon.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)

            startActivity(intent)
        }
        if(user != null){
            binding.signinicon.visibility = View.INVISIBLE
            binding.sininText.visibility = View.INVISIBLE
        }
        else{
            binding.signinicon.visibility = View.VISIBLE
            binding.sininText.visibility = View.VISIBLE
        }
        /*  binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_logout -> {
                    logOutUser()
                    true
                }
                else -> false
            }

            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }*/

        if (!(isNetworkAvailable())) {
            val snackBar = Snackbar.make(
                findViewById(R.id.home_activity),
                getString(R.string.no_internet),
                Snackbar.LENGTH_SHORT
            )
            snackBar.setAction(getString(R.string.enable_connection)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    startActivity(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
                } else {
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                }            }
            snackBar.show()
        }
        val bottomNavigationView = binding.bottomNav

        if (isNetworkAvailable()) {
            viewModel.getCustomer()

        }
        viewModel.customer.observe(this) {
            if (!it.isNullOrEmpty()) {
                binding.navView.getHeaderView(0).findViewById<TextView>(R.id.name_text).text =
                    it[0].first_name
            } else {
                Log.i(TAG, "onCreate: ELSE ")

            }

        }

        val navigationDrawerView = binding.navView
        // binding.navView.getHeaderView(0).findViewById<TextView>(R.id.name_text).text = "This is my User"

        val navController: NavController = Navigation.findNavController(this, R.id.hostFragment)

        setupActionBarWithNavController(navController, binding.drawerLayout)
        navigationDrawerView.setupWithNavController(navController)

        setupWithNavController(bottomNavigationView, navController)

        /*  navigationDrawerView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    logOutUser()
                    true
                }
                else -> {
                    true
                }
            }
            //true
        }*/
        binding.navView.setNavigationItemSelectedListener {
            // val item: Int = it.getItemId()
            when (it.itemId) {
                R.id.nav_logout -> {
                    logOutUser()

                }


            }

            NavigationUI.onNavDestinationSelected(it, navController)

            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }


        if (!(sharedPref.getString(Constants.USER_TYPE, Constants.GUEST_TYPE)
                .equals(Constants.SELLER_TYPE))
        ) {
            bottomNavigationView.menu.findItem(R.id.displaySellerProductsFragment).isVisible = false

        }
        if (sharedPref.getString(Constants.USER_TYPE, Constants.GUEST_TYPE)
                .equals(Constants.GUEST_TYPE)
        ) {
            Log.i(
                TAG,
                "onCreate: inside if(sharedPref.getString(Constants.USER_TYPE, Constants.GUEST_TYPE) ${
                    sharedPref.getString(
                        Constants.USER_TYPE,
                        Constants.GUEST_TYPE
                    )
                }"
            )
            val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
            val menu: Menu = navigationView.menu
            val logOut: MenuItem = menu.findItem(R.id.nav_logout)
            val profile: MenuItem = menu.findItem(R.id.profileFragment)
            logOut.isVisible = false
            profile.isVisible = false
        }


        Log.i("TAG", "usertype: ${sharedPref.getString(Constants.USER_TYPE, Constants.GUEST_TYPE)}")
        Log.i(TAG, "usertype: ${Firebase.auth.currentUser?.uid}")

    }

    private fun logOutUser() {

        Firebase.auth.signOut()

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.log_out))
        builder.setMessage(getString(R.string.sure_you_want_to_log_out))

        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            val editor = sharedPref.edit()
            editor.putString(Constants.USER_TYPE, Constants.GUEST_TYPE)
            editor.apply()

            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            //rewan
        }

        builder.setNegativeButton(getString(R.string.no)) { _, _ ->
        }

        builder.show()

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