package com.chiniyar.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DirectionsSubway
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private data class HomeFeature(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val action: () -> Unit,
    val accent: Color
)

private const val RoseKaboodUrl = "https://rosekabood.com"

@Composable
fun HomeScreen(
    onTranslatorClick: () -> Unit,
    onDictionaryClick: () -> Unit,
    onCameraClick: () -> Unit,
    onVocabularyBankClick: () -> Unit,
    onTravelPhrasesClick: () -> Unit,
    onLearningClick: () -> Unit,
    onCitiesClick: () -> Unit,
    onRoutesClick: () -> Unit,
    onUrbanRoutesClick: () -> Unit,
    onExhibitionsClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val context = LocalContext.current
    val features = listOf(
        HomeFeature("مترجم متنی", "چینی ↔ فارسی، سریع و آفلاین", Icons.Default.Translate, onTranslatorClick, colors.primary),
        HomeFeature("عبارات سفر", "۳۰ عبارت ضروری + تلفظ صوتی", Icons.Default.FlightTakeoff, onTravelPhrasesClick, colors.tertiary),
        HomeFeature("مترجم تصویری", "عکس بگیر، متن را استخراج و ترجمه کن", Icons.Default.CameraAlt, onCameraClick, colors.primary),
        HomeFeature("بانک لغات من", "واژه‌های مورد علاقه را ذخیره کن", Icons.Default.Star, onVocabularyBankClick, colors.tertiary),
        HomeFeature("یادگیری چینی", "واژگان، آموزش و منابع مفید", Icons.Default.Book, onLearningClick, colors.secondary),
        HomeFeature("شهرهای چین", "۲۰ شهر معروف و راهنمای سفر", Icons.Default.LocationCity, onCitiesClick, colors.secondary),
        HomeFeature("مسیرهای شهری", "راهنمای مترو و رفت‌وآمد در چین", Icons.Default.DirectionsSubway, onUrbanRoutesClick, colors.primary),
        HomeFeature("نمایشگاه‌های چین", "تقویم نمایشگاه‌های مهم + راهنمای بازدید", Icons.Default.Event, onExhibitionsClick, colors.tertiary)
    )

    Box(modifier = Modifier.fillMaxSize().background(colors.background)) {
        TravelBackground(modifier = Modifier.fillMaxSize(), alpha = 0.28f)
        Box(modifier = Modifier.fillMaxSize().background(colors.background.copy(alpha = 0.18f)))
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Spacer(Modifier.height(14.dp))
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(26.dp), colors = CardDefaults.cardColors(containerColor = colors.primaryContainer), elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)) {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("چینی‌یار", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = colors.primary)
                        Text("یادگیری چینی • سفر به چین • کشف بیشتر", style = MaterialTheme.typography.bodyMedium, color = colors.onPrimaryContainer)
                    }
                    Text("🏮", style = MaterialTheme.typography.headlineLarge)
                }
            }
            Text("همراه هوشمند شما برای چین", modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Right)
            Text("ترجمه، واژه‌ها و اطلاعات سفر؛ حتی وقتی اینترنت در دسترس نیست.", modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodyMedium, color = colors.onSurfaceVariant, textAlign = TextAlign.Right)
            LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.fillMaxWidth().weight(1f), contentPadding = PaddingValues(vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(features) { feature ->
                    Card(onClick = feature.action, modifier = Modifier.fillMaxWidth().border(1.dp, feature.accent.copy(alpha = 0.16f), RoundedCornerShape(22.dp)), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = colors.surface), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                        Column(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.size(42.dp).background(feature.accent.copy(alpha = 0.12f), RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                                Icon(feature.icon, contentDescription = feature.title, tint = feature.accent, modifier = Modifier.size(23.dp))
                            }
                            Text(feature.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(feature.description, style = MaterialTheme.typography.bodySmall, color = colors.onSurfaceVariant)
                        }
                    }
                }
            }
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = colors.secondaryContainer)) {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("🌹", style = MaterialTheme.typography.titleLarge)
                    Column(modifier = Modifier.weight(1f)) {
                        Text("آماده سفر به چین هستی؟", fontWeight = FontWeight.Bold)
                        Text("آژانس مسافرتی رز کبود • برنامه‌ریزی و خدمات سفر", style = MaterialTheme.typography.bodySmall)
                        Text("rosekabood.com  ›", style = MaterialTheme.typography.labelMedium, color = colors.primary)
                    }
                    IconButton(onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(RoseKaboodUrl))) }) {
                        Icon(Icons.Default.TravelExplore, contentDescription = "وب‌سایت رز کبود", tint = colors.primary)
                    }
                }
            }
            Spacer(Modifier.height(6.dp))
        }
    }
}