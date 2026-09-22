package com.chiniyar.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.navigation.compose.rememberNavController
import com.chiniyar.app.data.preferences.OnboardingPreferences
import com.chiniyar.app.ui.navigation.AppNavHost
import com.chiniyar.app.ui.theme.MyChiniYarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as ChiniYarApplication
        val onboardingPreferences = OnboardingPreferences(this)

        setContent {
            MyChiniYarTheme {
                var onboardingCompleted by remember { mutableStateOf<Boolean?>(null) }

                LaunchedEffect(Unit) {
                    val packageInfo = packageManager.getPackageInfo(packageName, 0)
                    val isFreshInstall = packageInfo.firstInstallTime == packageInfo.lastUpdateTime
                    val completed = onboardingPreferences.isCompleted()

                    // Existing installs/upgrades should not be interrupted by new onboarding.
                    if (!completed && !isFreshInstall) {
                        onboardingPreferences.markCompleted()
                        onboardingCompleted = true
                    } else {
                        onboardingCompleted = completed
                    }
                }

                CompositionLocalProvider(
                    LocalLayoutDirection provides LayoutDirection.Rtl
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFFFFBF2)),
                        contentAlignment = Alignment.Center
                    ) {
                        when (onboardingCompleted) {
                            null -> CircularProgressIndicator()
                            else -> AppNavHost(
                                navController = rememberNavController(),
                                appContainer = app.appContainer,
                                showOnboarding = onboardingCompleted == false,
                                onboardingPreferences = onboardingPreferences
                            )
                        }
                    }
                }
            }
        }
    }
}
