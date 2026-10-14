package com.example.feature.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.core.navigation.BottomNavTab
import com.example.feature.category.CategoryScreen
import com.example.feature.home.HomeScreen
import com.example.feature.profile.ProfileScreen
import com.example.feature.search.SearchScreen
import com.example.feature.wishlist.WishlistScreen
import com.example.ui.theme.RoyalBluePrimary
import com.example.ui.theme.VibrantOrangeSecondary
import com.example.ui.viewmodel.BazaarViewModel

@Composable
fun MainScreen(
    viewModel: BazaarViewModel,
    onNavigateToSearch: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToCategory: (String, String) -> Unit,
    onNavigateToProduct: (String) -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToAddresses: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val wishlistCount by viewModel.wishlistIds.collectAsState()

    // BackHandler: If not on Home, press back returns to Home
    BackHandler(enabled = currentTab != BottomNavTab.HOME) {
        viewModel.selectTab(BottomNavTab.HOME)
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                windowInsets = NavigationBarDefaults.windowInsets
            ) {
                // HOME
                NavigationBarItem(
                    selected = currentTab == BottomNavTab.HOME,
                    onClick = { viewModel.selectTab(BottomNavTab.HOME) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == BottomNavTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                            contentDescription = "Home"
                        )
                    },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RoyalBluePrimary,
                        selectedTextColor = RoyalBluePrimary,
                        indicatorColor = RoyalBluePrimary.copy(alpha = 0.12f)
                    ),
                    modifier = Modifier.testTag(BottomNavTab.HOME.testTag)
                )

                // CATEGORIES
                NavigationBarItem(
                    selected = currentTab == BottomNavTab.CATEGORIES,
                    onClick = { viewModel.selectTab(BottomNavTab.CATEGORIES) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == BottomNavTab.CATEGORIES) Icons.Filled.GridView else Icons.Outlined.GridView,
                            contentDescription = "Categories"
                        )
                    },
                    label = { Text("Categories") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RoyalBluePrimary,
                        selectedTextColor = RoyalBluePrimary,
                        indicatorColor = RoyalBluePrimary.copy(alpha = 0.12f)
                    ),
                    modifier = Modifier.testTag(BottomNavTab.CATEGORIES.testTag)
                )

                // SEARCH
                NavigationBarItem(
                    selected = currentTab == BottomNavTab.SEARCH,
                    onClick = { viewModel.selectTab(BottomNavTab.SEARCH) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == BottomNavTab.SEARCH) Icons.Filled.Search else Icons.Outlined.Search,
                            contentDescription = "Search"
                        )
                    },
                    label = { Text("Search") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RoyalBluePrimary,
                        selectedTextColor = RoyalBluePrimary,
                        indicatorColor = RoyalBluePrimary.copy(alpha = 0.12f)
                    ),
                    modifier = Modifier.testTag(BottomNavTab.SEARCH.testTag)
                )

                // WISHLIST
                NavigationBarItem(
                    selected = currentTab == BottomNavTab.WISHLIST,
                    onClick = { viewModel.selectTab(BottomNavTab.WISHLIST) },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (wishlistCount.isNotEmpty()) {
                                    Badge(containerColor = VibrantOrangeSecondary) {
                                        Text(wishlistCount.size.toString())
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (currentTab == BottomNavTab.WISHLIST) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Wishlist"
                            )
                        }
                    },
                    label = { Text("Wishlist") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RoyalBluePrimary,
                        selectedTextColor = RoyalBluePrimary,
                        indicatorColor = RoyalBluePrimary.copy(alpha = 0.12f)
                    ),
                    modifier = Modifier.testTag(BottomNavTab.WISHLIST.testTag)
                )

                // PROFILE
                NavigationBarItem(
                    selected = currentTab == BottomNavTab.PROFILE,
                    onClick = { viewModel.selectTab(BottomNavTab.PROFILE) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == BottomNavTab.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
                            contentDescription = "Profile"
                        )
                    },
                    label = { Text("Profile") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RoyalBluePrimary,
                        selectedTextColor = RoyalBluePrimary,
                        indicatorColor = RoyalBluePrimary.copy(alpha = 0.12f)
                    ),
                    modifier = Modifier.testTag(BottomNavTab.PROFILE.testTag)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                BottomNavTab.HOME -> {
                    HomeScreen(
                        viewModel = viewModel,
                        onNavigateToSearch = { viewModel.selectTab(BottomNavTab.SEARCH) },
                        onNavigateToCart = onNavigateToCart,
                        onNavigateToNotifications = onNavigateToNotifications,
                        onNavigateToCategory = onNavigateToCategory,
                        onNavigateToProduct = onNavigateToProduct
                    )
                }
                BottomNavTab.CATEGORIES -> {
                    CategoryScreen(
                        viewModel = viewModel,
                        onCategoryClick = onNavigateToCategory
                    )
                }
                BottomNavTab.SEARCH -> {
                    SearchScreen(
                        viewModel = viewModel,
                        onNavigateBack = { viewModel.selectTab(BottomNavTab.HOME) },
                        onProductClick = onNavigateToProduct
                    )
                }
                BottomNavTab.WISHLIST -> {
                    WishlistScreen(
                        viewModel = viewModel,
                        onProductClick = onNavigateToProduct,
                        onStartShopping = { viewModel.selectTab(BottomNavTab.HOME) }
                    )
                }
                BottomNavTab.PROFILE -> {
                    ProfileScreen(
                        viewModel = viewModel,
                        onNavigateToOrders = onNavigateToOrders,
                        onNavigateToWishlist = { viewModel.selectTab(BottomNavTab.WISHLIST) },
                        onNavigateToAddresses = onNavigateToAddresses,
                        onNavigateToNotifications = onNavigateToNotifications,
                        onNavigateToLogin = onNavigateToLogin
                    )
                }
            }
        }
    }
}
