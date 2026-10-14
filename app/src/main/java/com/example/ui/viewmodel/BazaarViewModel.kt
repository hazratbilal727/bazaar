package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.di.ServiceLocator
import com.example.core.navigation.BottomNavTab
import com.example.domain.model.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BazaarViewModel : ViewModel() {
    private val appContainer = ServiceLocator.container

    val productRepository = appContainer.productRepository
    val categoryRepository = appContainer.categoryRepository
    val cartRepository = appContainer.cartRepository
    val wishlistRepository = appContainer.wishlistRepository
    val orderRepository = appContainer.orderRepository
    val authRepository = appContainer.authRepository
    val userRepository = appContainer.userRepository
    val addressRepository = appContainer.addressRepository
    val reviewRepository = appContainer.reviewRepository
    val notificationRepository = appContainer.notificationRepository

    // Global UI state
    val currentUser = authRepository.getCurrentUser().stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val cartItems = cartRepository.getCartItems().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val wishlistIds = wishlistRepository.getWishlistProductIds().stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())
    val wishlistProducts = wishlistRepository.getWishlistProducts().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val notifications = notificationRepository.getNotifications().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val addresses = addressRepository.getAddresses().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val orders = orderRepository.getOrders().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val categories = categoryRepository.getCategories().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val unreadNotificationCount = notifications.map { list -> list.count { !it.isRead } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val cartItemCount = cartItems.map { list -> list.sumOf { it.quantity } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    // Navigation and location
    val currentTab = MutableStateFlow(BottomNavTab.HOME)
    val deliveryLocation = MutableStateFlow("Gulberg III, Lahore")

    // Filter & Search State
    val filterState = MutableStateFlow(FilterState())
    val searchQuery = MutableStateFlow("")

    // Promo code state
    val appliedCoupon = MutableStateFlow<String?>(null)
    val couponDiscount = MutableStateFlow(0.0)

    // Snackbar events
    private val _snackbarChannel = Channel<String>(Channel.BUFFERED)
    val snackbarEvents = _snackbarChannel.receiveAsFlow()

    fun showSnackbar(message: String) {
        viewModelScope.launch {
            _snackbarChannel.send(message)
        }
    }

    fun selectTab(tab: BottomNavTab) {
        currentTab.value = tab
    }

    fun updateDeliveryLocation(location: String) {
        deliveryLocation.value = location
        showSnackbar("Delivery location updated to $location")
    }

    fun addToCart(product: Product, quantity: Int = 1, color: String? = null, size: String? = null) {
        viewModelScope.launch {
            cartRepository.addToCart(
                product = product,
                quantity = quantity,
                color = color ?: product.colors.firstOrNull(),
                size = size ?: product.sizes.firstOrNull()
            )
            showSnackbar("Added '${product.name.take(24)}...' to Cart")
        }
    }

    fun updateCartQuantity(cartItemId: String, newQty: Int) {
        viewModelScope.launch {
            cartRepository.updateQuantity(cartItemId, newQty)
        }
    }

    fun removeFromCart(cartItemId: String) {
        viewModelScope.launch {
            cartRepository.removeFromCart(cartItemId)
            showSnackbar("Item removed from Cart")
        }
    }

    fun toggleCartItemSelection(cartItemId: String, isSelected: Boolean) {
        viewModelScope.launch {
            cartRepository.toggleItemSelected(cartItemId, isSelected)
        }
    }

    fun selectAllCartItems(isSelected: Boolean) {
        viewModelScope.launch {
            cartRepository.selectAllItems(isSelected)
        }
    }

    fun toggleWishlist(product: Product) {
        viewModelScope.launch {
            val wasWishlisted = wishlistIds.value.contains(product.id)
            wishlistRepository.toggleWishlist(product.id)
            if (wasWishlisted) {
                showSnackbar("Removed from Wishlist")
            } else {
                showSnackbar("Added to Wishlist ❤️")
            }
        }
    }

    fun applyPromoCode(code: String): Boolean {
        val trimmed = code.trim().uppercase()
        return when (trimmed) {
            "BAZAAR10" -> {
                appliedCoupon.value = "BAZAAR10 (10% OFF)"
                couponDiscount.value = 0.10 // 10%
                showSnackbar("Coupon 'BAZAAR10' applied! 10% discount added.")
                true
            }
            "WELCOME500" -> {
                appliedCoupon.value = "WELCOME500 (Rs. 500 OFF)"
                couponDiscount.value = 500.0 // flat 500
                showSnackbar("Voucher 'WELCOME500' applied! Rs. 500 saved.")
                true
            }
            "FREESHIP" -> {
                appliedCoupon.value = "FREESHIP (Free Delivery)"
                couponDiscount.value = 150.0
                showSnackbar("Free delivery voucher applied!")
                true
            }
            else -> {
                showSnackbar("Invalid or expired coupon code. Try 'BAZAAR10' or 'WELCOME500'")
                false
            }
        }
    }

    fun removePromoCode() {
        appliedCoupon.value = null
        couponDiscount.value = 0.0
        showSnackbar("Coupon removed")
    }

    fun markNotificationAsRead(id: String) {
        viewModelScope.launch {
            notificationRepository.markAsRead(id)
        }
    }

    fun markAllNotificationsAsRead() {
        viewModelScope.launch {
            notificationRepository.markAllAsRead()
            showSnackbar("All notifications marked as read")
        }
    }

    fun deleteNotification(id: String) {
        viewModelScope.launch {
            notificationRepository.deleteNotification(id)
        }
    }

    fun addAddress(address: Address) {
        viewModelScope.launch {
            addressRepository.addAddress(address)
            showSnackbar("New delivery address added")
        }
    }

    fun updateAddress(address: Address) {
        viewModelScope.launch {
            addressRepository.updateAddress(address)
            showSnackbar("Address updated")
        }
    }

    fun deleteAddress(id: String) {
        viewModelScope.launch {
            addressRepository.deleteAddress(id)
            showSnackbar("Address removed")
        }
    }

    fun setDefaultAddress(id: String) {
        viewModelScope.launch {
            addressRepository.setDefaultAddress(id)
            showSnackbar("Default address updated")
        }
    }

    suspend fun placeOrder(
        items: List<CartItem>,
        deliveryAddress: Address,
        paymentMethod: String,
        deliveryFee: Double
    ): Order {
        val selectedItems = items.filter { it.isSelected }
        val subtotal = selectedItems.sumOf { it.totalPrice }
        val discount = when {
            couponDiscount.value > 0.0 && couponDiscount.value <= 1.0 -> subtotal * couponDiscount.value
            else -> couponDiscount.value
        }

        val order = orderRepository.placeOrder(
            items = selectedItems,
            deliveryAddress = deliveryAddress,
            paymentMethod = paymentMethod,
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            discountAmount = discount
        )

        // Clear placed items from cart
        cartRepository.clearCart()
        appliedCoupon.value = null
        couponDiscount.value = 0.0

        return order
    }

    fun cancelOrder(orderId: String, reason: String) {
        viewModelScope.launch {
            orderRepository.cancelOrder(orderId, reason)
            showSnackbar("Order cancellation requested: $reason")
        }
    }

    fun requestReturn(orderId: String, reason: String) {
        viewModelScope.launch {
            orderRepository.requestReturn(orderId, reason)
            showSnackbar("Return request submitted. Courier will pick up item.")
        }
    }

    fun addReview(review: Review) {
        viewModelScope.launch {
            reviewRepository.addReview(review)
            showSnackbar("Thank you! Your verified review has been published.")
        }
    }

    fun markReviewHelpful(reviewId: String) {
        viewModelScope.launch {
            reviewRepository.markHelpful(reviewId)
            showSnackbar("Marked review as helpful")
        }
    }

    fun updateProfile(name: String, email: String, phone: String) {
        viewModelScope.launch {
            userRepository.updateProfile(name, email, phone)
            showSnackbar("Profile updated successfully")
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            showSnackbar("Logged out successfully")
        }
    }
}
