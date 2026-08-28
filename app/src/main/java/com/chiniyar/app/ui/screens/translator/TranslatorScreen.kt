package com.chiniyar.app.ui.screens.translator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun TranslatorScreen(
    viewModel: TranslatorViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("مترجم", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.weight(1f))
            IconButton(onClick = viewModel::swapLanguages) {
                Icon(Icons.Default.SwapHoriz, contentDescription = "جابجایی زبان‌ها")
            }
        }

        Text("${state.source.code}  →  ${state.target.code}", style = MaterialTheme.typography.labelLarge)

        OutlinedTextField(
            value = state.input,
            onValueChange = viewModel::setInput,
            modifier = Modifier.fillMaxWidth().weight(1f),
            label = { Text("متن برای ترجمه") },
            minLines = 6,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Button(
            onClick = viewModel::translate,
            enabled = state.input.isNotBlank() && !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) CircularProgressIndicator()
            else Text("ترجمه")
        }

        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        if (state.output.isNotBlank()) {
            Text("نتیجه", style = MaterialTheme.typography.titleMedium)
            Text(state.output, style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(Modifier.height(8.dp))
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("بازگشت") }
    }
}
