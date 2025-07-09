package com.brews.cafeorderingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.brews.cafeorderingapp.repository.CartRepository

class CartViewModelProvider(val cartRepository: CartRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CartViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(cartRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}