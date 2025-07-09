package com.brews.cafeorderingapp.screens

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brews.cafeorderingapp.model.Cart
import com.brews.cafeorderingapp.viewmodel.CartViewModel
import com.brews.cafeorderingapp.viewmodel.MyViewModel
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.indexOf

@Composable
fun MenuScreen(viewModel: MyViewModel, scrollToCategory: String? = null, cartViewModel : CartViewModel) {
    val menuData by viewModel.menuData.observeAsState()

    // LazyListState to manage scrolling
    val listState = rememberLazyListState()

    // Load menu items when composable is first launched
    LaunchedEffect(Unit) {
        viewModel.loadMenuItems()
    }

    // Scroll to a specific category when data is loaded
    LaunchedEffect(menuData) {
        menuData?.let { menuMap ->
            scrollToCategory?.let { category ->
                val categoryIndex = menuMap.keys.indexOf(category)
                Log.i("MYTAG", "MenuScreen: category $categoryIndex")

                if (categoryIndex != -1) {
                    // Calculate the index of the first item in the category
                    val firstItemIndex = menuMap.keys.take(categoryIndex).sumBy { menuMap[it]?.size ?: 0 } + categoryIndex
                    Log.i("MYTAG", "Scrolling to first item of $category at index $firstItemIndex")

                    // Scroll to the first item in the specified category
                    listState.animateScrollToItem(firstItemIndex)
                }
            }
        }
    }

    // Scroll to a specific category when data is loaded
//    LaunchedEffect(menuData) {
//        menuData?.let { menuMap ->
//            scrollToCategory?.let { category ->
//                val categoryIndex = menuMap.keys.indexOf(category)
//                Log.i("MYTAG", "MenuScreen: $categoryIndex")
//                if (categoryIndex != -1) {
//                    // Scroll to the category index dynamically
//                    listState.animateScrollToItem(categoryIndex)
//                }
//            }
//        }
//    }

    menuData?.let {
            menuMap ->
        LazyColumn(state = listState) {
            menuMap.forEach {
                    (category, items) ->
                item {
                    Text(
                        text = category,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        modifier = Modifier.padding(top = 24.dp , start = 12.dp, end = 8.dp, bottom = 18.dp)
                    )
                }

                items(items) {
                        item -> MenuItem(item, cartViewModel, viewModel)
                }
            }

        }
    }

}