package com.chiniyar.app

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.chiniyar.app.data.preferences.OnboardingPreferences
import com.chiniyar.app.ui.navigation.AppNavHost
import com.chiniyar.app.ui.screens.TravelBackground
import com.chiniyar.app.ui.theme.MyChiniYarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val crashPrefs = getSharedPreferences(ChiniYarApplication.CRASH_PREFS, MODE_PRIVATE)
        val previousCrash = crashPrefs.getString(ChiniYarApplication.CRASH_TRACE_KEY, null)
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager

        if (previousCrash != null) {
            setContent {
                MyChiniYarTheme {
                    StartupCrashScreen(
                        report = previousCrash,
                        onContinue = {
                            crashPrefs.edit().remove(ChiniYarApplication.CRASH_TRACE_KEY).commit()
                            recreate()
                        },
                        onCopy = {
                            clipboardManager?.setPrimaryClip(android.content.ClipData.newPlainText("ChiniYar crash report", previousCrash))
                        }
                    )
                }
            }
            return
        }

        val app = application as ChiniYarApplication
        val onboardingPreferences = OnboardingPreferences(this)

        setContent {
            MyChiniYarTheme {
                var onboardingCompleted by remember { mutableStateOf<Boolean?>(null) }

                LaunchedEffect(Unit) {
                    val packageInfo = packageManager.getPackageInfo(packageName, 0)
                    val isFreshInstall = packageInfo.firstInstallTime == packageInfo.lastUpdateTime
                    val completed = onboardingPreferences.isCompleted()

                    onboardingCompleted = if (!completed && !isFreshInstall) {
                        onboardingPreferences.markCompleted()
                        true
                    } else {
                        completed
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
                        TravelBackground(
                            modifier = Modifier.fillMaxSize(),
                            alpha = 0.25f
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White.copy(alpha = 0.08f))
                        )

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

@Composable
private fun StartupCrashScreen(
    report: String,
    onContinue: () -> Unit,
    onCopy: () -> Unit
) {
    val scrollState = rememberScrollState()
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "گزارش خطای اجرای چینی‌یار",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "برنامه در اجرای قبلی با خطا بسته شده است. این گزارش علت واقعی خطا را نشان می‌دهد.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )
            HorizontalDivider()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFF1F1F1))
                    .padding(10.dp)
            ) {
                Text(
                    report,
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.verticalScroll(scrollState)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onCopy,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("کپی گزارش")
                }
                OutlinedButton(
                    onClick = onContinue,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("ادامه اجرای برنامه")
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(
                "گزارش را کپی کنید و همینجا ارسال کنید.",
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
