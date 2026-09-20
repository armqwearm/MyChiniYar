package com.chiniyar.app.ui.screens.camera

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.activity.result.ActivityResultLauncher
import com.chiniyar.app.core.common.OnlineChinesePronunciationButton
import com.chiniyar.app.data.analysis.AnalyzedWord
import com.chiniyar.app.data.local.VocabularyDatabase
import com.chiniyar.app.data.local.VocabularyEntry
import com.chiniyar.app.domain.translation.CameraTranslationUseCase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraTranslatorScreen(
    viewModel: CameraTranslatorViewModel,
    onBack: () -> Unit,
    processor: CameraTranslationUseCase,
    vocabularyDb: VocabularyDatabase
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }
    var cameraUri by remember { mutableStateOf<Uri?>(null) }

    fun processUri(uri: Uri) {
        viewModel.setImage(uri)
        viewModel.setProcessing(true, "در حال استخراج متن چینی آفلاین...")
        scope.launch {
            val result = runCatching {
                processor.execute(
                    context = context,
                    imageUri = uri,
                    onStatus = { status -> viewModel.setProcessing(true, status) },
                    onOcrResult = { text ->
                        viewModel.setExtractedText(text)
                        viewModel.setProcessing(true, "متن OCR آماده شد؛ در حال پردازش ادامه کار...")
                    },
                    onWordsResult = { words -> viewModel.setWords(words) }
                )
            }.getOrElse { error ->
                kotlin.Result.failure<CameraTranslationUseCase.ResultData>(error)
            }
            result.onSuccess { data ->
                viewModel.setExtractedText(data.extractedText)
                viewModel.setTranslatedText(data.translatedText)
                viewModel.setWords(data.words)
                viewModel.setProcessing(false)
                viewModel.setError(data.translationError)
            }.onFailure { error ->
                viewModel.setProcessing(false)
                viewModel.setError(error.message ?: "پردازش تصویر انجام نشد.")
            }
        }
    }

    val galleryPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) processUri(uri)
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        val uri = cameraUri
        cameraUri = null
        if (success && uri != null) processUri(uri)
        else if (uri != null) context.contentResolver.delete(uri, null, null)
    }

    fun createCameraUri(): Uri? {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "chiniyar_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyChiniYar")
        }
        return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            val uri = createCameraUri()
            if (uri == null) scope.launch { snackbar.showSnackbar("امکان آماده‌سازی دوربین وجود ندارد") }
            else {
                cameraUri = uri
                cameraLauncher.launch(uri)
            }
        } else {
            scope.launch { snackbar.showSnackbar("برای استفاده از دوربین، دسترسی دوربین را فعال کنید") }
        }
    }

    fun openCamera() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            return
        }
        val uri = createCameraUri()
        if (uri == null) scope.launch { snackbar.showSnackbar("امکان آماده‌سازی دوربین وجود ندارد") }
        else {
            cameraUri = uri
            cameraLauncher.launch(uri)
        }
    }

    fun copyText(text: String, label: String) {
        if (text.isBlank()) return
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
        scope.launch { snackbar.showSnackbar("$label کپی شد") }
    }

    fun saveWord(word: AnalyzedWord) {
        if (word.saved) return
        scope.launch {
            val inserted = vocabularyDb.add(VocabularyEntry(word.word, word.pinyin, word.meaning))
            viewModel.setWordSaved(word.word, true)
            snackbar.showSnackbar(if (inserted) "${word.word} به بانک لغات اضافه شد" else "${word.word} قبلاً در بانک لغات بود")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("مترجم تصویری", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "بازگشت") } },
                actions = { if (state.imageUri != null) IconButton(onClick = { viewModel.clearResults() }) { Icon(Icons.Default.Clear, contentDescription = "پاک کردن") } }
            )
        },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("تصویر دارای متن چینی را انتخاب کنید.", modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodyLarge, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold, textAlign = TextAlign.Center)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = { galleryPicker.launch("image/*") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp)) {
                    Icon(Icons.Default.Image, null); Spacer(Modifier.padding(horizontal = 3.dp)); Text("گالری")
                }
                OutlinedButton(onClick = { openCamera() }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp)) {
                    Icon(Icons.Default.CameraAlt, null); Spacer(Modifier.padding(horizontal = 3.dp)); Text("دوربین")
                }
            }
            if (state.isProcessing) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 2.5.dp)
                    Spacer(Modifier.size(10.dp))
                    Text(state.statusMessage.ifBlank { "در حال پردازش..." }, style = MaterialTheme.typography.bodyMedium, fontWeight = androidx.compose.ui.text.font.FontWeight.Medium, textAlign = TextAlign.Center)
                }
            }
            state.error?.let {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Text(it, modifier = Modifier.fillMaxWidth().padding(14.dp), color = MaterialTheme.colorScheme.onErrorContainer, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
                }
            }
            ResultCard("متن OCR شده", state.extractedText, "متن تشخیص‌داده‌شده اینجا نمایش داده می‌شود.", "کپی متن OCR") { copyText(state.extractedText, "متن OCR") }
            ResultCard("ترجمه فارسی", state.translatedText, "ترجمه فارسی اینجا نمایش داده می‌شود.", "کپی ترجمه") { copyText(state.translatedText, "ترجمه") }
            if (state.words.isNotEmpty()) {
                Text("واژه‌های متن — ${state.words.size} مورد", modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleLarge, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, textAlign = TextAlign.Center)
                Text("۴۰ واژه غیرتکراری اول؛ معنی واژه‌های موجود در فرهنگ داخلی بدون اینترنت انجام می‌شود.", modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
                state.words.forEach { word -> WordCard(word) { saveWord(word) } }
            }
        }
    }
}

private fun launchCamera(
    context: Context,
    launcher: ActivityResultLauncher<Uri>,
    onFailure: () -> Unit
) {
    val values = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "chiniyar_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyChiniYar")
    }
    val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    if (uri == null) onFailure() else launcher.launch(uri)
}

@Composable
private fun ResultCard(title: String, text: String, emptyText: String, copyLabel: String, onCopy: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleMedium, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, textAlign = TextAlign.Center)
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(if (text.isBlank()) emptyText else text, modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodyLarge, textAlign = if (text.isBlank()) TextAlign.Center else TextAlign.Start)
                if (text.isNotBlank()) {
                    OutlinedButton(onClick = onCopy, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
                        Icon(Icons.Default.ContentCopy, null); Spacer(Modifier.size(6.dp)); Text(copyLabel)
                    }
                }
            }
        }
    }
}

@Composable
private fun WordCard(word: AnalyzedWord, onSave: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(word.word, style = MaterialTheme.typography.titleLarge)
                Text(word.pinyin, style = MaterialTheme.typography.bodyMedium)
                Text(word.meaning, style = MaterialTheme.typography.bodyLarge)
            }
            OnlineChinesePronunciationButton(word.word)
            IconButton(onClick = { if (!word.saved) onSave() }) {
                Icon(
                    if (word.saved) Icons.Default.Check else Icons.Default.Add,
                    if (word.saved) "ذخیره شده" else "افزودن به بانک لغات"
                )
            }
        }
    }
}
