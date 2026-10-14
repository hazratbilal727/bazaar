package com.example.domain.repository

import com.example.domain.model.*
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>
    fun getProductById(id: String): Flow<Product?>
    fun getProductsByCategory(categoryId: String): Flow<List<Product>>
    fun getFeaturedProducts(): Flow<List<Product>>
    fun getFlashSaleProducts(): Flow<List<Product>>
    fun getTrendingProducts(): Flow<List<Product>>
    fun getNewArrivals(): Flow<List<Product>>
    fun getBestSellers(): Flow<List<Product>>
    fun searchProducts(query: String, filterState: FilterState): Flow<List<Product>>
    fun getAvailableBrands(): Flow<List<String>>
}

interface CategoryRepository {
    fun getCategories(): Flow<List<Category>>
    fun getCategoryById(id: String): Flow<Category?>
}

interface CartRepository {
    fun getCartItems(): Flow<List<CartItem>>
    suspend fun addToCart(product: Product, quantity: Int, color: String?, size: String?)
    suspend fun updateQuantity(cartItemId: String, newQuantity: Int)
    suspend fun removeFromCart(cartItemId: String)
    suspend fun toggleItemSelected(cartItemId: String, isSelected: Boolean)
    suspend fun selectAllItems(isSelected: Boolean)
    suspend fun clearCart()
}

interface WishlistRepository {
    fun getWishlistProductIds(): Flow<Set<String>>
    fun getWishlistProducts(): Flow<List<Product>>
    suspend fun toggleWishlist(productId: String)
    suspend fun isWishlisted(productId: String): Boolean
}

interface OrderRepository {
    fun getOrders(): Flow<List<Order>>
    fun getOrderById(orderId: String): Flow<Order?>
    suspend fun placeOrder(
        items: List<CartItem>,
        deliveryAddress: Address,
        paymentMethod: String,
        subtotal: Double,
        deliveryFee: Double,
        discountAmount: Double
    ): Order
    suspend fun cancelOrder(orderId: String, reason: String)
    suspend fun requestReturn(orderId: String, reason: String)
}

interface AuthRepository {
    fun getCurrentUser(): Flow<User?>
    suspend fun login(emailOrPhone: String, password: String):Result<User>
    suspend fun register(name: String, email: String, phone: String, password: String): Result<User>
    suspend fun continueAsGuest(): User
    suspend fun sendOtp(phoneOrEmail: String): Result<Boolean>
    suspend fun verifyOtp(phoneOrEmail: String, code: String): Result<Boolean>
    suspend fun resetPassword(phoneOrEmail: String, newPassword: String): Result<Boolean>
    suspend fun logout()
}

interface UserRepository {
    fun getUserProfile(): Flow<User?>
    suspend fun updateProfile(name: String, email: String, phone: String)
}

interface AddressRepository {
    fun getAddresses(): Flow<List<Address>>
    suspend fun addAddress(address: Address)
    suspend fun updateAddress(address: Address)
    suspend fun deleteAddress(addressId: String)
    suspend fun setDefaultAddress(addressId: String)
}

interface ReviewRepository {
    fun getReviewsForProduct(productId: String): Flow<List<Review>>
    suspend fun addReview(review: Review)
    suspend fun markHelpful(reviewId: String)
}

interface NotificationRepository {
    fun getNotifications(): Flow<List<NotificationItem>>
    suspend fun markAsRead(id: String)
    suspend fun markAllAsRead()
    suspend fun deleteNotification(id: String)
}
