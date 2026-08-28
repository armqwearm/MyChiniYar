package com.chiniyar.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.chiniyar.app.di.AppContainer
import com.chiniyar.app.ui.navigation.AppNavHost
import com.chiniyar.app.ui.theme.MyChiniYarTheme

class MainActivity : ComponentActivity() {
    private val appContainer by lazy { AppContainer() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyChiniYarTheme {
                AppNavHost(
                    navController = rememberNavController(),
                    appContainer = appContainer
                )
            }
        }
    }
}
