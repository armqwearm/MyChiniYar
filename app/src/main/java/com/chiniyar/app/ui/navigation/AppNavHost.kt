package com.chiniyar.app.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.chiniyar.app.di.AppContainer
import com.chiniyar.app.ui.screens.HomeScreen
import com.chiniyar.app.ui.screens.camera.CameraTranslatorScreen
import com.chiniyar.app.ui.screens.camera.CameraTranslatorViewModel
import com.chiniyar.app.ui.screens.dictionary.DictionaryScreen
import com.chiniyar.app.ui.screens.dictionary.DictionaryViewModel
import com.chiniyar.app.ui.screens.dictionary.DictionaryViewModelFactory
import com.chiniyar.app.ui.screens.translator.TranslatorScreen
import com.chiniyar.app.ui.screens.translator.TranslatorViewModel
import com.chiniyar.app.ui.screens.translator.TranslatorViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(navController: NavHostController, appContainer: AppContainer) {
    NavHost(navController = navController, startDestination = AppDestination.Home.route) {
        composable(AppDestination.Home.route) {
            HomeScreen(
                onTranslatorClick = { navController.navigate(AppDestination.Translator.route) },
                onDictionaryClick = { navController.navigate(AppDestination.Dictionary.route) },
                onCameraClick = { navController.navigate(AppDestination.CameraTranslator.route) },
                onLearningClick = { navController.navigate(AppDestination.Learning.route) },
                onCitiesClick = { navController.navigate(AppDestination.Cities.route) }
            )
        }
        composable(AppDestination.Translator.route) {
            val vm: TranslatorViewModel = viewModel(factory = TranslatorViewModelFactory(appContainer))
            TranslatorScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }
        composable(AppDestination.Dictionary.route) {
            val vm: DictionaryViewModel = viewModel(factory = DictionaryViewModelFactory(appContainer.searchDictionaryUseCase))
            DictionaryScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }
        composable(AppDestination.CameraTranslator.route) {
            val vm: CameraTranslatorViewModel = viewModel()
            CameraTranslatorScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }
        composable(AppDestination.Learning.route) { PlaceholderScreen("یادگیری زبان چینی") }
        composable(AppDestination.Cities.route) { PlaceholderScreen("شهرهای چین") }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    Scaffold { padding ->
        Text(
            text = title,
            modifier = Modifier.padding(padding).padding(24.dp),
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
