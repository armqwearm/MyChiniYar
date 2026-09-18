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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.chiniyar.app.core.common.OnlineChinesePronunciationButton
import com.chiniyar.app.data.analysis.ChineseWordAnalyzer
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
    var showAddDialog by remember { mutableStateOf(false) }
    var newWord by remember { mutableStateOf("") }
    var newPinyin by remember { mutableStateOf("") }
    var newMeaning by remember { mutableStateOf("") }
    var pinyinEdited by remember { mutableStateOf(false) }
    var addError by remember { mutableStateOf<String?>(null) }
    val colors = MaterialTheme.colorScheme
    val analyzer = remember { ChineseWordAnalyzer() }

    val filtered = remember(allEntries, query) {
        val q = query.trim()
        if (q.isEmpty()) allEntries else allEntries.filter { entry ->
            entry.word.contains(q, ignoreCase = true) ||
                entry.pinyin.contains(q, ignoreCase = true) ||
                entry.meaning.contains(q, ignoreCase = true)
        }
    }

    fun openAddDialog() {
        newWord = ""
        newPinyin = ""
        newMeaning = ""
        pinyinEdited = false
        addError = null
        showAddDialog = true
    }

    fun saveManualWord() {
        val word = newWord.trim()
        val pinyin = newPinyin.trim()
        val meaning = newMeaning.trim()
        addError = when {
            word.isBlank() -> "لطفاً واژه چینی را وارد کنید."
            meaning.isBlank() -> "لطفاً معنی واژه را وارد کنید."
            else -> null
        }
        if (addError != null) return
        scope.launch {
            val inserted = db.add(VocabularyEntry(word, pinyin, meaning))
            if (inserted) showAddDialog = false
            else addError = "این واژه قبلاً در بانک لغات شما ثبت شده است."
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
        },
        floatingActionButton = {
            Button(onClick = ::openAddDialog, shape = RoundedCornerShape(18.dp)) {
                Icon(Icons.Default.Add, contentDescription = null)
                androidx.compose.foundation.layout.Spacer(Modifier.size(6.dp))
                Text("افزودن لغت")
            }
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
                    Icon(Icons.Default.Add, contentDescription = null, tint = colors.tertiary, modifier = Modifier.size(30.dp))
                    Column(modifier = Modifier.weight(1f).padding(start = 10.dp), horizontalAlignment = Alignment.End) {
                        Text("واژه‌های منتخب من", fontWeight = FontWeight.Bold)
                        Text("واژه‌های مهمت را ذخیره و هر زمان مرور کن", style = MaterialTheme.typography.bodySmall)
                        Text("می‌توانی واژه را مستقیم و دستی هم اضافه کنی.", style = MaterialTheme.typography.labelMedium, color = colors.tertiary)
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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.surface.copy(alpha = 0.72f))
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = colors.primary, modifier = Modifier.size(30.dp))
                        Text(
                            if (allEntries.isEmpty()) "هنوز لغتی ذخیره نشده است."
                            else "نتیجه‌ای برای جست‌وجوی شما پیدا نشد.",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Right
                        )
                        if (allEntries.isEmpty()) {
                            Text(
                                "واژه‌های جدید را مستقیم به بانک شخصی‌ات اضافه کن.",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Right
                            )
                            OutlinedButton(onClick = ::openAddDialog, modifier = Modifier.fillMaxWidth()) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                androidx.compose.foundation.layout.Spacer(Modifier.size(6.dp))
                                Text("افزودن اولین لغت")
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filtered, key = { it.word }) { entry ->
                        VocabularyCard(entry) { scope.launch { db.remove(entry.word) } }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("افزودن واژه جدید", fontWeight = FontWeight.ExtraBold)
                    Text("یک واژه را برای بانک شخصی خودت ثبت کن.", style = MaterialTheme.typography.bodySmall, color = colors.onSurfaceVariant)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newWord,
                        onValueChange = {
                            newWord = it
                            if (!pinyinEdited) newPinyin = analyzer.pinyin(it.trim())
                            addError = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("واژه چینی") },
                        placeholder = { Text("مثلاً 你好") },
                        shape = RoundedCornerShape(16.dp)
                    )
                    OutlinedTextField(
                        value = newPinyin,
                        onValueChange = {
                            newPinyin = it
                            pinyinEdited = true
                            addError = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Pinyin") },
                        placeholder = { Text("مثلاً ni3 hao3") },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    newPinyin = analyzer.pinyin(newWord.trim())
                                    pinyinEdited = false
                                },
                                enabled = newWord.isNotBlank()
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = "تولید پین‌یین")
                            }
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
                    OutlinedTextField(
                        value = newMeaning,
                        onValueChange = {
                            newMeaning = it
                            addError = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 3,
                        label = { Text("معنی فارسی") },
                        placeholder = { Text("مثلاً سلام") },
                        shape = RoundedCornerShape(16.dp)
                    )
                    Text(
                        "پین‌یین اختیاری است؛ معنی فارسی برای ذخیره واژه لازم است.",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Right
                    )
                    addError?.let {
                        Text(
                            it,
                            color = colors.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Right
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = ::saveManualWord) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    androidx.compose.foundation.layout.Spacer(Modifier.size(5.dp))
                    Text("ذخیره در بانک")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("انصراف") }
            }
        )
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
            OnlineChinesePronunciationButton(entry.word)
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "حذف", tint = colors.error)
            }
        }
    }
}
