package com.chiniyar.app.ui.screens.translator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TranslatorScreen(
    viewModel: TranslatorViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "بازگشت")
            }
            Text(
                "مترجم چینی",
                modifier = Modifier.padding(top = 10.dp),
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Text("ترجمه سریع چینی و فارسی")

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                "${state.source.code}  ↔  ${state.target.code}",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(onClick = viewModel::swapLanguages) {
                Icon(Icons.Default.SwapHoriz, contentDescription = "جابجایی زبان‌ها")
            }
        }

        OutlinedTextField(
            value = state.input,
            onValueChange = viewModel::setInput,
            modifier = Modifier.fillMaxWidth(),
            minLines = 4,
            label = { Text("متن ورودی") },
            placeholder = { Text("مثلاً: 你好") }
        )

        Button(
            onClick = viewModel::translate,
            enabled = !state.isLoading && state.input.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) CircularProgressIndicator(strokeWidth = 2.dp)
            else Text("ترجمه")
        }

        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        Spacer(Modifier.height(4.dp))
        Text("نتیجه", style = MaterialTheme.typography.titleMedium)
        Text(
            state.output.ifBlank { "ترجمه اینجا نمایش داده می‌شود" },
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
