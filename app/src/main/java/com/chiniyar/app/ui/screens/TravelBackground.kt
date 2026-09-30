package com.chiniyar.app.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.chiniyar.app.R

/**
 * Safe bundled travel background.
 *
 * The image is decoded directly with BitmapFactory instead of painterResource,
 * avoiding BitmapDrawable casts and resource-resolution crashes.
 */
@Composable
fun TravelBackground(
    modifier: Modifier = Modifier,
    alpha: Float = 0.25f
) {
    val context = LocalContext.current
    val bitmap = remember(context) {
        runCatching {
            BitmapFactory.decodeResource(
                context.resources,
                R.drawable.yajing_travel_background_actual
            )?.takeIf { !it.isRecycled }?.asImageBitmap()
        }.getOrNull()
    }

    Box(
        modifier = modifier.background(
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFFFFBF2),
                    Color(0xFFF6F1E6),
                    Color(0xFFEAF3F4)
                )
            )
        )
    ) {
        bitmap?.let {
            Image(
                bitmap = it,
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop,
                alpha = alpha
            )
        }
    }
}
