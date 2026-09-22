package com.chiniyar.app.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.onboardingDataStore by preferencesDataStore(name = "onboarding_preferences")

class OnboardingPreferences(private val context: Context) {
    private val completedKey = booleanPreferencesKey("onboarding_completed")

    suspend fun isCompleted(): Boolean =
        context.onboardingDataStore.data.first()[completedKey] ?: false

    suspend fun markCompleted() {
        context.onboardingDataStore.edit { preferences ->
            preferences[completedKey] = true
        }
    }
}
