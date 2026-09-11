package com.chiniyar.app.ui.screens.vocabulary

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.chiniyar.app.data.local.VocabularyDatabase
import com.chiniyar.app.data.local.VocabularyEntry
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyBankScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val db = remember { VocabularyDatabase.getInstance(context) }
    val scope = rememberCoroutineScope()
    val allEntries by db.words.collectAsState(initial = emptyList<VocabularyEntry>())
    var query by remember { mutableStateOf("") }
    val colors = MaterialTheme.colorScheme

    val filtered = remember(allEntries, query) {
        val q = query.trim()
        if (q.isEmpty()) allEntries else allEntries.filter { entry ->
            entry.word.contains(q, ignoreCase = true) ||
                entry.pinyin.contains(q, ignoreCase = true) ||
                entry.meaning.contains(q, ignoreCase = true)
        }
    }

    Scaffold(
        containerColor = colors.background,
        topBar = {
            TopAppBar(
                title = { Text("بانک لغات من", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "بازگشت")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = colors.tertiaryContainer)
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("⭐", style = MaterialTheme.typography.headlineSmall)
                    Column(modifier = Modifier.weight(1f).padding(start = 10.dp), horizontalAlignment = Alignment.End) {
                        Text("واژه‌های منتخب من", fontWeight = FontWeight.Bold)
                        Text("کلمات مهمت را برای مرور بعدی نگه دار", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                label = { Text("جست‌وجوی لغت") },
                placeholder = { Text("Hanzi، Pinyin یا معنی") },
                shape = RoundedCornerShape(18.dp)
            )
            Text("${filtered.size} لغت", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            if (filtered.isEmpty()) {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)) {
                    Text(
                        if (allEntries.isEmpty()) "هنوز لغتی ذخیره نشده است. از مترجم تصویری با ⭐ لغت اضافه کنید."
                        else "نتیجه‌ای برای جست‌وجوی شما پیدا نشد.",
                        modifier = Modifier.padding(18.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Right
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(filtered, key = { it.word }) { entry ->
                        VocabularyCard(entry) { scope.launch { db.remove(entry.word) } }
                    }
                }
            }
        }
    }
}

@Composable
private fun VocabularyCard(entry: VocabularyEntry, onDelete: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .size(58.dp)
                    .background(colors.tertiary.copy(alpha = 0.14f), RoundedCornerShape(16.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(entry.word, style = MaterialTheme.typography.titleLarge, color = colors.tertiary, fontWeight = FontWeight.Bold)
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp), horizontalAlignment = Alignment.End) {
                Text(entry.pinyin, style = MaterialTheme.typography.titleMedium, color = colors.secondary)
                Text(entry.meaning, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Right)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "حذف", tint = colors.error)
            }
        }
    }
}
