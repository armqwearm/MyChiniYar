package com.chiniyar.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.chiniyar.app.R

@Composable
fun TravelBackground(
    modifier: Modifier = Modifier,
    alpha: Float = 0.52f
) {
    Image(
        painter = painterResource(R.drawable.yajing_travel_background),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        alpha = alpha
    )
}
