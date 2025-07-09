package com.brews.cafeorderingapp.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.brews.cafeorderingapp.R
import com.brews.cafeorderingapp.activity.ui.theme.poppins
import com.brews.cafeorderingapp.model.FavouriteDish
import com.brews.cafeorderingapp.repository.CartRepository
import com.brews.cafeorderingapp.screens.ui.theme.CafeOrderingAppTheme
import com.brews.cafeorderingapp.viewmodel.CartViewModel
import com.brews.cafeorderingapp.viewmodel.MyViewModel

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel = ViewModelProvider(this)[MyViewModel::class.java]

        val cartRepository = CartRepository()
        val cartViewModel = CartViewModel(cartRepository)
        setContent {
            CafeOrderingAppTheme {


                val orderList = cartViewModel.orderList
                val favouriteDishesList = viewModel.allFavouriteDishes.observeAsState()

                LaunchedEffect(Unit) {
                    cartViewModel.getRecentOrders()
                }

                val navController = rememberNavController()

                NavHost(navController, startDestination = "profile") {
                    composable("profile") {
                        ProfileScreen(navController)
                    }

                    composable("faq") {
                        FaqScreen(navController)
                    }

                    composable("about") {
                        AboutScreen(navController)
                    }

                    composable("recent_orders") {
                        RecentOrdersScreen(navController, orderList)
                    }

                    composable("loyalty_program") {
                        LoyaltyProgramScreen(navController)
                    }

                    composable("favourite_dishes") {
                        FavouriteScreen(navController, viewModel)
                    }
                }

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

//@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CafeOrderingAppTheme {
        Greeting("Android")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaqScreen(navController: NavController) {
    val faqList = listOf(
        "How do I place an order?" to "Browse the menu, select items, and tap 'Add to Cart'. Then, go to the cart and confirm your order.",
        "How can I track my order?" to "Go to the 'My Orders' section in the profile. You'll see real-time status updates.",
        "What payment methods are supported?" to "We support UPI, credit/debit cards, and cash on delivery.",
        "Can I cancel my order?" to "Yes, you can cancel your order within 5 minutes of placing it.",
        "What if I receive the wrong item?" to "You can contact support through the 'Help & Support' section. We’ll fix it ASAP.",
        "Is there a delivery charge?" to "Delivery is free for orders above ₹199. Otherwise, a small delivery fee applies.",
        "How can I give feedback?" to "Use the 'Rate Us' option in the profile to share your feedback."
    )

    Scaffold(
        containerColor = Color(0xFFDEE7E7),
        topBar = {
            TopAppBar(
                title = { Text("FAQs", fontFamily = poppins) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFDEE7E7)),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            items(faqList) { faq ->
                ExpandableFaqItem(question = faq.first, answer = faq.second)
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}

@Composable
fun ExpandableFaqItem(question: String, answer: String) {

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .padding(8.dp)
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        AnimatedVisibility(visible = expanded) {
            Text(
                text = answer,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavController) {
    val context = LocalContext.current
    val appVersion = try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName
    } catch (e: Exception) {
        "N/A"
    }

    Scaffold(
        containerColor = Color(0xFFDEE7E7),
        topBar = {
            TopAppBar(
                title = { Text("About Us", fontFamily = poppins) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFDEE7E7)),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Logo
            Image(
                painter = painterResource(id = R.drawable.logo_circular), // Replace with your drawable
                contentDescription = "App Logo",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Cafe Ordering App",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                fontFamily = poppins
            )

            Text(
                text = "Version $appVersion",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp,
                fontFamily = poppins
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "This app allows you to browse cafe menu items, place orders, and track them in real-time. Built with modern Android technologies like Jetpack Compose and Firebase.",
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 22.sp,
                textAlign = TextAlign.Justify,
                fontFamily = poppins
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Developed by Aarav Halvadiya",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = poppins,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Contact: aaravhalvadiya@gmail.com", // Change or link to feedback
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = poppins,
                fontSize = 14.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoyaltyProgramScreen(navController: NavController) {


    Scaffold(
        containerColor = Color(0xFFDEE7E7),
        topBar = {
            TopAppBar(
                title = { Text("Loyalty Program", fontFamily = poppins) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFDEE7E7)),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                Text(
                    text = "Coming Soon",
                    fontSize = 24.sp,
                    fontFamily = poppins,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    )
}

@Composable
fun FavouriteItemCard(favItem : FavouriteDish, viewModel : MyViewModel){

    var isFavourite = viewModel.isDishFavourite(favItem.dishPrice).observeAsState();


    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4FAFF)),
        modifier = Modifier.fillMaxWidth().padding(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp, bottom = 12.dp, top = 6.dp)
        ) {


            Row(
                horizontalArrangement = Arrangement.Start,
            ) {
                AsyncImage(
                    model = favItem.dishImage,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = favItem.dishName,
                    fontFamily = poppins,
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                Box(modifier = Modifier.fillMaxWidth()
                ){
                    Image(
                        painter = painterResource(id = if (isFavourite.value == true) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                            .align(Alignment.TopEnd).clickable{
                                viewModel.deleteFavouriteDish(FavouriteDish(favItem.dishName, favItem.dishImage, favItem.dishPrice, favItem.dishDescription, favItem.dishPrice))
                            }
                    )
                }

            }

            Spacer(modifier = Modifier.height(8.dp))

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFFA6AEBF))
            )

            Spacer(modifier = Modifier.height(8.dp))


            Text(
                text = favItem.dishDescription,
                fontFamily = poppins,
                fontSize = 14.sp,
                color = Color.Black,
                textAlign = TextAlign.Justify,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.align(Alignment.Start)
            )


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteScreen(navController: NavController, viewModel: MyViewModel) {

    val favouriteDishesList: List<FavouriteDish> by viewModel.allFavouriteDishes.observeAsState(
        emptyList()
    )


    Scaffold(
        containerColor = Color(0xFFDEE7E7),
        topBar = {
            TopAppBar(
                title = { Text("Favourite Dishes", fontFamily = poppins) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFDEE7E7)),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = {
            if(favouriteDishesList.isEmpty()){
                Box(
                    modifier = Modifier.fillMaxSize()
                ){
                    Text(
                        text = "Add some dishes to the list of favourite dishes",
                        textAlign = TextAlign.Center,
                        fontFamily = poppins,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            else{
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                ) {

                    items(favouriteDishesList) { item ->
                        FavouriteItemCard(item, viewModel)
                    }
                }
            }
        }
    )
}









