package com.chiniyar.app

import android.app.Application
import com.chiniyar.app.di.AppContainer

class ChiniYarApplication : Application() {
    val appContainer: AppContainer by lazy { AppContainer(this) }
}
