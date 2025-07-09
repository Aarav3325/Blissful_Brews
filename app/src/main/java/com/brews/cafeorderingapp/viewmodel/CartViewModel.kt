package com.brews.cafeorderingapp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brews.cafeorderingapp.model.Category
import com.brews.cafeorderingapp.model.FirebaseCart
import com.brews.cafeorderingapp.model.MenuModel
import com.brews.cafeorderingapp.model.Order
import com.brews.cafeorderingapp.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel(val cartRepository: CartRepository) : ViewModel() {


    fun getMenuList(): MutableMap<String?, List<MenuModel?>> {
        return cartRepository.getMenuData()
    }

    fun loadMenu(){
        cartRepository.loadMenuItems()
    }

    fun addItemToCart(menuItem : MenuModel){
        cartRepository.addItemToCart(menuItem)
    }

    fun updateCountFirebase(key : String,cartId: String, cartItemCount : Int){
        cartRepository.updateCountFirebase(key, cartId, cartItemCount)
    }

    fun getItemCountFirebase(key : String, cartId : String) : Int{
        return cartRepository.getItemCountFirebase(key, cartId)
    }

    fun getFirebaseCart(uniqueKey : String) : List<FirebaseCart>{
        return cartRepository.firebaseCartList
    }

    private val _firebaseCart = MutableStateFlow<List<FirebaseCart>>(emptyList())
    val firebaseCart: StateFlow<List<FirebaseCart>> = _firebaseCart

    fun fetchCart(userId: String) {
        cartRepository.getFirebaseCart(userId) { cartList ->
            _firebaseCart.value = cartList.toList() // ← Forces a new reference
        }
    }

    fun getCategoryList() : ArrayList<Category?>{
        return cartRepository.categoryList
    }

    fun callCategoryList(){
        cartRepository.getCategoryList()
    }

    val orderList = cartRepository.orderList

    fun getRecentOrders(){
        cartRepository.getRecentOrders()
    }


    private val _hasCurrentOrder = MutableStateFlow(false)
    val hasCurrentOrder: StateFlow<Boolean> = _hasCurrentOrder

    private val _currentOrderId = MutableStateFlow("")
    val currentOrderId: StateFlow<String> = _currentOrderId

        fun fetchCurrentOrder() {
            cartRepository.getCurrentOrder { flag ->
                viewModelScope.launch {
                    _hasCurrentOrder.value = flag
                    _currentOrderId.value = cartRepository.currentOrderID ?: ""
                }
            }
        }





}