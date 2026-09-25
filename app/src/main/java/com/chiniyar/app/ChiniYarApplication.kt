package com.chiniyar.app

import android.app.Application
import com.chiniyar.app.di.AppContainer

class ChiniYarApplication : Application() {
    val appContainer: AppContainer by lazy { AppContainer(this) }

    override fun onCreate() {
        super.onCreate()
        // Keep Application startup lightweight. The offline dictionary is loaded
        // lazily when the camera translation feature is first used.
    }
}
