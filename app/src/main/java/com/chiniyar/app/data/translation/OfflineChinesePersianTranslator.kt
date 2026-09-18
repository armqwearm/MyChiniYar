package com.chiniyar.app.data.translation

import com.google.android.gms.tasks.Task
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/** On-device Chinese <-> Persian translation using ML Kit. */
class OfflineChinesePersianTranslator {
    private val chineseToPersian: Translator = Translation.getClient(
        TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.CHINESE)
            .setTargetLanguage(TranslateLanguage.PERSIAN)
            .build()
    )

    private val persianToChinese: Translator = Translation.getClient(
        TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.PERSIAN)
            .setTargetLanguage(TranslateLanguage.CHINESE)
            .build()
    )

    /** Prepare both direction models. The network is required only for first download. */
    suspend fun prepareModel(): Result<Unit> = runCatching {
        val conditions = DownloadConditions.Builder().build()
        awaitTask(chineseToPersian.downloadModelIfNeeded(conditions))
        awaitTask(persianToChinese.downloadModelIfNeeded(conditions))
    }

    suspend fun translateChineseToPersian(text: String): String =
        translateWith(chineseToPersian, text)

    suspend fun translatePersianToChinese(text: String): String =
        translateWith(persianToChinese, text)

    /** Kept for camera pipeline compatibility: Chinese -> Persian. */
    suspend fun translate(text: String): String = translateChineseToPersian(text)

    fun close() {
        chineseToPersian.close()
        persianToChinese.close()
    }

    private suspend fun translateWith(translator: Translator, text: String): String {
        if (text.isBlank()) return ""
        return awaitTask(translator.translate(text))
    }

    private suspend fun <T> awaitTask(task: Task<T>): T = suspendCancellableCoroutine { continuation ->
        task.addOnSuccessListener { result ->
            if (continuation.isActive) continuation.resume(result)
        }.addOnFailureListener { error ->
            if (continuation.isActive) continuation.resumeWithException(error)
        }
    }
}
