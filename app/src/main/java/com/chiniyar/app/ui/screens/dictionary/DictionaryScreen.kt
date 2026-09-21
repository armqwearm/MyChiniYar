package com.chiniyar.app.ui.screens.dictionary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryScreen(viewModel: DictionaryViewModel, onBack: () -> Unit) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val entries by viewModel.entries.collectAsStateWithLifecycle()
    val colors = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colors.background.copy(alpha = 0f),
        topBar = {
            TopAppBar(
                title = { Text("واژه‌نامه چینی", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "بازگشت")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.background.copy(alpha = 0f))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "یاد بگیر، جست‌وجو کن، برای سفر آماده شو 🇨🇳",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            OutlinedTextField(
                value = query,
                onValueChange = viewModel::setQuery,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                placeholder = { Text("Hanzi، Pinyin یا معنی فارسی") },
                singleLine = true,
                shape = RoundedCornerShape(18.dp)
            )
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(entries, key = { it.hanzi }) { entry ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.surface)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(15.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .size(58.dp)
                                    .background(colors.primary.copy(alpha = 0.10f), RoundedCornerShape(16.dp)),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(entry.hanzi, style = MaterialTheme.typography.titleLarge, color = colors.primary, fontWeight = FontWeight.Bold)
                            }
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp), horizontalAlignment = Alignment.End) {
                                Text(entry.pinyin, modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleMedium, color = colors.secondary, textAlign = TextAlign.Right)
                                Text(entry.meaningFa, modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, textAlign = TextAlign.Right)
                                entry.partOfSpeech?.let { Text(it, modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.labelMedium, color = colors.onSurfaceVariant, textAlign = TextAlign.Right) }
                                entry.example?.let { Text(it, modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Right) }
                            }
                        }
                    }
                }
            }
        }
    }
}
