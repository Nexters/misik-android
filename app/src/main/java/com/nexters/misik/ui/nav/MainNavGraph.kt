package com.nexters.misik.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nexters.misik.webview.WebViewScreen

sealed class Route(val route: String) {
    object WebView : Route("webview")
}

@Composable
fun MainNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.WebView.route,
        modifier = modifier,
    ) {
        composable(Route.WebView.route) {
            WebViewScreen()
        }
    }
}
