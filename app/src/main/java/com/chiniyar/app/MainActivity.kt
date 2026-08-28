package com.chiniyar.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MyChiniYarApp() }
    }
}

private data class Feature(
    val title: String,
    val subtitle: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
private fun MyChiniYarApp() {
    val features = listOf(
        Feature("مترجم", "ترجمه متن چینی و فارسی", Icons.Default.Translate),
        Feature("مترجم تصویری", "تشخیص متن از عکس با OCR", Icons.Default.CameraAlt),
        Feature("یادگیری چینی", "Hanzi، Pinyin و واژگان", Icons.Default.Book),
        Feature("شهرهای چین", "شهرها و اطلاعات کاربردی", Icons.Default.LocationCity)
    )

    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            Text("چینی‌یار", style = MaterialTheme.typography.headlineLarge)
            Text(
                "همراه هوشمند شما برای یادگیری زبان چینی و شناخت چین",
                style = MaterialTheme.typography.bodyLarge
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentPadding = PaddingValues(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(features) { feature -> FeatureCard(feature) }
            }
        }
    }
}

@Composable
private fun FeatureCard(feature: Feature) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(feature.icon, contentDescription = feature.title)
                Spacer(Modifier.padding(horizontal = 5.dp))
                Text(feature.title, style = MaterialTheme.typography.titleMedium)
            }
            Text(feature.subtitle, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
