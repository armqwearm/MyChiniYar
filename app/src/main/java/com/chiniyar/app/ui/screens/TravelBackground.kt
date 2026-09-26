package com.chiniyar.app.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
 * Renders the travel background when the device can decode the bundled WebP.
 * A safe Compose-only gradient fallback is used when decoding fails, so a
 * broken/unsupported bitmap can never crash application startup.
 */
@Composable
fun TravelBackground(
    modifier: Modifier = Modifier,
    alpha: Float = 0.84f
) {
    val context = LocalContext.current
    val bitmap = remember(context) {
        runCatching {
            BitmapFactory.decodeResource(
                context.resources,
                R.drawable.yajing_travel_background_final
            )?.takeIf { !it.isRecycled }?.asImageBitmap()
        }.getOrNull()
    }

    BoxWithConstraints(
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
