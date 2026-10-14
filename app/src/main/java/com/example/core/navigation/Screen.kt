package com.example.core.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    object OtpVerification : Screen("otp_verification/{identifier}") {
        fun createRoute(identifier: String) = "otp_verification/$identifier"
    }
    object Main : Screen("main")
    object CategoryProducts : Screen("category_products/{categoryId}/{categoryName}") {
        fun createRoute(categoryId: String, categoryName: String) = "category_products/$categoryId/$categoryName"
    }
    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }
    object Cart : Screen("cart")
    object Checkout : Screen("checkout")
    object OrderConfirmation : Screen("order_confirmation/{orderId}") {
        fun createRoute(orderId: String) = "order_confirmation/$orderId"
    }
    object Orders : Screen("orders")
    object OrderDetail : Screen("order_detail/{orderId}") {
        fun createRoute(orderId: String) = "order_detail/$orderId"
    }
    object OrderTracking : Screen("order_tracking/{orderId}") {
        fun createRoute(orderId: String) = "order_tracking/$orderId"
    }
    object Notifications : Screen("notifications")
    object Addresses : Screen("addresses")
}

enum class BottomNavTab(val title: String, val testTag: String) {
    HOME("Home", "nav_tab_home"),
    CATEGORIES("Categories", "nav_tab_categories"),
    SEARCH("Search", "nav_tab_search"),
    WISHLIST("Wishlist", "nav_tab_wishlist"),
    PROFILE("Profile", "nav_tab_profile")
}
