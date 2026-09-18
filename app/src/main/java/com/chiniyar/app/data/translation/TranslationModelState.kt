package com.chiniyar.app.data.translation

/** Explicit state for the on-device translation model. */
sealed interface TranslationModelState {
    data object NotReady : TranslationModelState
    data object Preparing : TranslationModelState
    data object Ready : TranslationModelState
    data class Failed(val message: String) : TranslationModelState
}
