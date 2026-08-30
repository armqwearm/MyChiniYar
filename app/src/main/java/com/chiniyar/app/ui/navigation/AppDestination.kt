package com.chiniyar.app.ui.navigation

sealed interface AppDestination {
    val route: String

    data object Home : AppDestination { override val route = "home" }
    data object Translator : AppDestination { override val route = "translator" }
    data object Dictionary : AppDestination { override val route = "dictionary" }
    data object CameraTranslator : AppDestination { override val route = "camera_translator" }
    data object VocabularyBank : AppDestination { override val route = "vocabulary_bank" }
    data object Learning : AppDestination { override val route = "learning" }
    data object Cities : AppDestination { override val route = "cities" }
}
