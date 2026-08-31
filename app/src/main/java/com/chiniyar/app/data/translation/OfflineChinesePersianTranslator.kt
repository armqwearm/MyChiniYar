package com.chiniyar.app.data.translation

import com.google.android.gms.tasks.Task
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

/** Chinese -> Persian translation using ML Kit's on-device model. */
class OfflineChinesePersianTranslator {
    private val translator: Translator = Translation.getClient(
        TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.CHINESE)
            .setTargetLanguage(TranslateLanguage.PERSIAN)
            .build()
    )

    /**
     * Ensures the model exists locally. The network is needed only for the first download;
     * subsequent translations use the on-device model.
     */
    suspend fun prepareModel(): Result<Unit> = runCatching {
        // Do not require Wi-Fi: mobile data is also a valid one-time setup path.
        awaitTask(translator.downloadModelIfNeeded(DownloadConditions.Builder().build()))
    }

    /** Translates using the local model after prepareModel() succeeds. */
    suspend fun translate(text: String): String {
        if (text.isBlank()) return ""
        return awaitTask(translator.translate(text))
    }

    fun close() = translator.close()

    private suspend fun <T> awaitTask(task: Task<T>): T = suspendCancellableCoroutine { continuation ->
        task.addOnSuccessListener { result ->
            if (continuation.isActive) continuation.resume(result)
        }.addOnFailureListener { error ->
            if (continuation.isActive) continuation.resumeWithException(error)
        }
    }
}
