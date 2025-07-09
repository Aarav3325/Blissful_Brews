package com.brews.cafeorderingapp.repository

import android.util.Log
import androidx.compose.ui.util.unpackInt1
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.brews.cafeorderingapp.model.Category
import com.brews.cafeorderingapp.model.FirebaseCart
import com.brews.cafeorderingapp.model.MenuModel
import com.brews.cafeorderingapp.model.Order
import com.firebase.ui.auth.data.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.toString

class CartRepository {

    private lateinit var firebaseDatabase : FirebaseDatabase
    private lateinit var menuReference : DatabaseReference
    private lateinit var cartReference : DatabaseReference
    private lateinit var orderReference : DatabaseReference

    private lateinit var categoryReference : DatabaseReference

    private lateinit var menuList: ArrayList<MenuModel>;

    val userId = UserUtil.getUserId()

    private var menuData : MutableMap<String?, List<MenuModel?>> = HashMap<String?, List<MenuModel?>>()

    private var cartItemList = ArrayList<FirebaseCart>();

    private var menuItemList = menuData.values.flatten()
    internal var firebaseCartList = ArrayList<FirebaseCart>()

    var currentOrderID : String? = null


    constructor(){
        firebaseDatabase = FirebaseDatabase.getInstance()
        menuReference = firebaseDatabase.getReference("newmenu")
        cartReference = firebaseDatabase.getReference("newcart")
        orderReference = firebaseDatabase.getReference("Orders")
        categoryReference = firebaseDatabase.getReference("categories")
    }

    fun loadMenuItems() {
        menuReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val menuMap: MutableMap<String?, List<MenuModel?>> =
                    HashMap<String?, List<MenuModel?>>()

                for (dataSnapshot in snapshot.getChildren()) {
                    val categoryName = dataSnapshot.key
                    val menuModels: MutableList<MenuModel?> = java.util.ArrayList<MenuModel?>()

                    for (itemSnapshot in dataSnapshot.getChildren()) {
                        val menuModel = itemSnapshot.getValue(MenuModel::class.java)
                        menuModels.add(menuModel)
                    }

                    menuMap.put(categoryName, menuModels)
                }

                menuData.putAll(menuMap)
                Log.i("Repo", "loadMenuItems: $menuData")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MenuViewModel", "Database error: " + error.getMessage())
            }
        })
    }

    fun getMenuData(): MutableMap<String?, List<MenuModel?>> {
        return menuData
    }

    fun addItemToCart(menuItem : MenuModel){
        val cartItem = FirebaseCart(menuItem.itemName, menuItem.itemPrice, menuItem.count, menuItem.itemId)

        if(cartItemList.isEmpty()){
            cartReference.child(userId).child(menuItem.itemId).setValue(cartItem)
        }
        else if(cartItemList.isNotEmpty()){
            cartReference.child(userId).child(menuItem.itemId).setValue(cartItem)
        }
        else{
            updateCountFirebase(userId ,menuItem.itemId, cartItem.cartItemCount)
        }

        cartItemList.add(cartItem)

    }


    fun updateCountFirebase(key : String,cartId: String, cartItemCount : Int){

        if(cartItemCount == 0){
            cartReference.child(key).child(cartId).removeValue()
        }
        else{
            cartReference.child(key).child(cartId).child("cartItemCount").setValue(cartItemCount)
        }
    }

    fun getItemCountFirebase(key : String, cartId: String) : Int{
        var cartItemCount = 0;
        cartReference.child(key).child(cartId).child("cartItemCount").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.getValue(Int::class.java)
                if (count != null) {
                    cartItemCount = count
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        return cartItemCount
    }

    fun getFirebaseCart(uniqueKey : String, onResult: (List<FirebaseCart>) -> Unit) {
            Log.d("CartDebug", "Getting cart for userId = $userId")


            cartReference.child(userId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    Log.d("CartDebug", "onDataChange called")
                    firebaseCartList.clear()

                    for (dataSnapshot in snapshot.children) {
                        val firebaseCart = dataSnapshot.getValue(FirebaseCart::class.java)
                        if (firebaseCart != null) {
                            firebaseCartList.add(firebaseCart)
                        }
                    }
                    onResult(firebaseCartList)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        fun getCurrentOrder(callback: (Boolean) -> Unit) {
            val userId = UserUtil.getUserId()

            orderReference.child(userId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var hasActiveOrder = false

                        for (dataSnapshot in snapshot.children) {
                            val order = dataSnapshot.getValue(Order::class.java)
                            val orderId = dataSnapshot.key

                            if (order?.status != 5) {
                                hasActiveOrder = true
                                currentOrderID = orderId
                                break
                            }
                        }
                        Log.i("TrackRepo", hasActiveOrder.toString())
                        callback(hasActiveOrder)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // In case of error, assume no active order
                        callback(false)
                    }
                })
        }

    var categoryList = ArrayList<Category?>()

    fun getCategoryList(){
        categoryReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryList.clear()
                for (dataSnapshot in snapshot.children){
                    var categoryItem = dataSnapshot.getValue(Category::class.java)
                    categoryList.add(categoryItem)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    val orderList = ArrayList<Order?>()

    fun getRecentOrders(){
        orderReference.child(UserUtil.getUserId()).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                orderList.clear()
                for(dataSnapshot in snapshot.children){
                    var order = dataSnapshot.getValue(Order::class.java)
                    orderList.add(order)

                    Log.i("recentOrderCheck", order?.orderId.toString())
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }



}