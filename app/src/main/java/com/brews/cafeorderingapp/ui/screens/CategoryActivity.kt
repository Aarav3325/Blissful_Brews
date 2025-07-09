package com.brews.cafeorderingapp.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.ViewModelProvider
import com.brews.cafeorderingapp.activity.NewMenu
import com.brews.cafeorderingapp.activity.ui.theme.poppins
import com.brews.cafeorderingapp.repository.CartRepository
import com.brews.cafeorderingapp.screens.ui.theme.CafeOrderingAppTheme
import com.brews.cafeorderingapp.viewmodel.CartViewModel
import com.brews.cafeorderingapp.viewmodel.MyViewModel

class CategoryActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val cartRepository = CartRepository()
        val cartViewModel = CartViewModel(cartRepository)

        setContent {
            CafeOrderingAppTheme {
                var viewModel = ViewModelProvider(this)[MyViewModel::class.java]
                val menuData by viewModel.menuData.observeAsState()

                LaunchedEffect(Unit) {
                    viewModel.loadMenuItems()
                }

                val categoryName = intent.getStringExtra("categoryName")


                val categoryItemList = menuData?.get(categoryName)?.toList()
                Log.d("CategoryActivity", "Category Items: $categoryItemList")

                val lazyListState = rememberLazyListState()

                var searchText by remember {
                    mutableStateOf("")
                }

                var showSearchResult by remember {
                    mutableStateOf(false)
                }

                if(searchText.trim().isEmpty()){
                    showSearchResult = false
                }
                else{
                    showSearchResult = true
                }

                val context = LocalContext.current

                Scaffold(
                    containerColor = Color(0xFFDEE7E7),
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = categoryName.toString(),
                                    fontSize = 24.sp,
                                    fontFamily = poppins,
                                    fontWeight = FontWeight.Normal
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFDEE7E7)),
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        context.startActivity(Intent(context, NewMenu::class.java))
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.ArrowBack,
                                        contentDescription = "back arrow"
                                    )
                                }
                            }
                        )


                    },
                    content = {

//                        TextField(
//                            value = searchText,
//                            onValueChange = { searchText = it },
//                            modifier = Modifier.fillMaxWidth().padding(top = 48.dp, start = 12.dp, end = 12.dp).height(50.dp)
//                        )



                        LazyColumn(
                            state = lazyListState,
                            modifier = Modifier.padding(it)
                        ) {

                            item {
                                TextField(
                                    value = searchText,
                                    onValueChange = { searchText = it },
                                    leadingIcon = {Image(imageVector = Icons.Filled.Search, contentDescription = null)},
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp, start = 12.dp, end = 12.dp).height(55.dp).clip(
                                        RoundedCornerShape(12.dp)),
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = Color.White,
                                        focusedIndicatorColor = Color.White,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent,
                                        focusedContainerColor = Color.White
                                    ),
                                    placeholder = { Text("Search menu...") }
                                )
                            }

                            if (categoryItemList != null) {
                                val filteredItems = categoryItemList.filter {
                                    it.itemName.lowercase().contains(searchText.lowercase())
                                }

                                if (filteredItems.isNotEmpty()) {
                                    items(filteredItems) { item ->
                                        MenuItem(item, cartViewModel, viewModel)
                                    }
                                } else {
                                    item {
                                        Box(
                                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("No matching items found.")
                                        }
                                    }
                                }
                            }


                        }
                    }
                )
            }
        }
    }

    override fun onBackPressed() {

    }
}