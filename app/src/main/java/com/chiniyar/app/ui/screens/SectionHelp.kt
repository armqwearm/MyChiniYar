package com.chiniyar.app.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight

@Composable
fun SectionHelpButton(
    title: String,
    guide: String
) {
    var showHelp by remember { mutableStateOf(false) }

    IconButton(onClick = { showHelp = true }) {
        Icon(
            Icons.Default.HelpOutline,
            contentDescription = "راهنمای $title",
            tint = MaterialTheme.colorScheme.primary
        )
    }

    if (showHelp) {
        AlertDialog(
            onDismissRequest = { showHelp = false },
            title = {
                Text("راهنمای $title", fontWeight = FontWeight.Bold)
            },
            text = {
                Text(guide, style = MaterialTheme.typography.bodyMedium)
            },
            confirmButton = {
                TextButton(onClick = { showHelp = false }) {
                    Text("متوجه شدم")
                }
            }
        )
    }
}
