package com.example.data.repository

import com.example.data.mock.MockData
import com.example.domain.model.*
import com.example.domain.repository.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class MockProductRepository : ProductRepository {
    private val _products = MutableStateFlow(MockData.products)

    override fun getAllProducts(): Flow<List<Product>> = _products.asStateFlow()

    override fun getProductById(id: String): Flow<Product?> =
        _products.map { list -> list.find { it.id == id } }

    override fun getProductsByCategory(categoryId: String): Flow<List<Product>> =
        _products.map { list -> list.filter { it.categoryId == categoryId } }

    override fun getFeaturedProducts(): Flow<List<Product>> =
        _products.map { list -> list.filter { it.isFeatured } }

    override fun getFlashSaleProducts(): Flow<List<Product>> =
        _products.map { list -> list.filter { it.isFlashSale } }

    override fun getTrendingProducts(): Flow<List<Product>> =
        _products.map { list -> list.filter { it.isTrending } }

    override fun getNewArrivals(): Flow<List<Product>> =
        _products.map { list -> list.filter { it.isNewArrival } }

    override fun getBestSellers(): Flow<List<Product>> =
        _products.map { list -> list.filter { it.isBestSeller } }

    override fun searchProducts(query: String, filterState: FilterState): Flow<List<Product>> =
        _products.map { list ->
            var result = list

            if (query.isNotBlank()) {
                val q = query.trim().lowercase()
                result = result.filter {
                    it.name.lowercase().contains(q) ||
                    it.brand.lowercase().contains(q) ||
                    it.categoryName.lowercase().contains(q) ||
                    it.description.lowercase().contains(q)
                }
            }

            if (!filterState.selectedCategory.isNullOrBlank()) {
                result = result.filter { it.categoryId == filterState.selectedCategory }
            }

            if (!filterState.selectedBrand.isNullOrBlank()) {
                result = result.filter { it.brand.equals(filterState.selectedBrand, ignoreCase = true) }
            }

            result = result.filter {
                it.price >= filterState.minPrice && it.price <= filterState.maxPrice
            }

            if (filterState.minRating > 0f) {
                result = result.filter { it.rating >= filterState.minRating }
            }

            if (filterState.inStockOnly) {
                result = result.filter { it.stock > 0 }
            }

            when (filterState.sortOption) {
                SortOption.PRICE_LOW_HIGH -> result.sortedBy { it.price }
                SortOption.PRICE_HIGH_LOW -> result.sortedByDescending { it.price }
                SortOption.RATING -> result.sortedByDescending { it.rating }
                SortOption.DISCOUNT -> result.sortedByDescending { it.discountPercent }
                SortOption.NEWEST -> result.sortedByDescending { if (it.isNewArrival) 1 else 0 }
                SortOption.POPULARITY -> result.sortedByDescending { it.reviewCount }
            }
        }

    override fun getAvailableBrands(): Flow<List<String>> =
        _products.map { list -> list.map { it.brand }.distinct().sorted() }
}

class MockCategoryRepository : CategoryRepository {
    private val _categories = MutableStateFlow(MockData.categories)

    override fun getCategories(): Flow<List<Category>> = _categories.asStateFlow()

    override fun getCategoryById(id: String): Flow<Category?> =
        _categories.map { list -> list.find { it.id == id } }
}

class MockCartRepository : CartRepository {
    private val _cartItems = MutableStateFlow<List<CartItem>>(
        listOf(
            CartItem(
                id = "c_init_1",
                product = MockData.products[0], // Samsung S24 Ultra
                quantity = 1,
                selectedColor = "Titanium Black",
                selectedSize = "256GB"
            ),
            CartItem(
                id = "c_init_2",
                product = MockData.products[18], // J. Kurta
                quantity = 1,
                selectedColor = "Royal Navy",
                selectedSize = "L"
            )
        )
    )

    override fun getCartItems(): Flow<List<CartItem>> = _cartItems.asStateFlow()

    override suspend fun addToCart(product: Product, quantity: Int, color: String?, size: String?) {
        val current = _cartItems.value.toMutableList()
        val existingIndex = current.indexOfFirst {
            it.product.id == product.id &&
            it.selectedColor == color &&
            it.selectedSize == size
        }

        if (existingIndex >= 0) {
            val existing = current[existingIndex]
            current[existingIndex] = existing.copy(quantity = existing.quantity + quantity)
        } else {
            current.add(
                CartItem(
                    id = UUID.randomUUID().toString(),
                    product = product,
                    quantity = quantity,
                    selectedColor = color,
                    selectedSize = size,
                    isSelected = true
                )
            )
        }
        _cartItems.value = current
    }

    override suspend fun updateQuantity(cartItemId: String, newQuantity: Int) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.id == cartItemId }
        if (index >= 0) {
            if (newQuantity <= 0) {
                current.removeAt(index)
            } else {
                current[index] = current[index].copy(quantity = newQuantity)
            }
            _cartItems.value = current
        }
    }

    override suspend fun removeFromCart(cartItemId: String) {
        _cartItems.value = _cartItems.value.filterNot { it.id == cartItemId }
    }

    override suspend fun toggleItemSelected(cartItemId: String, isSelected: Boolean) {
        _cartItems.value = _cartItems.value.map {
            if (it.id == cartItemId) it.copy(isSelected = isSelected) else it
        }
    }

    override suspend fun selectAllItems(isSelected: Boolean) {
        _cartItems.value = _cartItems.value.map { it.copy(isSelected = isSelected) }
    }

    override suspend fun clearCart() {
        _cartItems.value = emptyList()
    }
}

class MockWishlistRepository : WishlistRepository {
    private val _wishlistIds = MutableStateFlow<Set<String>>(
        setOf(MockData.products[1].id, MockData.products[12].id, MockData.products[24].id)
    )

    override fun getWishlistProductIds(): Flow<Set<String>> = _wishlistIds.asStateFlow()

    override fun getWishlistProducts(): Flow<List<Product>> =
        _wishlistIds.map { ids ->
            MockData.products.filter { ids.contains(it.id) }
        }

    override suspend fun toggleWishlist(productId: String) {
        val current = _wishlistIds.value.toMutableSet()
        if (current.contains(productId)) {
            current.remove(productId)
        } else {
            current.add(productId)
        }
        _wishlistIds.value = current
    }

    override suspend fun isWishlisted(productId: String): Boolean =
        _wishlistIds.value.contains(productId)
}

class MockOrderRepository(private val addressRepository: AddressRepository) : OrderRepository {
    private val _orders = MutableStateFlow(MockData.sampleOrders)

    override fun getOrders(): Flow<List<Order>> = _orders.asStateFlow()

    override fun getOrderById(orderId: String): Flow<Order?> =
        _orders.map { list -> list.find { it.id == orderId } }

    override suspend fun placeOrder(
        items: List<CartItem>,
        deliveryAddress: Address,
        paymentMethod: String,
        subtotal: Double,
        deliveryFee: Double,
        discountAmount: Double
    ): Order {
        val now = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.US).format(Date())
        val estDate = SimpleDateFormat("MMM dd, yyyy", Locale.US).format(Date(System.currentTimeMillis() + 3 * 86400000L))
        val orderNum = "BZ-${(10000..99999).random()}"
        val newOrder = Order(
            id = "ord_${UUID.randomUUID()}",
            orderNumber = orderNum,
            items = items,
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            discountAmount = discountAmount,
            totalAmount = (subtotal + deliveryFee - discountAmount).coerceAtLeast(0.0),
            status = OrderStatus.CONFIRMED,
            deliveryAddress = deliveryAddress,
            paymentMethod = paymentMethod,
            orderDate = now,
            estimatedDeliveryDate = estDate,
            trackingEvents = listOf(
                TrackingEvent("Order Placed", "Order successfully recorded via Bazaar checkout.", now, true),
                TrackingEvent("Order Confirmed", "Verified and routed to fulfillment warehouse.", "Just now", true),
                TrackingEvent("Packing in Progress", "Goods being picked and packaged securely.", "In progress", false),
                TrackingEvent("Dispatched with Courier", "Awaiting handover to TCS Express.", "Pending", false),
                TrackingEvent("Out for Delivery", "Rider assigned for delivery.", "Pending", false),
                TrackingEvent("Delivered", "Delivered to recipient.", "Pending", false)
            )
        )
        val list = _orders.value.toMutableList()
        list.add(0, newOrder)
        _orders.value = list
        return newOrder
    }

    override suspend fun cancelOrder(orderId: String, reason: String) {
        _orders.value = _orders.value.map { order ->
            if (order.id == orderId) {
                order.copy(
                    status = OrderStatus.CANCELLED,
                    cancellationReason = reason
                )
            } else order
        }
    }

    override suspend fun requestReturn(orderId: String, reason: String) {
        _orders.value = _orders.value.map { order ->
            if (order.id == orderId) {
                order.copy(
                    status = OrderStatus.RETURNED,
                    returnReason = reason
                )
            } else order
        }
    }
}

class MockAuthRepository : AuthRepository {
    private val _currentUser = MutableStateFlow<User?>(MockData.currentUser)

    override fun getCurrentUser(): Flow<User?> = _currentUser.asStateFlow()

    override suspend fun login(emailOrPhone: String, password: String): Result<User> {
        val user = User(
            id = "usr_${UUID.randomUUID()}",
            name = if (emailOrPhone.contains("@")) emailOrPhone.substringBefore("@").replaceFirstChar { it.uppercase() } else "Bilal Khan",
            email = if (emailOrPhone.contains("@")) emailOrPhone else "user@bazaar.pk",
            phoneNumber = if (emailOrPhone.contains("@")) "+92 300 1234567" else emailOrPhone,
            isGuest = false
        )
        _currentUser.value = user
        return Result.success(user)
    }

    override suspend fun register(name: String, email: String, phone: String, password: String): Result<User> {
        val user = User(
            id = "usr_${UUID.randomUUID()}",
            name = name,
            email = email,
            phoneNumber = phone,
            isGuest = false
        )
        _currentUser.value = user
        return Result.success(user)
    }

    override suspend fun continueAsGuest(): User {
        val guest = User(
            id = "guest_${UUID.randomUUID().toString().take(6)}",
            name = "Guest Shopper",
            email = "guest@bazaar.pk",
            phoneNumber = "+92 300 0000000",
            isGuest = true
        )
        _currentUser.value = guest
        return guest
    }

    override suspend fun sendOtp(phoneOrEmail: String): Result<Boolean> {
        return Result.success(true)
    }

    override suspend fun verifyOtp(phoneOrEmail: String, code: String): Result<Boolean> {
        return if (code == "1234" || code.length == 4 || code.length == 6) {
            Result.success(true)
        } else {
            Result.failure(IllegalArgumentException("Invalid verification code. Enter 1234."))
        }
    }

    override suspend fun resetPassword(phoneOrEmail: String, newPassword: String): Result<Boolean> {
        return Result.success(true)
    }

    override suspend fun logout() {
        _currentUser.value = null
    }
}

class MockUserRepository : UserRepository {
    private val _user = MutableStateFlow<User?>(MockData.currentUser)

    override fun getUserProfile(): Flow<User?> = _user.asStateFlow()

    override suspend fun updateProfile(name: String, email: String, phone: String) {
        _user.value = _user.value?.copy(name = name, email = email, phoneNumber = phone)
    }
}

class MockAddressRepository : AddressRepository {
    private val _addresses = MutableStateFlow(MockData.sampleAddresses)

    override fun getAddresses(): Flow<List<Address>> = _addresses.asStateFlow()

    override suspend fun addAddress(address: Address) {
        val list = _addresses.value.toMutableList()
        if (address.isDefault || list.isEmpty()) {
            list.replaceAll { it.copy(isDefault = false) }
        }
        list.add(0, address)
        _addresses.value = list
    }

    override suspend fun updateAddress(address: Address) {
        val list = _addresses.value.map {
            if (it.id == address.id) address else if (address.isDefault) it.copy(isDefault = false) else it
        }
        _addresses.value = list
    }

    override suspend fun deleteAddress(addressId: String) {
        _addresses.value = _addresses.value.filterNot { it.id == addressId }
    }

    override suspend fun setDefaultAddress(addressId: String) {
        _addresses.value = _addresses.value.map {
            it.copy(isDefault = it.id == addressId)
        }
    }
}

class MockReviewRepository : ReviewRepository {
    private val _reviews = MutableStateFlow(MockData.sampleReviews)

    override fun getReviewsForProduct(productId: String): Flow<List<Review>> =
        _reviews.map { list -> list.filter { it.productId == productId } }

    override suspend fun addReview(review: Review) {
        val list = _reviews.value.toMutableList()
        list.add(0, review)
        _reviews.value = list
    }

    override suspend fun markHelpful(reviewId: String) {
        _reviews.value = _reviews.value.map {
            if (it.id == reviewId) it.copy(helpfulCount = it.helpfulCount + 1) else it
        }
    }
}

class MockNotificationRepository : NotificationRepository {
    private val _notifications = MutableStateFlow(MockData.sampleNotifications)

    override fun getNotifications(): Flow<List<NotificationItem>> = _notifications.asStateFlow()

    override suspend fun markAsRead(id: String) {
        _notifications.value = _notifications.value.map {
            if (it.id == id) it.copy(isRead = true) else it
        }
    }

    override suspend fun markAllAsRead() {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
    }

    override suspend fun deleteNotification(id: String) {
        _notifications.value = _notifications.value.filterNot { it.id == id }
    }
}
