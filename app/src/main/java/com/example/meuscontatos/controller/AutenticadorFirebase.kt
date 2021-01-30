package com.example.meuscontatos.controller

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

object AutenticadorFirebase {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var googleSignInClient: GoogleSignInClient? = null
}