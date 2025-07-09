package com.brews.cafeorderingapp.screens

import android.content.Intent
import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.brews.cafeorderingapp.R
import com.brews.cafeorderingapp.activity.LoginActivity
import com.brews.cafeorderingapp.activity.NewMenu
import com.brews.cafeorderingapp.activity.ui.theme.poppins
import com.brews.cafeorderingapp.repository.UserUtil


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Profile", fontFamily = poppins)},
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
            ConstraintLayout(
                modifier = Modifier
                    .padding(it)
                    .background(Color(0xFFDEE7E7))
                    .fillMaxSize()
            ) {

                var scrollRef = createRef()

                val context = LocalContext.current

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                        .verticalScroll(rememberScrollState())
                        .constrainAs(scrollRef) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                ) {

//                    Box(
//                        modifier = Modifier.padding(top = 48.dp)
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
//                            contentDescription = "back arrow",
//                            modifier = Modifier
//                                .size(28.dp)
//                                .clickable {
//                                    context.startActivity(Intent(context, NewMenu::class.java))
//                                }
//                        )
//                    }

//                    Spacer(modifier = Modifier.height(16.dp))

                    ProfileCard()

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F5F5)),
                        elevation = CardDefaults.cardElevation(1.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF6F5F5))
                        ) {
                            ProfileItem(
                                R.drawable.recent_orders,
                                "Recent Orders",
                                modifier = Modifier.clickable {
                                    navController.navigate("recent_orders")
                                })
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Color(0xFFA6AEBF))
                            )
                            ProfileItem(
                                R.drawable.heart,
                                "Favourite Dishes",
                                modifier = Modifier.clickable {
                                    navController.navigate("favourite_dishes")
                                })
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Color(0xFFA6AEBF))
                            )
                            ProfileItem(
                                R.drawable.loyalty_program,
                                "Loyalty Program",
                                modifier = Modifier.clickable {
                                    navController.navigate("loyalty_program")
                                })
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F5F5)),
                        elevation = CardDefaults.cardElevation(1.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF6F5F5))
                        ) {
                            ProfileItem(R.drawable.faq, "FAQs", modifier = Modifier.clickable {
                                navController.navigate("faq")
                            })
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Color(0xFFA6AEBF))
                            )
                            ProfileItem(R.drawable.about, "About Us", modifier = Modifier.clickable {
                                navController.navigate("about")
                            })
                        }
                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F5F5)),
                        elevation = CardDefaults.cardElevation(1.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF6F5F5))
                        ) {
                            ProfileItem(R.drawable.logout, "Logout", modifier = Modifier.clickable {
                                UserUtil.logout()
                                context.startActivity(Intent(context, LoginActivity::class.java))
                            })
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))


//            Text(
//                text = versionName,
//                fontFamily = poppins,
//                fontWeight = FontWeight.SemiBold,
//                fontSize = 18.sp,
//                textAlign = TextAlign.Center,
//                modifier = Modifier.fillMaxWidth()
//            )
                }
            }
        }
    )
}


@Composable
fun ProfileItem(activityIcon: Int, activityName: String, modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF6F5F5))
            .padding(12.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(activityIcon),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = activityName,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            fontFamily = poppins
        )

    }

//    Box(
//        modifier = Modifier.fillMaxWidth().padding(start = 12.dp, end = 12.dp)
//    ){
//        Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.Black))
//    }
}

//@Preview(showBackground = true)
@Composable
fun ProfileCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth().padding(top = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F5F5)),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = UserUtil.getProfilePic(),
                contentDescription = "user avatar",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = UserUtil.getUserName(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                fontFamily = poppins
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = UserUtil.getUserEmail(),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                fontFamily = poppins
            )
        }
    }
}

