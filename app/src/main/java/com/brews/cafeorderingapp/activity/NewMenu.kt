package com.brews.cafeorderingapp.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.brews.cafeorderingapp.activity.ui.theme.CafeOrderingAppTheme
import com.brews.cafeorderingapp.repository.CartRepository
import com.brews.cafeorderingapp.repository.UserUtil
import com.brews.cafeorderingapp.screens.HomeScreen
import com.brews.cafeorderingapp.viewmodel.AuthViewModel
import com.brews.cafeorderingapp.viewmodel.CartViewModel
import com.brews.cafeorderingapp.viewmodel.CartViewModelProvider
import com.brews.cafeorderingapp.viewmodel.MyViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.collections.toList

class NewMenu : ComponentActivity() {

    var firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var firebaseUser : FirebaseUser
    lateinit var userId : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeOrderingAppTheme {
                val viewModel = ViewModelProvider(this)[MyViewModel::class.java]
                var showCategoryList by remember {
                    mutableStateOf(false)
                }

                var currentOrder = intent.getStringExtra("orderID")



                val authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

                val cartRepository = CartRepository()
                val cartViewModelProvider = CartViewModelProvider(cartRepository)

                val cartViewModel = ViewModelProvider(this, cartViewModelProvider)[CartViewModel::class.java]
                cartViewModel.loadMenu()
                val menuMap = cartViewModel.getMenuList()
                val menuList = menuMap.values.flatten()

                val categoryItemList = menuMap["Burgers"]?.toList()

                if (categoryItemList != null) {
                    for (item in categoryItemList){
                        Log.i("catItem", "onCreate: ${item}")
                    }
                }

                val cartList = cartViewModel.fetchCart(UserUtil.getUserId())

                var categoryList = cartViewModel.getCategoryList()

                LaunchedEffect(categoryList) {
                    cartViewModel.callCategoryList()
                    categoryList = cartViewModel.getCategoryList()
                }

                for (category in categoryList){
                    Log.i("CATTAG", "onCreate: $category")
                }

                firebaseUser = firebaseAuth.currentUser!!

//                var showCurrentOrder = cartViewModel.getCurrentOrder()

                if(true){
                    userId = firebaseUser.uid
                }

                Log.i("MYTAG", "onCreate: $userId")





//                MenuScreen(viewModel)
                HomeScreen(viewModel, cartViewModel)

                //CategoryScreen()
                // MenuScreen(viewModel = viewModel, scrollToCategory = "Beverages")
            }
        }
    }

    override fun onBackPressed() {

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CafeOrderingAppTheme {
        Greeting("Android")
    }
}


