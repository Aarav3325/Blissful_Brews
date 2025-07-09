package com.brews.cafeorderingapp.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout


@Preview(showBackground = true)
@Composable
fun CategoryScreen(){

    var showCategoryList by remember {
        mutableStateOf(false)
    }


    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {

        val (fab, category) = createRefs();

        CategoryFAB(
              modifier = Modifier.constrainAs(fab) {
                bottom.linkTo(parent.bottom, margin = 36.dp)
                end.linkTo(parent.end, margin = 36.dp)
            },
            onClick = {
                showCategoryList = !showCategoryList
            }
        )


        AnimatedVisibility(visible = showCategoryList, modifier = Modifier
            .constrainAs(category){
            bottom.linkTo(fab.top, margin = 8.dp)
            end.linkTo(fab.start, margin = 0.dp)
        }) {
            //CategoryDisplay(modifier = Modifier, viewModel = v)

        }


    }

}