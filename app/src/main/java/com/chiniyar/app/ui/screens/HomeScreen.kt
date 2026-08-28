package com.chiniyar.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private data class HomeFeature(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val action: () -> Unit
)

@Composable
fun HomeScreen(
    onTranslatorClick: () -> Unit,
    onCameraClick: () -> Unit,
    onLearningClick: () -> Unit,
    onCitiesClick: () -> Unit
) {
    val features = listOf(
        HomeFeature("مترجم", "ترجمه سریع چینی و فارسی", Icons.Default.Translate, onTranslatorClick),
        HomeFeature("مترجم تصویری", "استخراج و ترجمه متن عکس", Icons.Default.CameraAlt, onCameraClick),
        HomeFeature("یادگیری چینی", "Hanzi، Pinyin و واژگان", Icons.Default.Book, onLearningClick),
        HomeFeature("شهرهای چین", "راهنمای شهرها و اطلاعات کاربردی", Icons.Default.LocationCity, onCitiesClick)
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(Modifier.height(20.dp))
        Text("چینی‌یار", style = MaterialTheme.typography.headlineLarge)
        Text(
            "یادگیری، ترجمه و شناخت چین در یک برنامه",
            style = MaterialTheme.typography.bodyLarge
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentPadding = PaddingValues(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(features) { feature ->
                Card(onClick = feature.action, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Icon(feature.icon, contentDescription = feature.title)
                        Text(feature.title, style = MaterialTheme.typography.titleMedium)
                        Text(feature.description, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
