package com.chiniyar.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.chiniyar.app.ui.screens.HomeScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = AppDestination.Home.route) {
        composable(AppDestination.Home.route) {
            HomeScreen(
                onTranslatorClick = { navController.navigate(AppDestination.Translator.route) },
                onCameraClick = { navController.navigate(AppDestination.CameraTranslator.route) },
                onLearningClick = { navController.navigate(AppDestination.Learning.route) },
                onCitiesClick = { navController.navigate(AppDestination.Cities.route) }
            )
        }
        composable(AppDestination.Translator.route) { PlaceholderScreen("مترجم") }
        composable(AppDestination.CameraTranslator.route) { PlaceholderScreen("مترجم تصویری") }
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
