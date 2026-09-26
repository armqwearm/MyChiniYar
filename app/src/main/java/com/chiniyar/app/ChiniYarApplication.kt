package com.chiniyar.app

import android.app.Application
import com.chiniyar.app.di.AppContainer
import java.io.PrintWriter
import java.io.StringWriter

class ChiniYarApplication : Application() {
    val appContainer: AppContainer by lazy { AppContainer(this) }

    override fun onCreate() {
        super.onCreate()

        // Keep Application startup lightweight. The offline dictionary and other
        // heavy feature components are initialized lazily.
        val previousHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                val stackTrace = StringWriter().also { writer ->
                    throwable.printStackTrace(PrintWriter(writer))
                }.toString()

                getSharedPreferences(CRASH_PREFS, MODE_PRIVATE)
                    .edit()
                    .putString(CRASH_TRACE_KEY, stackTrace.take(MAX_CRASH_TRACE_LENGTH))
                    .commit()
            } catch (_: Throwable) {
                // Never let crash reporting interfere with the system crash handler.
            }

            previousHandler?.uncaughtException(thread, throwable)
        }
    }

    companion object {
        const val CRASH_PREFS = "chiniyar_startup_diagnostics"
        const val CRASH_TRACE_KEY = "last_crash_trace"
        private const val MAX_CRASH_TRACE_LENGTH = 24000
    }
}
