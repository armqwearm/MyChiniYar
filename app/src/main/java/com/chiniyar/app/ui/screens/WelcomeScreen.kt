package com.chiniyar.app.ui.screens

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.ui.res.painterResource
import com.chiniyar.app.R
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chiniyar.app.data.preferences.OnboardingPreferences
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.platform.LocalLayoutDirection

private data class WelcomePage(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val title: String,
    val description: String
)

@Composable
fun WelcomeScreen(
    preferences: OnboardingPreferences,
    onFinished: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var pageIndex by rememberSaveable { mutableIntStateOf(0) }

    val pages = listOf(
        WelcomePage(
            Icons.Default.TravelExplore,
            "چینی‌یار، همراه شما در سفر به چین",
            "ترجمه، عبارات کاربردی، فرهنگ لغت و ابزارهای سفر؛ همه در یک برنامه."
        ),
        WelcomePage(
            Icons.Default.CameraAlt,
            "ترجمه را همیشه همراهت داشته باش",
            "با مترجم متنی و تصویری، متن‌های چینی را حتی در سفر و در حالت آفلاین ترجمه کن."
        ),
        WelcomePage(
            Icons.Default.AutoAwesome,
            "چینی را بهتر یاد بگیر",
            "عبارات سفر، تلفظ، بانک لغات و منابع یادگیری کمک می‌کنند بعد از سفر هم از برنامه استفاده کنی."
        )
    )

    fun finish() {
        scope.launch {
            preferences.markCompleted()
            onFinished()
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Box(modifier = Modifier.fillMaxSize()) {
            TravelBackground(modifier = Modifier.fillMaxSize(), alpha = 0.72f)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.18f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 22.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(18.dp))

                Text(
                    "چینی‌یار",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF173B4B)
                )
                Text(
                    "همراه هوشمند سفر به چین 🇨🇳",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF4B6269),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(30.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(30.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.90f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp, vertical = 30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(92.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.foundation.Image(
                                painter = painterResource(R.drawable.chiniyar_icon_final),
                                contentDescription = "آیکن چینی‌یار",
                                modifier = Modifier.size(74.dp)
                            )
                        }

                        Spacer(Modifier.height(28.dp))

                        Text(
                            pages[pageIndex].title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF173B4B),
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(14.dp))

                        Text(
                            pages[pageIndex].description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF50656B),
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(26.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            pages.indices.forEach { index ->
                                Box(
                                    modifier = Modifier
                                        .size(if (index == pageIndex) 22.dp else 8.dp)
                                        .background(
                                            if (index == pageIndex) MaterialTheme.colorScheme.primary
                                            else Color(0xFFB7C5C9),
                                            CircleShape
                                        )
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (pageIndex < pages.lastIndex) pageIndex++ else finish()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        if (pageIndex < pages.lastIndex) "ادامه" else "شروع کنیم",
                        fontWeight = FontWeight.Bold
                    )
                }

                if (pageIndex < pages.lastIndex) {
                    TextButton(onClick = { finish() }) {
                        Text("فعلاً رد کن")
                    }
                } else {
                    Spacer(Modifier.height(36.dp))
                }
            }
        }
    }
}
