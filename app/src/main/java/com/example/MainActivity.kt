package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.core.di.ServiceLocator
import com.example.core.navigation.BottomNavTab
import com.example.core.navigation.Screen
import com.example.feature.address.AddressesScreen
import com.example.feature.auth.ForgotPasswordScreen
import com.example.feature.auth.LoginScreen
import com.example.feature.auth.OtpVerificationScreen
import com.example.feature.auth.RegisterScreen
import com.example.feature.cart.CartScreen
import com.example.feature.category.CategoryProductsScreen
import com.example.feature.checkout.CheckoutScreen
import com.example.feature.checkout.OrderConfirmationScreen
import com.example.feature.main.MainScreen
import com.example.feature.notification.NotificationsScreen
import com.example.feature.onboarding.OnboardingScreen
import com.example.feature.orders.OrderDetailScreen
import com.example.feature.orders.OrderTrackingScreen
import com.example.feature.orders.OrdersScreen
import com.example.feature.product.ProductDetailScreen
import com.example.feature.splash.SplashScreen
import com.example.ui.theme.BazaarTheme
import com.example.ui.viewmodel.BazaarViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ServiceLocator.init()
        enableEdgeToEdge()

        setContent {
            BazaarTheme {
                val navController = rememberNavController()
                val bazaarViewModel: BazaarViewModel = viewModel()
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(Unit) {
                    bazaarViewModel.snackbarEvents.collectLatest { message ->
                        snackbarHostState.showSnackbar(message)
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Splash.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Splash.route) {
                            SplashScreen(
                                onSplashFinished = {
                                    navController.navigate(Screen.Main.route) {
                                        popUpTo(Screen.Splash.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(Screen.Onboarding.route) {
                            OnboardingScreen(
                                onFinishOnboarding = {
                                    navController.navigate(Screen.Main.route) {
                                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(Screen.Login.route) {
                            LoginScreen(
                                viewModel = bazaarViewModel,
                                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                                onNavigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) },
                                onLoginSuccess = {
                                    navController.navigate(Screen.Main.route) {
                                        popUpTo(Screen.Login.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(Screen.Register.route) {
                            RegisterScreen(
                                viewModel = bazaarViewModel,
                                onNavigateToLogin = { navController.popBackStack() },
                                onRegisterSuccess = {
                                    navController.navigate(Screen.Main.route) {
                                        popUpTo(Screen.Register.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(Screen.ForgotPassword.route) {
                            ForgotPasswordScreen(
                                viewModel = bazaarViewModel,
                                onNavigateBack = { navController.popBackStack() },
                                onOtpSent = { identifier ->
                                    navController.navigate(Screen.OtpVerification.createRoute(identifier))
                                }
                            )
                        }

                        composable(
                            route = Screen.OtpVerification.route,
                            arguments = listOf(navArgument("identifier") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val identifier = backStackEntry.arguments?.getString("identifier") ?: ""
                            OtpVerificationScreen(
                                identifier = identifier,
                                viewModel = bazaarViewModel,
                                onNavigateBack = { navController.popBackStack() },
                                onVerificationSuccess = {
                                    navController.navigate(Screen.Main.route) {
                                        popUpTo(Screen.Login.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(Screen.Main.route) {
                            MainScreen(
                                viewModel = bazaarViewModel,
                                onNavigateToSearch = { bazaarViewModel.selectTab(BottomNavTab.SEARCH) },
                                onNavigateToCart = { navController.navigate(Screen.Cart.route) },
                                onNavigateToNotifications = { navController.navigate(Screen.Notifications.route) },
                                onNavigateToCategory = { id, name ->
                                    navController.navigate(Screen.CategoryProducts.createRoute(id, name))
                                },
                                onNavigateToProduct = { productId ->
                                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                                },
                                onNavigateToOrders = { navController.navigate(Screen.Orders.route) },
                                onNavigateToAddresses = { navController.navigate(Screen.Addresses.route) },
                                onNavigateToLogin = { navController.navigate(Screen.Login.route) }
                            )
                        }

                        composable(
                            route = Screen.CategoryProducts.route,
                            arguments = listOf(
                                navArgument("categoryId") { type = NavType.StringType },
                                navArgument("categoryName") { type = NavType.StringType }
                            )
                        ) { entry ->
                            val catId = entry.arguments?.getString("categoryId") ?: ""
                            val catName = entry.arguments?.getString("categoryName") ?: ""
                            CategoryProductsScreen(
                                categoryId = catId,
                                categoryName = catName,
                                viewModel = bazaarViewModel,
                                onNavigateBack = { navController.popBackStack() },
                                onProductClick = { prodId ->
                                    navController.navigate(Screen.ProductDetail.createRoute(prodId))
                                }
                            )
                        }

                        composable(
                            route = Screen.ProductDetail.route,
                            arguments = listOf(navArgument("productId") { type = NavType.StringType })
                        ) { entry ->
                            val prodId = entry.arguments?.getString("productId") ?: ""
                            ProductDetailScreen(
                                productId = prodId,
                                viewModel = bazaarViewModel,
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToCart = { navController.navigate(Screen.Cart.route) },
                                onNavigateToCheckout = { navController.navigate(Screen.Checkout.route) },
                                onProductClick = { newId ->
                                    navController.navigate(Screen.ProductDetail.createRoute(newId))
                                }
                            )
                        }

                        composable(Screen.Cart.route) {
                            CartScreen(
                                viewModel = bazaarViewModel,
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToCheckout = { navController.navigate(Screen.Checkout.route) },
                                onStartShopping = {
                                    bazaarViewModel.selectTab(BottomNavTab.HOME)
                                    navController.navigate(Screen.Main.route) {
                                        popUpTo(Screen.Main.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(Screen.Checkout.route) {
                            CheckoutScreen(
                                viewModel = bazaarViewModel,
                                onNavigateBack = { navController.popBackStack() },
                                onOrderPlaced = { orderId ->
                                    navController.navigate(Screen.OrderConfirmation.createRoute(orderId)) {
                                        popUpTo(Screen.Cart.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(
                            route = Screen.OrderConfirmation.route,
                            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
                        ) { entry ->
                            val orderId = entry.arguments?.getString("orderId") ?: ""
                            OrderConfirmationScreen(
                                orderId = orderId,
                                viewModel = bazaarViewModel,
                                onTrackOrder = { id ->
                                    navController.navigate(Screen.OrderTracking.createRoute(id))
                                },
                                onContinueShopping = {
                                    bazaarViewModel.selectTab(BottomNavTab.HOME)
                                    navController.navigate(Screen.Main.route) {
                                        popUpTo(Screen.Main.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(Screen.Orders.route) {
                            OrdersScreen(
                                viewModel = bazaarViewModel,
                                onNavigateBack = { navController.popBackStack() },
                                onOrderClick = { id ->
                                    navController.navigate(Screen.OrderDetail.createRoute(id))
                                }
                            )
                        }

                        composable(
                            route = Screen.OrderDetail.route,
                            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
                        ) { entry ->
                            val orderId = entry.arguments?.getString("orderId") ?: ""
                            OrderDetailScreen(
                                orderId = orderId,
                                viewModel = bazaarViewModel,
                                onNavigateBack = { navController.popBackStack() },
                                onTrackOrder = { id ->
                                    navController.navigate(Screen.OrderTracking.createRoute(id))
                                }
                            )
                        }

                        composable(
                            route = Screen.OrderTracking.route,
                            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
                        ) { entry ->
                            val orderId = entry.arguments?.getString("orderId") ?: ""
                            OrderTrackingScreen(
                                orderId = orderId,
                                viewModel = bazaarViewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable(Screen.Notifications.route) {
                            NotificationsScreen(
                                viewModel = bazaarViewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable(Screen.Addresses.route) {
                            AddressesScreen(
                                viewModel = bazaarViewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
