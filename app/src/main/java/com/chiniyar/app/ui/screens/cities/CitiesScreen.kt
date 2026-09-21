package com.chiniyar.app.ui.screens.cities

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chiniyar.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesScreen(onBack: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    val cities = ChinaCitiesData.cities
    Scaffold(
        containerColor = colors.background.copy(alpha = 0f),
        topBar = {
            TopAppBar(
                title = { Text("شهرهای چین", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "بازگشت")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.background.copy(alpha = 0f),
                    titleContentColor = colors.onBackground
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Image(
                painter = painterResource(R.drawable.china_map_outline),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.055f),
                contentScale = ContentScale.Fit
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(Modifier.height(4.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.primaryContainer)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("🏯", style = MaterialTheme.typography.headlineMedium)
                            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "راهنمای سفر به ۲۰ شهر چین",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = colors.primary,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    "اطلاعات شهرها داخل برنامه ذخیره شده و آفلاین قابل مشاهده است.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.onPrimaryContainer,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(2.dp))
                }
                items(cities) { city -> CityCard(city) }
                item { Spacer(Modifier.height(12.dp)) }
            }
        }
    }
}

@Composable
private fun CityCard(city: ChinaCity) {
    val colors = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                BoxedCityIcon()
                Spacer(Modifier.size(10.dp))
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(city.nameFa, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
                    Text("${city.nameZh}  •  ${city.pinyin}", style = MaterialTheme.typography.bodyMedium, color = colors.secondary, textAlign = TextAlign.Center)
                }
            }

            CityInfoRow(Icons.Default.LocationOn, "استان / منطقه", city.province, colors.secondary)
            CityInfoRow(Icons.Default.People, "جمعیت تقریبی", city.population, colors.tertiary)
            CityInfoRow(Icons.Default.Place, "جاهای دیدنی", city.highlights, colors.primary)
            CityInfoRow(Icons.Default.LocationOn, "نکات سفر", city.travelTips, colors.secondary)
            CityInfoRow(Icons.Default.CalendarMonth, "زمان پیشنهادی", city.bestTime, colors.tertiary)
        }
    }
}

@Composable
private fun BoxedCityIcon() {
    val colors = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .size(46.dp)
            .background(colors.primary.copy(alpha = 0.10f), RoundedCornerShape(15.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.LocationOn, contentDescription = null, tint = colors.primary, modifier = Modifier.size(25.dp))
    }
}

@Composable
private fun CityInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    accent: Color
) {
    val colors = MaterialTheme.colorScheme
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.padding(top = 2.dp).size(20.dp))
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = colors.onSurfaceVariant, fontWeight = FontWeight.Bold)
            Text(value, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Right)
        }
    }
}
