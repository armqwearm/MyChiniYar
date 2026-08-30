package com.chiniyar.app.ui.screens.vocabulary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.chiniyar.app.data.local.VocabularyDatabase
import com.chiniyar.app.data.local.VocabularyEntry
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyBankScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val db = remember { VocabularyDatabase.getInstance(context) }
    val scope = rememberCoroutineScope()
    val allEntries by db.observeAll().collectAsState(initial = emptyList())
    var query by remember { mutableStateOf("") }

    val filtered = remember(allEntries, query) {
        val q = query.trim()
        if (q.isEmpty()) allEntries else allEntries.filter {
            it.word.contains(q, ignoreCase = true) ||
                it.pinyin.contains(q, ignoreCase = true) ||
                it.meaning.contains(q, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("بانک لغات من") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "بازگشت") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, null) },
                label = { Text("جست‌وجوی لغت") },
                placeholder = { Text("Hanzi، Pinyin یا معنی") }
            )
            Text("${filtered.size} لغت", style = MaterialTheme.typography.titleMedium)
            if (filtered.isEmpty()) {
                Text(
                    if (allEntries.isEmpty()) "هنوز لغتی ذخیره نشده است. از مترجم تصویری با ⭐ لغت اضافه کنید."
                    else "نتیجه‌ای برای جست‌وجوی شما پیدا نشد.",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(filtered, key = { it.word }) { entry ->
                        VocabularyCard(entry) {
                            scope.launch { db.remove(entry.word) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VocabularyCard(entry: VocabularyEntry, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(entry.word, style = MaterialTheme.typography.headlineSmall)
                Text(entry.pinyin, style = MaterialTheme.typography.bodyMedium)
                Text(entry.meaning, style = MaterialTheme.typography.bodyLarge)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "حذف")
            }
        }
    }
}
