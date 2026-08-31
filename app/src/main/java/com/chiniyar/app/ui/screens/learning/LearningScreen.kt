package com.chiniyar.app.ui.screens.learning

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private const val WEBSITE_URL = "https://yajingchinese.ir/"
private const val TELEGRAM_URL = "https://t.me/yajingchinese"
private const val BALE_URL = "https://ble.ir/Yajing_chinese"

@Composable
fun LearningScreen(onBack: () -> Unit) {
    val context = LocalContext.current

    fun openUrl(url: String) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("یادگیری زبان چینی") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "بازگشت")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                "منابع و مسیر یادگیری زبان چینی",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                "برای یادگیری عمیق‌تر زبان چینی، می‌توانید از مجموعه آموزشی یاجینگ چینی استفاده کنید.",
                style = MaterialTheme.typography.bodyLarge
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("یادگیری زبان چینی با یاجینگ چینی", style = MaterialTheme.typography.titleLarge)
                    Text(
                        "وب‌سایت آموزشی و کانال‌های ارتباطی یاجینگ چینی را از اینجا دنبال کنید.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Button(
                        onClick = { openUrl(WEBSITE_URL) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Language, contentDescription = null)
                        Spacer(Modifier.padding(horizontal = 4.dp))
                        Text("وب‌سایت یاجینگ چینی")
                    }

                    OutlinedButton(
                        onClick = { openUrl(TELEGRAM_URL) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null)
                        Spacer(Modifier.padding(horizontal = 4.dp))
                        Text("کانال تلگرام")
                    }

                    OutlinedButton(
                        onClick = { openUrl(BALE_URL) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null)
                        Spacer(Modifier.padding(horizontal = 4.dp))
                        Text("کانال بله")
                    }
                }
            }
        }
    }
}
