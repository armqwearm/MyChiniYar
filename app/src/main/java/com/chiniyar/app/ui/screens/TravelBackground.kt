package com.chiniyar.app.ui.screens

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

private val TRAVEL_BACKGROUND_PARTS = listOf(
    "china_travel_bg_00.b64",
    "china_travel_bg_01.b64",
    "china_travel_bg_02.b64"
)

@Composable
fun TravelBackground(
    modifier: Modifier = Modifier,
    alpha: Float = 0.28f
) {
    val context = LocalContext.current
    val bitmap = remember {
        runCatching {
            val encoded = buildString {
                TRAVEL_BACKGROUND_PARTS.forEach { fileName ->
                    context.assets.open(fileName).bufferedReader().use { append(it.readText()) }
                }
            }
            val bytes = Base64.decode(encoded, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }.getOrNull()
    }

    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Crop,
            alpha = alpha
        )
    }
}
