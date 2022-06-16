package com.example.teqelmasr.authentication.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.teqelmasr.authentication.login.viewmodel.LoginViewModel
import com.example.teqelmasr.authentication.login.viewmodel.LoginViewModelFactory
import com.example.teqelmasr.authentication.register.view.RegistrationActivity
import com.example.teqelmasr.databinding.ActivityLoginBinding
import com.example.teqelmasr.helper.Constants
import com.example.teqelmasr.home.HomeActivity
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences

    private val TAG = "LoginActivity"
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    private val factory by lazy {
        LoginViewModelFactory(
            Repository.getInstance(
                Client.getInstance(),
                this
            )
        )
    }
    private lateinit var viewModel: LoginViewModel

    /*public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // reload();
            Log.i(TAG, "onStart: inside currentUser != null")
        }
    }*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sharedPref = applicationContext.getSharedPreferences("MyPref", MODE_PRIVATE)


        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        auth = Firebase.auth



        binding.loginBtn.setOnClickListener {
            loginUser()


        }

        binding.register.setOnClickListener {
            val registerIntent = Intent(this, RegistrationActivity::class.java)
            startActivity(registerIntent)
            finish()
        }
    }

    private fun loginUser() {

        val email = binding.emailEditText.text.toString()
        val password = binding.passEditText.text.toString()

        Log.i(TAG, "onCreate: email is $email and password is $password")

        when {
            TextUtils.isEmpty(email) -> {
                binding.apply {
                    emailEditText.error = "Email can't be empty"
                    emailEditText.requestFocus()
                }
            }
            TextUtils.isEmpty(password) -> {
                binding.apply {
                    passEditText.error = "Password can't be empty"
                    passEditText.requestFocus()
                }
            }
            else -> {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val editor: SharedPreferences.Editor = sharedPref.edit()
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            viewModel.customer.observe(this) {
                                if (!it.isNullOrEmpty()){
                                    editor.putString(Constants.USER_TYPE, it[0].note)
                                    Log.i(TAG, "onCreate: user type is ${it[0].note}")
                                    editor.apply()
                                }
                            }
                            Toast.makeText(
                                baseContext,
                                "Logged in Successfully.",
                                Toast.LENGTH_SHORT
                            ).show()
                            val homeIntent = Intent(this, HomeActivity::class.java)
                            startActivity(homeIntent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            binding.errorTextView.visibility = View.VISIBLE
                        }
                    }
            }
        }
    }


}