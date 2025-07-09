package com.brews.cafeorderingapp.screens

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brews.cafeorderingapp.activity.OrderTrackingActivity
import com.brews.cafeorderingapp.activity.ui.theme.poppins

@Composable
fun OrderTrackingCard(orderID : String){
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F7F0)),
        modifier = Modifier.fillMaxWidth().height(80.dp).padding(12.dp).clickable {
            val i = Intent(context, OrderTrackingActivity::class.java)
            i.putExtra("orderID", orderID)
            context.startActivity(i)
        },

    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Track Order",
                fontSize = 18.sp,
                fontFamily = poppins,
                fontWeight = FontWeight.Bold
            )



            Text(
                text = "#${orderID}",
                fontSize = 14.sp,
                fontFamily = poppins,
                fontWeight = FontWeight.Normal
            )
        }
    }
}