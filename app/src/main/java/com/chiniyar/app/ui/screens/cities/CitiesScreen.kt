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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chiniyar.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesScreen(onBack: () -> Unit) {
    val cities = ChinaCitiesData.cities
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("شهرهای چین") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "بازگشت")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Image(
                painter = painterResource(R.drawable.china_map_outline),
                contentDescription = "نقشه چین",
                modifier = Modifier.fillMaxSize().alpha(0.08f),
                contentScale = ContentScale.Fit
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "راهنمای آفلاین ۲۰ شهر معروف چین",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "اطلاعات این بخش داخل برنامه ذخیره شده و برای مشاهده آن به اینترنت نیاز ندارید.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(4.dp))
                }
                items(cities) { city -> CityCard(city) }
                item { Spacer(Modifier.height(12.dp)) }
            }
        }
    }
}

@Composable
private fun CityCard(city: ChinaCity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(24.dp))
                Spacer(Modifier.padding(horizontal = 4.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(city.nameFa, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("${city.nameZh} · ${city.pinyin}", style = MaterialTheme.typography.bodyMedium)
                }
            }
            InfoLine("استان/منطقه", city.province)
            InfoLine("جمعیت تقریبی", city.population)
            InfoLine("جاهای دیدنی", city.highlights)
            InfoLine("نکات سفر", city.travelTips)
            InfoLine("زمان پیشنهادی سفر", city.bestTime)
        }
    }
}

@Composable
private fun InfoLine(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}
