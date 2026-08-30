package com.chiniyar.app.ui.screens.camera

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.chiniyar.app.data.translation.OfflineChinesePersianTranslator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraTranslatorScreen(
    viewModel: CameraTranslatorViewModel,
    onBack: () -> Unit,
    ocrProcessor: ChineseOcrProcessor = remember { ChineseOcrProcessor() },
    translator: OfflineChinesePersianTranslator = remember { OfflineChinesePersianTranslator() }
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    DisposableEffect(translator) {
        onDispose { translator.close() }
    }

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        viewModel.setImage(uri)
        viewModel.setProcessing(true, "در حال استخراج متن چینی آفلاین...")
        scope.launch {
            try {
                val text = ocrProcessor.recognize(context, uri)
                if (text.isBlank()) {
                    viewModel.setError("متن قابل تشخیصی در تصویر پیدا نشد.")
                    return@launch
                }

                viewModel.setExtractedText(text)
                viewModel.setProcessing(true, "در حال آماده‌سازی مدل ترجمه؛ فقط بار اول به اینترنت Wi‑Fi نیاز دارد...")

                val translated = translator.translate(text)
                if (translated.isBlank()) {
                    viewModel.setError("ترجمه‌ای برای متن استخراج‌شده دریافت نشد.")
                } else {
                    viewModel.setTranslatedText(translated)
                }
            } catch (e: Exception) {
                viewModel.setError(
                    e.message ?: "ترجمه انجام نشد. اگر مدل ترجمه هنوز دانلود نشده، یک بار با Wi‑Fi امتحان کنید."
                )
            }
        }
    }

    fun copyText(text: String, label: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
        scope.launch { snackbar.showSnackbar("$label کپی شد") }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("مترجم تصویری") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "بازگشت") }
                },
                actions = {
                    if (state.imageUri != null) {
                        IconButton(onClick = { viewModel.setImage(null) }) {
                            Icon(Icons.Default.Clear, "پاک کردن")
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("تصویر دارای متن چینی را انتخاب کنید.", style = MaterialTheme.typography.bodyLarge)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = { picker.launch("image/*") }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Image, null)
                    Spacer(Modifier.padding(horizontal = 3.dp))
                    Text("گالری")
                }
                OutlinedButton(onClick = { picker.launch("image/*") }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.CameraAlt, null)
                    Spacer(Modifier.padding(horizontal = 3.dp))
                    Text("دوربین")
                }
            }
            if (state.isProcessing) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    CircularProgressIndicator()
                    Text(state.statusMessage.ifBlank { "در حال پردازش..." }, modifier = Modifier.padding(top = 8.dp))
                }
            }
            state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            ResultCard("متن OCR شده", state.extractedText, "متن تشخیص‌داده‌شده اینجا نمایش داده می‌شود.", "کپی متن OCR") {
                copyText(state.extractedText, "متن OCR")
            }
            ResultCard("ترجمه فارسی", state.translatedText, "ترجمه فارسی اینجا نمایش داده می‌شود.", "کپی ترجمه") {
                copyText(state.translatedText, "ترجمه")
            }
        }
    }
}

@Composable
private fun ResultCard(title: String, text: String, emptyText: String, copyLabel: String, onCopy: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(if (text.isBlank()) emptyText else text)
                if (text.isNotBlank()) {
                    OutlinedButton(onClick = onCopy, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.ContentCopy, null)
                        Spacer(Modifier.padding(horizontal = 4.dp))
                        Text(copyLabel)
                    }
                }
            }
        }
    }
}
