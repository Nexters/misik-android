package com.nexters.misik.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nexters.misik.webview.WebViewContainer

@Composable
fun MainNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "webview") {
        composable("webview") {
            WebViewContainer()
        }
    }
}