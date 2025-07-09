package com.brews.cafeorderingapp.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brews.cafeorderingapp.R
import com.brews.cafeorderingapp.activity.ui.theme.poppins
import com.brews.cafeorderingapp.model.MenuModel

@Composable
fun CategoryFAB(modifier: Modifier, onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = Color(0xFFE8A0BF),
        modifier = modifier.clip(CircleShape)
    ) {
        Image(
            painter = painterResource(id = R.drawable.menu),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun CategoryDisplay(
    modifier: Modifier,
    categoryList: List<String>,
    menuData: Map<String?, List<MenuModel?>?>,
    onCategoryClick: (String) -> Unit
) {


    val listState = rememberLazyListState()

    var category by remember {
        mutableStateOf("")
    }


    LazyColumn(
        state = listState,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .fillMaxWidth(0.55f)
            .background(Color(0xFFF3F7F0))
            .padding(8.dp)
    ) {


        items(categoryList) { categoryItem ->
            Text(
                text = categoryItem,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppins,
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth().clickable(
                    onClick = {
                        onCategoryClick(categoryItem)
                        Log.i("MYTAG", category)
                    }
                ).padding(8.dp)
            )
        }

    }
}



