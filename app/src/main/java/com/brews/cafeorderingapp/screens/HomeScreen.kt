package com.brews.cafeorderingapp.screens

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import coil.decode.ImageDecoderDecoder
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat.startActivity
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.LocalImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.brews.cafeorderingapp.R
import com.brews.cafeorderingapp.activity.CartActivity
import com.brews.cafeorderingapp.activity.OrderSummary
import com.brews.cafeorderingapp.activity.OrderTrackingActivity
import com.brews.cafeorderingapp.activity.ui.theme.poppins
import com.brews.cafeorderingapp.repository.UserUtil
import com.brews.cafeorderingapp.viewmodel.CartViewModel
import com.brews.cafeorderingapp.viewmodel.MyViewModel
import com.bumptech.glide.gifdecoder.GifDecoder
import com.google.firebase.auth.FirebaseAuth
import kotlin.jvm.java


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(viewModel: MyViewModel, cartViewModel: CartViewModel) {

    val menuData by viewModel.menuData.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMenuItems()
        cartViewModel.callCategoryList()
    }

    val categoryGridList = cartViewModel.getCategoryList()

    val hasCurrentOrder by cartViewModel.hasCurrentOrder.collectAsState()
    val currentOrderId by cartViewModel.currentOrderId.collectAsState()

    LaunchedEffect(hasCurrentOrder) {
        cartViewModel.fetchCurrentOrder()
        Log.i("Track", hasCurrentOrder.toString())
    }


    var selectedCategory by remember { mutableStateOf<String?>("") }


    val categoryItemList = menuData?.keys?.toList()

    var showCategoryList by remember {
        mutableStateOf(false)
    }

    var searchText by remember {
        mutableStateOf("")
    }

    val scrollState = rememberLazyListState()

    val searchCardHeight by remember {
        derivedStateOf {
            // Reduce the height by the scroll offset, but don't go below a certain minimum
            val minHeight = 150.dp
            val maxHeight = 400.dp
            val offset = scrollState.firstVisibleItemScrollOffset
            val newHeight = maxHeight - offset.dp
            if (newHeight < minHeight) minHeight else newHeight
        }
    }

    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    val showSearchBg by remember {
            derivedStateOf {
                scrollState.firstVisibleItemIndex > 0 || scrollState.firstVisibleItemScrollOffset > 800
            }
    }

    LaunchedEffect(Unit) {
        cartViewModel.fetchCart(UserUtil.getUserId())
    }


    val cartList by cartViewModel.firebaseCart.collectAsState()

    var showCartBtn by remember { mutableStateOf(false) }

    LaunchedEffect(cartList) {
        Log.d("CartDebug", "Recomposition triggered: ${cartList.size}")
        showCartBtn = cartList.isNotEmpty()
    }





//    LaunchedEffect(cartList) {
//        if(cartList.isEmpty()){
//            showCartBtn = false
//        }
//        else{
//            showCartBtn = true
//        }
//    }



    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFDEE7E7))
    ) {
        val (fab, categoryList, card, menuList, searchCard, cartBtn) = createRefs()
        val barrier = createStartBarrier(fab)
//        SearchCard(
//            modifier = Modifier.constrainAs(card) {
//                top.linkTo(parent.top, margin = -18.dp)
//                start.linkTo(parent.start)
//                end.linkTo(parent.end)
//            }, onSearchTextChange = { searchText = it },
//            searchText = searchText
//        )

        AnimatedVisibility(
            visible = showCartBtn,
            modifier = Modifier.zIndex(1f).constrainAs(cartBtn){
                start.linkTo(parent.start)
                end.linkTo(barrier)
                bottom.linkTo(parent.bottom, margin = 32.dp)

            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .animateContentSize()
            ) {

                val context = LocalContext.current

                FilledTonalButton(
                    onClick = {
                        val i = Intent(context, CartActivity::class.java)
                        context.startActivity(i)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA93F55))
                ) {
                    Text("Go to Cart", fontSize = 18.sp, color = Color.White)
                }
            }

        }
//        createHorizontalChain(
//            cartBtn,
//            fab,
//            chainStyle = ChainStyle.Spread
//        )


        Box(
            modifier = Modifier
                .constrainAs(searchCard) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .zIndex(1f) // Bring it above the list
        ) {
            CompactSearchCard(
                searchText = searchText,
                showBackground = showSearchBg,
                onSearchTextChange = { searchText = it },
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }

        val context = LocalContext.current

        val imageLoader = ImageLoader.Builder(context)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(coil.decode.GifDecoder.Factory())
                }
            }
            .build()


        Box(modifier = Modifier.constrainAs(menuList) {
            top.linkTo(searchCard.top, margin = 0.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }) {


            menuData?.let { menuMap ->
                Log.i("MYTAG", menuMap.toString())
                LazyColumn(
                    state = scrollState,
                    modifier = Modifier.padding(bottom = 0.dp)
                ) {

                    item {
                        Box(modifier = Modifier.height(400.dp).fillMaxWidth()) {
                            Image(
                                painter = painterResource(id = R.drawable.home),
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .fillMaxWidth().fillMaxHeight()
                            )

                            Box(modifier = Modifier.fillMaxSize()){
                                Image(
                                    painter = painterResource(id = R.drawable.offer),
                                    contentDescription = null,
//                                    imageLoader = imageLoader,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.align(Alignment.BottomCenter)
                                )
                            }
                        }

                    }

                      item {
                          if(hasCurrentOrder){
                              OrderTrackingCard(orderID = currentOrderId)
                          }
                      }

                    item{
                        CategoryGrid(categoryGridList)
                    }


                    menuMap.forEach { (category, items) ->

                        item {
                            Text(
                                text = category,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                fontFamily = poppins,
                                modifier = Modifier.padding(
                                    top = 12.dp,
                                    start = 12.dp,
                                    end = 8.dp,
                                    bottom = 12.dp
                                )
                            )
                        }

                        items(items) { item ->
                            Log.i("MYTAG", item.itemName)
                            MenuItem(item, cartViewModel, viewModel)
                        }


                    }


                }
            } ?: Box(
                modifier = Modifier.fillMaxSize()
            ){
                AsyncImage(
                    model = R.drawable.loader,
                    contentDescription = null,
                    imageLoader = imageLoader,
                    //colorFilter = ColorFilter.tint(Color.Green),
                    modifier = Modifier.size(56.dp).align(Alignment.Center)
                )
            }


        //Text(text = "Loading")

        }

        CategoryFAB(
            modifier = Modifier.constrainAs(fab) {
                bottom.linkTo(parent.bottom, margin = 32.dp)
                end.linkTo(parent.end, margin = 32.dp)
            },
            onClick = {
                showCategoryList = !showCategoryList
            }
        )

        AnimatedVisibility(
            visible = showCategoryList,
            modifier = Modifier
                .shadow(6.dp, spotColor = Color.Black, shape = RoundedCornerShape(12.dp)).constrainAs(categoryList) {
                bottom.linkTo(fab.top, margin = 8.dp)
                end.linkTo(fab.start, margin = 0.dp)
            }) {

            CategoryDisplay(
                modifier = Modifier,
                categoryList = categoryItemList!!,
                menuData = menuData!!,
                onCategoryClick = { clickedCategory ->
                    selectedCategory = clickedCategory
                })
    
        }

        LaunchedEffect(selectedCategory) {
            menuData?.let { menuMap ->
                val categoryIndex = menuMap.keys.indexOf(selectedCategory)
                if (categoryIndex != -1) {
                    val firstItemIndex = menuMap.keys
                        .take(categoryIndex)
                        .sumOf { menuMap[it]?.size ?: 0 } + categoryIndex

                    Log.i(
                        "MYTAG",
                        "Scrolling to first item of $selectedCategory at index $firstItemIndex"
                    )
                    showCategoryList = false
                    scrollState.animateScrollToItem(firstItemIndex)
                }
            }
        }


    }
}

@Composable
fun SearchCard(
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
        ) {
            val (searchBar, avatar, banner) = createRefs()

            Image(
                painter = painterResource(id = R.drawable.promo),
                contentDescription = "Banner",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(banner) {
                        top.linkTo(searchBar.bottom, margin = 12.dp)
                        bottom.linkTo(parent.bottom, margin = 12.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
                    .size(250.dp)
            )
        }
    }

}

@Composable
fun CompactSearchCard(
    modifier: Modifier = Modifier,
    searchText: String,
    showBackground: Boolean,
    onSearchTextChange: (String) -> Unit
) {

    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = if (showBackground) Color(0xFF065242) else Color.Transparent),
        elevation = CardDefaults.cardElevation(if (showBackground) 8.dp else 0.dp),
        //colors = CardDefaults.cardColors(containerColor = Color(0xFFD0DDD0)),
        //colors = CardDefaults.cardColors(containerColor = Color(0xFF065242)),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
        ) {


            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                val (searchBar, avatar, banner, bg) = createRefs()



                TextField(
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    leadingIcon = {Image(imageVector = Icons.Filled.Search, contentDescription = null)},
                    modifier = Modifier
                        .constrainAs(searchBar) {
                            top.linkTo(parent.top, margin = 56.dp)
                            start.linkTo(parent.start, margin = 12.dp)
                            end.linkTo(avatar.start, margin = 8.dp)
                            width = Dimension.fillToConstraints //
                        }
                        .height(55.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.White
                    ),
                    placeholder = { Text("Search menu...") }
                )

                Box(
                    modifier = Modifier
                        .constrainAs(avatar) {
                            top.linkTo(searchBar.top)
                            bottom.linkTo(searchBar.bottom)
                            start.linkTo(searchBar.end, margin = 6.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                        }
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color.White).clickable{
                            context.startActivity(Intent(context, ProfileActivity::class.java))
                        }
                ) {
                    AsyncImage(
                        model = FirebaseAuth.getInstance().currentUser?.photoUrl,
                        contentDescription = "avatar",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxSize()
                    )
                }

            }
        }
    }

}

@Composable
fun PromoBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.horizontalGradient(listOf(Color(0xFFff6e7f), Color(0xFFbfe9ff))))
    ) {
        Text(
            text = "🎉 20% OFF on Beverages!",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


