package com.chiniyar.app.ui.screens.routes

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsSubway
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private const val METROMAN_PLAY_URL = "https://play.google.com/store/apps/details?id=com.xinlukou.metroman"
private const val METROMAN_WEB_URL = "https://www.metroman.cn/en/apps"

@Composable
fun RoutesScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme

    fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            context.startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            // No browser/Play Store available; keep the screen usable without crashing.
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("مسیرهای شهری", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "بازگشت")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(padding)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(Modifier.height(4.dp))

            Text(
                "حمل‌ونقل در شهرهای چین",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Right
            )
            Text(
                "برای پیدا کردن مسیر مترو، زمان سفر و ایستگاه‌های مناسب، فعلاً MetroMan را به‌عنوان راهنمای مترو معرفی می‌کنیم.",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant,
                textAlign = TextAlign.Right
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = colors.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Default.DirectionsSubway,
                            contentDescription = null,
                            tint = colors.primary,
                            modifier = Modifier.size(42.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text("MetroMan China", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                            Text("راهنمای مترو چین", style = MaterialTheme.typography.bodyMedium, color = colors.onPrimaryContainer)
                        }
                    }

                    Text(
                        "• نقشه‌ها و اطلاعات مترو شهرهای چین\n" +
                            "• برنامه‌ریزی مسیر با زمان و هزینه سفر\n" +
                            "• امکان استفاده آفلاین\n" +
                            "• مناسب برای مسافران و کاربران انگلیسی‌زبان",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onPrimaryContainer
                    )

                    Button(
                        onClick = { openUrl(METROMAN_PLAY_URL) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.OpenInNew, contentDescription = null)
                        Spacer(Modifier.size(8.dp))
                        Text("نصب از Google Play")
                    }

                    OutlinedButton(
                        onClick = { openUrl(METROMAN_WEB_URL) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Language, contentDescription = null)
                        Spacer(Modifier.size(8.dp))
                        Text("وب‌سایت رسمی MetroMan")
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceVariant)
            ) {
                Text(
                    "نکته: MetroMan یک برنامه مستقل است و لینک‌های بالا به منابع رسمی آن اشاره می‌کنند. اطلاعات خطوط مترو ممکن است با تغییرات شبکه به‌روزرسانی شود.",
                    modifier = Modifier.padding(15.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant,
                    textAlign = TextAlign.Right
                )
            }
        }
    }
}
