package com.example.teqelmasr.authentication.register.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.teqelmasr.R
import com.example.teqelmasr.authentication.login.LoginActivity
import com.example.teqelmasr.authentication.register.viewModel.RegistrationViewModel
import com.example.teqelmasr.authentication.register.viewModel.RegistrationViewModelFactory
import com.example.teqelmasr.databinding.ActivityRegisterationBinding
import com.example.teqelmasr.model.Customer
import com.example.teqelmasr.model.CustomerObj
import com.example.teqelmasr.model.Repository
import com.example.teqelmasr.network.Client
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.Exception

class RegistrationActivity : AppCompatActivity() {
    private val TAG = "RegistrationActivity"
    private val binding by lazy { ActivityRegisterationBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val REQ_ONE_TAP = 2

    private val factory by lazy {
        RegistrationViewModelFactory(
            Repository.getInstance(
                Client.getInstance(),
                this
            )
        )
    }
    private lateinit var viewModel: RegistrationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        viewModel = ViewModelProvider(this, factory)[RegistrationViewModel::class.java]

        binding.registerBtn.setOnClickListener {
            if (binding.sellerCheck.isChecked) {
                registerUser(true)
            } else {
                registerUser(false)
            }
        }
        binding.login.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }



/*        binding.googleSign.setOnClickListener {
            startActivity(Intent(this, GoogleActivity::class.java))
        }*/
    }


    private fun registerUser(isSeller: Boolean) {
        val userName = binding.nameEdt.text.toString()
        val email = binding.emailEdt.text.toString()
        val password = binding.passEdt.text.toString()

        when {
            TextUtils.isEmpty(userName) -> {
                binding.apply {
                    nameEdt.error = "Name can't be empty"
                    nameEdt.requestFocus()
                }
            }
            TextUtils.isEmpty(email) -> {
                binding.apply {
                    emailEdt.error = "Email can't be empty"
                    emailEdt.requestFocus()
                }
            }
            TextUtils.isEmpty(password) -> {
                binding.apply {
                    passEdt.error = "Password can't be empty"
                    passEdt.requestFocus()
                }
            }
            else -> {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, R.string.success_reg, Toast.LENGTH_SHORT).show()
                        val customerObj = CustomerObj(
                            first_name = userName,
                            email = email,
                            last_name = FirebaseAuth.getInstance().currentUser!!.uid
                        )
                        if (isSeller) {
                            customerObj.note = "seller"
                        } else {
                            customerObj.note = "buyer"

                        }
                        val customer = Customer(customerObj)
                        viewModel.postCustomer(customer)
                    } else {
                        Log.i(TAG, "registerUser: ${it.exception}")
                        it.exception?.message.let { message ->
                            when (message.toString()) {
                                "The email address is already in use by another account." -> Toast.makeText(
                                    this,
                                    R.string.already_exists,
                                    Toast.LENGTH_LONG
                                ).show()
                                "The given password is invalid." -> Toast.makeText(
                                    this,
                                    R.string.not_valid_pass,
                                    Toast.LENGTH_LONG
                                ).show()
                                "The email address is badly formatted." -> Toast.makeText(
                                    this,
                                    R.string.badly_formatted,
                                    Toast.LENGTH_LONG
                                ).show()
                                else -> Toast.makeText(
                                    this,
                                    R.string.failed,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }


                    }
                }
            }
        }

    }


}