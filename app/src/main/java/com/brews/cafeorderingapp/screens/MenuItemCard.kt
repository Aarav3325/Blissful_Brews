package com.brews.cafeorderingapp.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.brews.cafeorderingapp.R
import com.brews.cafeorderingapp.model.MenuModel

import com.brews.cafeorderingapp.activity.ui.theme.poppins
import com.brews.cafeorderingapp.model.FavouriteDish
import com.brews.cafeorderingapp.repository.UserUtil
import com.brews.cafeorderingapp.viewmodel.AuthViewModel
import com.brews.cafeorderingapp.viewmodel.CartViewModel
import com.brews.cafeorderingapp.viewmodel.MyViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuItem(item: MenuModel, cartViewModel: CartViewModel, viewModel: MyViewModel) {


    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    var isFavourite = viewModel.isDishFavourite(item.itemPrice).observeAsState();


    var showBottomSheet by remember { mutableStateOf(false) }

    var itemCount by rememberSaveable {
        mutableStateOf(item.count)
    }

    LaunchedEffect(Unit) {
        cartViewModel.fetchCart(UserUtil.getUserId())
    }

    val cartList by cartViewModel.firebaseCart.collectAsState()

    LaunchedEffect(cartList) {
        for (cart in cartList) {
            Log.i("CARTTEST", cart.cartItemName)
            if (cart.cartItemId == item.itemId) {
                itemCount = cart.cartItemCount
            }
        }

    }

    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4FAFF)),
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .fillMaxWidth()
            .padding(top = 0.dp, start = 12.dp, end = 12.dp, bottom = 22.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()){
            Column(modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(12.dp)) {
                Text(
                    text = item.itemName,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppins,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = item.itemDes,
                    fontFamily = poppins,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(0.65f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "₹ ${item.itemPrice}",
                    fontFamily = poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {showBottomSheet = true
                                 itemCount = 1
                    item.count = itemCount

                    cartViewModel.addItemToCart(item)},
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .width(85.dp)
                        .height(35.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009688))) {
                    if(itemCount == 0){
                        Text(
                            text = "ADD +",
                            fontFamily = poppins,
                            fontSize = 16.sp,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                    else{
                        Button(onClick = {
                            showBottomSheet = true
                        },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .width(100.dp)
                                .height(35.dp),

                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009688))) {

                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = "-",
                                    fontFamily = poppins,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .fillMaxWidth(0.3f)
                                        .clickable(
                                            onClick = {
                                                itemCount--
                                                item.count = itemCount
                                                cartViewModel.updateCountFirebase(
                                                    UserUtil.getUserId(),
                                                    item.itemId,
                                                    itemCount
                                                )
                                                if (itemCount == 0) {
                                                    showBottomSheet = false
                                                }
                                            }
                                        )
                                )
                                Text(
                                    text = "$itemCount",
                                    fontFamily = poppins,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .fillMaxWidth(0.3f)
                                )
                                Text(
                                    text = "+",
                                    fontFamily = poppins,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .fillMaxWidth(0.3f)
                                        .clickable(
                                            onClick = {
                                                //itemCount++
//                                                item.count = itemCount
//                                                cartViewModel.updateCountFirebase(
//                                                    UserUtil.getUserId(),
//                                                    item.itemId,
//                                                    itemCount
//                                                )
                                                if (itemCount > 0) {

                                                    showBottomSheet = true
                                                }
                                            }
                                        )
                                )
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 12.dp, end = 12.dp)
            ){
                Image(
                    painter = painterResource(id = if(isFavourite.value == true) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp).clickable{
                        if(isFavourite.value == true){
                            viewModel.deleteFavouriteDish(FavouriteDish(item.itemName, item.imageUrl, item.itemPrice, item.itemDes, item.itemPrice))
                        }
                        else{
                            viewModel.insertFavouriteDish(FavouriteDish(item.itemName, item.imageUrl, item.itemPrice, item.itemDes, item.itemPrice))
                        }
                    }
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 24.dp)
                    .clip(RoundedCornerShape(12.dp))
            ){
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }

    if(showBottomSheet){
        ItemModalSheet({showBottomSheet = false}, item, sheetState, {itemCount++
            item.count = itemCount
            cartViewModel.updateCountFirebase(UserUtil.getUserId(), item.itemId, itemCount)}, {itemCount--
            item.count = itemCount
            cartViewModel.updateCountFirebase(UserUtil.getUserId(), item.itemId, itemCount)
            if (itemCount == 0) showBottomSheet = false}, itemCount)
    }

}

@Preview(showBackground = true)
@Composable
fun ButtonTest(){


}