package com.chiniyar.app.core.common

import android.media.MediaPlayer
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/** Streams Mandarin pronunciation from the online TTS endpoint without bundling audio assets. */
@Composable
fun OnlineChinesePronunciationButton(text: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var player by remember { mutableStateOf<MediaPlayer?>(null) }
    var loading by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            player?.release()
            player = null
        }
    }

    IconButton(
        onClick = {
            if (text.isBlank() || loading) return@IconButton
            player?.release()
            player = null
            loading = true

            val uri = Uri.parse("https://translate.google.com/translate_tts")
                .buildUpon()
                .appendQueryParameter("ie", "UTF-8")
                .appendQueryParameter("client", "tw-ob")
                .appendQueryParameter("tl", "zh-CN")
                .appendQueryParameter("q", text)
                .build()

            runCatching {
                MediaPlayer().apply {
                    setDataSource(
                        context,
                        uri,
                        mapOf("User-Agent" to "Mozilla/5.0 (Android) MyChiniYar")
                    )
                    setOnPreparedListener { mediaPlayer ->
                        loading = false
                        player = mediaPlayer
                        mediaPlayer.start()
                    }
                    setOnCompletionListener { mediaPlayer ->
                        loading = false
                        mediaPlayer.release()
                        if (player === mediaPlayer) player = null
                    }
                    setOnErrorListener { mediaPlayer, _, _ ->
                        loading = false
                        mediaPlayer.release()
                        if (player === mediaPlayer) player = null
                        Toast.makeText(context, "تلفظ آنلاین در دسترس نیست", Toast.LENGTH_SHORT).show()
                        true
                    }
                    prepareAsync()
                    player = this
                }
            }.onFailure {
                loading = false
                Toast.makeText(context, "تلفظ آنلاین در دسترس نیست", Toast.LENGTH_SHORT).show()
            }
        },
        enabled = text.isNotBlank() && !loading,
        modifier = modifier.size(44.dp)
    ) {
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
        } else {
            Icon(Icons.Default.VolumeUp, contentDescription = "تلفظ آنلاین")
        }
    }
}
