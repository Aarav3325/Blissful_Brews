package com.brews.cafeorderingapp.screens

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.util.Log
import com.brews.cafeorderingapp.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.brews.cafeorderingapp.activity.CartActivity
import com.brews.cafeorderingapp.activity.ui.theme.poppins
import com.brews.cafeorderingapp.model.MenuModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemModalSheet(
    onDismiss: () -> Unit,
    item: MenuModel,
    sheetState: SheetState,
    onCountIncrease: () -> Unit,
    onCountDecrease: () -> Unit,
    itemCount: Int
) {
    val context = LocalContext.current

    var checked1 by remember {
        mutableStateOf(false)
    }
    var checked2 by remember {
        mutableStateOf(false)
    }

    ModalBottomSheet(
        onDismissRequest =
                onDismiss,
          sheetState = sheetState,
        containerColor = Color.White,
        modifier = Modifier
    ) {
        // Sheet content
        Column(modifier = Modifier.padding(12.dp).verticalScroll(rememberScrollState()).fillMaxWidth()){
            Row(verticalAlignment = Alignment.CenterVertically){
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(72.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = item.itemName,
                        fontSize = 24.sp,
                        fontFamily = poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )

                    Text(
                        text = "Category",
                        fontSize = 16.sp,
                        fontFamily = poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

//            Text(
//                text = "Quantity",
//                fontSize = 18.sp,
//                fontFamily = poppins,
//                fontWeight = FontWeight.SemiBold,
//                color = Color.Black
//            )
//
//            Text(
//                text = "Required - select any 1 option",
//                fontSize = 12.sp,
//                fontFamily = poppins,
//                fontWeight = FontWeight.SemiBold,
//                color = Color.Black
//            )
//
//            AssistChip(
//                onClick = { Log.d("Assist chip", "hello world") },
//                label = { Text("Small") },
//                leadingIcon = {
//                    Icon(
//                        Icons.Filled.Settings,
//                        contentDescription = "Localized description",
//                        Modifier.size(AssistChipDefaults.IconSize)
//                    )
//                }
//            )
//
//            AssistChip(
//                onClick = { Log.d("Assist chip", "hello world") },
//                label = { Text("Medium") },
//                leadingIcon = {
//                    Icon(
//                        Icons.Filled.Settings,
//                        contentDescription = "Localized description",
//                        Modifier.size(AssistChipDefaults.IconSize)
//                    )
//                }
//            )
//
//
//
//            AssistChip(
//                onClick = { Log.d("Assist chip", "hello world") },
//                label = { Text("Large") },
//                leadingIcon = {
//                    Icon(
//                        Icons.Filled.Settings,
//                        contentDescription = "Localized description",
//                        Modifier.size(AssistChipDefaults.IconSize)
//                    )
//                }
//            )
//
//            Spacer(Modifier.height(12.dp))
//
//            Text(
//                text = "Dips/Toppings",
//                fontSize = 18.sp,
//                fontFamily = poppins,
//                fontWeight = FontWeight.SemiBold,
//                color = Color.Black
//            )
//
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Text(
//                    "Minimal checkbox"
//                )
//                Checkbox(
//                    checked = checked1,
//                    onCheckedChange = { checked1 = it }
//                )
//            }
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Text(
//                    "Minimal checkbox"
//                )
//                Checkbox(
//                    checked = checked2,
//                    onCheckedChange = { checked2 = it }
//                )
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(0.4f).height(60.dp).padding(bottom = 8.dp).align(Alignment.BottomStart)
                ){
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize()
                    ){
                        Text(
                            text = "-",
                            fontSize = 28.sp,
                            fontFamily = poppins,
                            color = Color.Black,
                            modifier = Modifier.clickable(
                                onClick = {
                                    onCountDecrease()
                                }
                            )
                        )
                        Text(
                            text = "$itemCount",
                            fontSize = 22.sp,
                            fontFamily = poppins,
                            color = Color.Black,
                            modifier = Modifier
                        )
                        Text(
                            text = "+",
                            fontSize = 28.sp,
                            fontFamily = poppins,
                            color = Color.Black,
                            modifier = Modifier.clickable(
                                onClick = {
                                    onCountIncrease()
                                }
                            )
                        )
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF2545B)),
                    modifier = Modifier.fillMaxWidth(0.55f).height(60.dp).padding(bottom = 8.dp).align(Alignment.BottomEnd).clickable(
                        onClick = {
                            val intent = Intent(context, CartActivity::class.java)
                            context.startActivity(intent)
                        }
                    )
                ){
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize()
                    ){

                        Text(
                            text = "Add to cart",
                            fontSize = 22.sp,
                            fontFamily = poppins,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }

            }


        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ModalTest(){

    var checked1 by remember {
        mutableStateOf(false)
    }
    var checked2 by remember {
        mutableStateOf(false)
    }

    ModalBottomSheet(
        onDismissRequest = {

        },
        containerColor = Color.Green,
        modifier = Modifier.fillMaxHeight()
    ) {
        // Sheet content
        Column(modifier = Modifier.padding(12.dp).verticalScroll(rememberScrollState())){
            Row(verticalAlignment = Alignment.CenterVertically){
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(56.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Item Item Name",
                        fontSize = 24.sp,
                        fontFamily = poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )

                    Text(
                        text = "Category",
                        fontSize = 18.sp,
                        fontFamily = poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Quantity",
                fontSize = 18.sp,
                fontFamily = poppins,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )

            Text(
                text = "Required - select any 1 option",
                fontSize = 12.sp,
                fontFamily = poppins,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )

            AssistChip(
                onClick = { Log.d("Assist chip", "hello world") },
                label = { Text("Assist chip") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Localized description",
                        Modifier.size(AssistChipDefaults.IconSize)
                    )
                }
            )


            AssistChip(
                onClick = { Log.d("Assist chip", "hello world") },
                label = { Text("Assist chip") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Localized description",
                        Modifier.size(AssistChipDefaults.IconSize)
                    )
                }
            )



            AssistChip(
                onClick = { Log.d("Assist chip", "hello world") },
                label = { Text("Assist chip") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Localized description",
                        Modifier.size(AssistChipDefaults.IconSize)
                    )
                }
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Dips/Toppings",
                fontSize = 18.sp,
                fontFamily = poppins,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Minimal checkbox"
                )
                Checkbox(
                    checked = checked1,
                    onCheckedChange = { checked1 = it }
                )
            }


            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Minimal checkbox"
                )
                Checkbox(
                    checked = checked2,
                    onCheckedChange = { checked2 = it }
                )
            }

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier.width(150.dp).height(60.dp).padding(bottom = 8.dp).align(Alignment.BottomStart)
                ){
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize()
                    ){
                        Text(
                            text = "-",
                            fontSize = 32.sp,
                            fontFamily = poppins,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth(0.3f)
                        )

                        Text(
                            text = "Add",
                            fontSize = 26.sp,
                            fontFamily = poppins,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth(0.3f)
                        )
                        Text(
                            text = "+",
                            fontSize = 32.sp,
                            fontFamily = poppins,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth(0.3f)
                        )
                    }
                }

                Card(
                    modifier = Modifier.width(230.dp).height(60.dp).padding(bottom = 8.dp).align(Alignment.BottomEnd)
                ){
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize()
                    ){

                        Text(
                            text = "Add to cart",
                            fontSize = 26.sp,
                            fontFamily = poppins,
                            color = Color.Black
                        )
                    }
                }

            }





        }
    }
}