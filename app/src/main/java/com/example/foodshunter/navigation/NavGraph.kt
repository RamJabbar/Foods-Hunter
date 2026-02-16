package com.example.foodshunter.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.foodshunter.data.repository.CartRepository
import com.example.foodshunter.ui.screens.auth.AuthViewModel
import com.example.foodshunter.ui.screens.auth.LoginScreen
import com.example.foodshunter.ui.screens.auth.RegisterScreen
import com.example.foodshunter.ui.screens.cart.CartScreen
import com.example.foodshunter.ui.screens.home.HomeScreen
import com.example.foodshunter.ui.screens.restaurant.RestaurantDetailScreen

/**
 * Sealed class untuk define navigation routes
 * Ini membuat navigation type-safe
 */
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object RestaurantDetail : Screen("restaurant_detail/{restaurantId}") {
        fun createRoute(restaurantId: String) = "restaurant_detail/$restaurantId"
    }
    object Cart : Screen("cart")
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel(),
    cartRepository: CartRepository = CartRepository()
) {
    // Determine start destination based on auth state
    val startDestination = if (authViewModel.isLoggedIn) {
        Screen.Home.route
    } else {
        Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Login Screen
        composable(route = Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        // Clear back stack
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                viewModel = authViewModel
            )
        }

        // Register Screen
        composable(route = Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        // Clear back stack after successful registration
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                viewModel = authViewModel
            )
        }
        composable(route = Screen.Home.route) {
            HomeScreen(
                onRestaurantClick = { restaurantId ->
                    navController.navigate(Screen.RestaurantDetail.createRoute(restaurantId))
                },
                onCartClick = {
                    navController.navigate(Screen.Cart.route)
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                cartRepository = cartRepository
            )
        }
        composable(route = Screen.RestaurantDetail.route, listOf(
            navArgument("restaurantId") {type = NavType.StringType}
        )
        ) { backStackEntry ->
            val restaurantId = backStackEntry.arguments?.getString("restaurantId") ?: ""
            RestaurantDetailScreen(
                restaurantId = restaurantId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                cartRepository = cartRepository
            )
        }
        composable(route = Screen.Cart.route) {
            CartScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
            cartRepository = cartRepository
            )
        }
    }
}

