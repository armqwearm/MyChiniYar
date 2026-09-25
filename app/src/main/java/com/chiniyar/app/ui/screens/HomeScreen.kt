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
import androidx.compose.material.icons.filled.MenuBook
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
    val tint: Color,
    val container: Color
)

private const val RoseKaboodUrl = "https://rosekabood.com"

@Composable
fun HomeScreen(
    onTranslatorClick: () -> Unit,
    onCultureClick: () -> Unit,
    onCameraClick: () -> Unit,
    onVocabularyBankClick: () -> Unit,
    onTravelPhrasesClick: () -> Unit,
    onLearningClick: () -> Unit,
    onCitiesClick: () -> Unit,
    onRoutesClick: () -> Unit,
    onUrbanRoutesClick: () -> Unit,
    onExhibitionsClick: () -> Unit
) {
    val context = LocalContext.current
    val blue = Color(0xFF1976A8)
    val purple = Color(0xFF7652C7)
    val red = Color(0xFFC62828)
    val green = Color(0xFF2E8B68)
    val gold = Color(0xFFB57900)
    val teal = Color(0xFF147F86)

    val features = listOf(
        HomeFeature("مترجم متنی", "چینی ↔ فارسی، سریع و آفلاین", Icons.Default.Translate, onTranslatorClick, blue, Color(0xFFDCEFFF)),
        HomeFeature("مترجم تصویری", "عکس بگیر، متن را استخراج و ترجمه کن", Icons.Default.CameraAlt, onCameraClick, purple, Color(0xFFEDE3FF)),
        HomeFeature("عبارات سفر", "۳۰ عبارت ضروری + تلفظ صوتی", Icons.Default.FlightTakeoff, onTravelPhrasesClick, red, Color(0xFFFFE1E3)),
        HomeFeature("بانک لغات من", "واژه‌های ذخیره‌شده و شخصی", Icons.Default.Star, onVocabularyBankClick, green, Color(0xFFDDF7E8)),
        HomeFeature("آداب و فرهنگ", "جشن‌ها، غذاها و رفتار مردم چین", Icons.Default.MenuBook, onCultureClick, purple, Color(0xFFEDE3FF)),
        HomeFeature("شهرهای چین", "۲۰ شهر معروف و راهنمای سفر", Icons.Default.LocationCity, onCitiesClick, gold, Color(0xFFFFEBC5)),
        HomeFeature("مسیرهای شهری", "راهنمای مترو و رفت‌وآمد در چین", Icons.Default.DirectionsSubway, onUrbanRoutesClick, teal, Color(0xFFD9F4F3)),
        HomeFeature("نمایشگاه‌های چین", "تقویم نمایشگاه‌های مهم + راهنمای بازدید", Icons.Default.Event, onExhibitionsClick, red, Color(0xFFFFE5D8)),
        HomeFeature("یادگیری چینی", "واژگان، آموزش و منابع مفید", Icons.Default.Book, onLearningClick, blue, Color(0xFFE0F0FF))
    )

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFFFFBF2))) {
        TravelBackground(modifier = Modifier.fillMaxSize(), alpha = 0.84f)
        Box(modifier = Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.08f)))

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.84f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Yajing", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = blue)
                        Text("Go China Now • چینی‌یار", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF334A52))
                    }
                    Text("🏮", style = MaterialTheme.typography.headlineLarge)
                }
            }

            Text(
                "به چین خوش آمدید",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF123B52),
                textAlign = TextAlign.Right
            )
            Text(
                "با ما، سفر به چین آسان‌تر است",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF47626B),
                textAlign = TextAlign.Right
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentPadding = PaddingValues(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(features) { feature ->
                    Card(
                        onClick = feature.action,
                        modifier = Modifier.fillMaxWidth().border(1.dp, feature.tint.copy(alpha = 0.14f), RoundedCornerShape(22.dp)),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = feature.container.copy(alpha = 0.84f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier.size(42.dp).background(feature.tint.copy(alpha = 0.14f), RoundedCornerShape(14.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(feature.icon, contentDescription = feature.title, tint = feature.tint, modifier = Modifier.size(23.dp))
                            }
                            Text(feature.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFF173B4B))
                            Text(feature.description, style = MaterialTheme.typography.bodySmall, color = Color(0xFF50656B))
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.82f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("🌹", style = MaterialTheme.typography.titleLarge)
                    Column(modifier = Modifier.weight(1f)) {
                        Text("آماده سفر به چین هستی؟", fontWeight = FontWeight.Bold, color = Color(0xFF173B4B))
                        Text("آژانس مسافرتی رز کبود • برنامه‌ریزی و خدمات سفر", style = MaterialTheme.typography.bodySmall, color = Color(0xFF50656B))
                    }
                    IconButton(onClick = {
                        runCatching {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(RoseKaboodUrl)))
                        }
                    }) {
                        Icon(Icons.Default.TravelExplore, contentDescription = "وب‌سایت رز کبود", tint = blue)
                    }
                }
            }
            Spacer(Modifier.height(5.dp))
        }
    }
}
