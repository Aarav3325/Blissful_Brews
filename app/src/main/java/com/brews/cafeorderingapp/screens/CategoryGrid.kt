package com.brews.cafeorderingapp.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.brews.cafeorderingapp.R
import com.brews.cafeorderingapp.model.Category

@Composable
fun CategoryGrid(categoryList : ArrayList<Category?>){
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .padding(start = 12.dp, end = 12.dp)
            .fillMaxWidth()
            .height(425.dp)
    ) {
        items(categoryList) {
            item -> CategoryItem(item?.categoryName, item?.categoryImageUrl)
        }
    }
}

@Composable
fun CategoryItem(categoryName: String?, categoryImageUrl: String?) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(130.dp), // fixed width for consistency in grid
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = categoryImageUrl,
            contentDescription = categoryName,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable{
                    val intent = Intent(context, CategoryActivity::class.java)
                    intent.putExtra("categoryName", categoryName)
                    context.startActivity(intent)
                }
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = categoryName ?: "Unnamed",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
        )
    }
}
