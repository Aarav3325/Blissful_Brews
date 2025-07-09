package com.brews.cafeorderingapp.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthRepository {
    var firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    var firebaseUser = firebaseAuth.currentUser
    var userId = firebaseUser?.uid

    var firebaseDatabase = FirebaseDatabase.getInstance()
    val userReference = firebaseDatabase.getReference("user")


}