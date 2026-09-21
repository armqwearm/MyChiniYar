package com.chiniyar.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.chiniyar.app.ui.navigation.AppNavHost
import com.chiniyar.app.ui.screens.TravelBackground
import com.chiniyar.app.ui.theme.MyChiniYarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as ChiniYarApplication
        setContent {
            MyChiniYarTheme {
                Box(modifier = Modifier.fillMaxSize().background(Color(0xFFFFFBF2))) {
                    TravelBackground(
                        modifier = Modifier.fillMaxSize(),
                        alpha = 0.84f
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White.copy(alpha = 0.08f))
                    )
                    AppNavHost(
                        navController = rememberNavController(),
                        appContainer = app.appContainer
                    )
                }
            }
        }
    }
}
