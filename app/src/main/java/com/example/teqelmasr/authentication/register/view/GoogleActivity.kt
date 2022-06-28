package com.example.teqelmasr.authentication.register.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.teqelmasr.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val REQ_ONE_TAP = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        createGoogleRequest()
        signInWithGoogleAccount()
    }

    private fun createGoogleRequest() {
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(R.string.default_web_client_id.toString())
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }
    private fun signInWithGoogleAccount() {
        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent,REQ_ONE_TAP)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQ_ONE_TAP){
            val task  = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)
                Log.i("TAG", "firebaseAuthWithGoogle : account.getId()" + account.getId());

                firebaseAuthWithGoogle(account.idToken.toString())

            }catch (e: ApiException){
                Log.i("TAG", "onActivityResult: ${e.message}")
                Log.i("TAG", "onActivityResult: ${e.status}")
                Toast.makeText(
                    this,
                    R.string.failed,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    fun  firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(
                        this,
                        R.string.success_reg,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

    }
}