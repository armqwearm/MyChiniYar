package com.chiniyar.app.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.userPreferencesDataStore by preferencesDataStore(name = "user_preferences")

data class UserPreferences(
    val darkTheme: Boolean = false,
    val hapticFeedback: Boolean = true,
    val sourceLanguageCode: String = "zh",
    val targetLanguageCode: String = "fa"
)

class UserPreferencesRepository(private val context: Context) {
    private object Keys {
        val DARK_THEME = booleanPreferencesKey("dark_theme")
        val HAPTIC = booleanPreferencesKey("haptic_feedback")
    }

    val preferences: Flow<UserPreferences> = context.userPreferencesDataStore.data.map { values ->
        UserPreferences(
            darkTheme = values[Keys.DARK_THEME] ?: false,
            hapticFeedback = values[Keys.HAPTIC] ?: true
        )
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.userPreferencesDataStore.edit { it[Keys.DARK_THEME] = enabled }
    }

    suspend fun setHapticFeedback(enabled: Boolean) {
        context.userPreferencesDataStore.edit { it[Keys.HAPTIC] = enabled }
    }
}
