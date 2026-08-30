package com.chiniyar.app.data.translation

import com.google.android.gms.tasks.Task
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Chinese -> Persian translation using the ML Kit on-device model.
 * The model is downloaded once (Wi-Fi by default) and then translation is local.
 */
class OfflineChinesePersianTranslator {
    private val translator: Translator = Translation.getClient(
        TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.CHINESE)
            .setTargetLanguage(TranslateLanguage.PERSIAN)
            .build()
    )

    suspend fun ensureModelDownloaded() {
        val conditions = DownloadConditions.Builder().requireWifi().build()
        awaitTask(translator.downloadModelIfNeeded(conditions))
    }

    suspend fun translate(text: String): String {
        if (text.isBlank()) return ""
        ensureModelDownloaded()
        return awaitTask(translator.translate(text))
    }

    suspend fun isModelDownloaded(): Boolean {
        val models = awaitTask(
            RemoteModelManager.getInstance()
                .getDownloadedModels(TranslateRemoteModel::class.java)
        )
        return models.any { it.language == TranslateLanguage.PERSIAN }
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
