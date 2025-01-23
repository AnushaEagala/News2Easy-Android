package com.example.newsapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.finalproject.NewsViewModel
import com.example.newsapp.LoginScreen
import com.example.newsapp.SignUpScreen
import com.example.newsapp.WelcomePage
import com.example.newsapp.ProfileScreen
import com.example.newsapp.ui.theme.ConnectionsScreen
import com.example.newsapp.ui.theme.ScreenTimeScreen
import com.example.newsapp.AuthViewModel
import com.example.newsapp.UnifiedPage
import com.example.newsapp.NewsArticlePage


@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    newsViewModel: NewsViewModel // Added NewsViewModel parameter
) {
    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomePage(
                onNavigateToLogin = { navController.navigate("login") }
            )
        }
        composable("login") {
            LoginScreen(
                onNavigateToHome = { navController.navigate("unified") },
                onNavigateToSignUp = { navController.navigate("signup") },
                authViewModel = authViewModel
            )
        }
        composable("signup") {
            SignUpScreen(
                onNavigateToLogin = { navController.navigate("login") },
                authViewModel = authViewModel
            )
        }
        composable("unified") {
            UnifiedPage(
                newsViewModel = newsViewModel,
                navController = navController,
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToConnectionsGraph = { navController.navigate("connectionsGraph") },
                onNavigateToScreenTimeGraph = { navController.navigate("screenTimeGraph") }
            )
        }
        composable("profile") {
            ProfileScreen()
        }
        composable("connectionsGraph") {
            ConnectionsScreen() // Navigates to ConnectionsScreen
        }
        composable("screenTimeGraph") {
            ScreenTimeScreen() // Navigates to ScreenTimeScreen
        }

        composable(
            route = "article/{title}/{content}/{urlToImage}/{url}/{author}/{publishedAt}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("content") { type = NavType.StringType },
                navArgument("urlToImage") { type = NavType.StringType; nullable = true },
                navArgument("url") { type = NavType.StringType },
                navArgument("author") { type = NavType.StringType },
                navArgument("publishedAt") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // Safely retrieve arguments
            val title = backStackEntry.arguments?.getString("title") ?: "Unknown Title"
            val content = backStackEntry.arguments?.getString("content") ?: "No Content"
            val urlToImage = backStackEntry.arguments?.getString("urlToImage")
            val url = backStackEntry.arguments?.getString("url") ?: ""
            val author = backStackEntry.arguments?.getString("author") ?: "Unknown Author"
            val publishedAt = backStackEntry.arguments?.getString("publishedAt") ?: ""

            NewsArticlePage(
                title = title,
                content = content,
                urlToImage = urlToImage,
                url = url,
                author = author,
                publishedAt = publishedAt,
                context = LocalContext.current
            )
        }
    }
}