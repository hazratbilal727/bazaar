package com.example.core.di

import com.example.data.repository.*
import com.example.domain.repository.*

class AppContainer {
    val addressRepository: AddressRepository by lazy { MockAddressRepository() }
    val productRepository: ProductRepository by lazy { MockProductRepository() }
    val categoryRepository: CategoryRepository by lazy { MockCategoryRepository() }
    val cartRepository: CartRepository by lazy { MockCartRepository() }
    val wishlistRepository: WishlistRepository by lazy { MockWishlistRepository() }
    val orderRepository: OrderRepository by lazy { MockOrderRepository(addressRepository) }
    val authRepository: AuthRepository by lazy { MockAuthRepository() }
    val userRepository: UserRepository by lazy { MockUserRepository() }
    val reviewRepository: ReviewRepository by lazy { MockReviewRepository() }
    val notificationRepository: NotificationRepository by lazy { MockNotificationRepository() }
}

object ServiceLocator {
    lateinit var container: AppContainer
        private set

    fun init() {
        if (!::container.isInitialized) {
            container = AppContainer()
        }
    }
}
