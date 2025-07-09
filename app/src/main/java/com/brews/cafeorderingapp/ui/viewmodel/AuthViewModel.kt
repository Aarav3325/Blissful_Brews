package com.brews.cafeorderingapp.viewmodel

import androidx.lifecycle.ViewModel
import com.brews.cafeorderingapp.repository.AuthRepository

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    fun getUserId() : String?{
        return authRepository.userId
    }
}