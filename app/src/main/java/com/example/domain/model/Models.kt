package com.example.domain.model

import java.util.UUID

data class Product(
    val id: String,
    val name: String,
    val brand: String,
    val categoryId: String,
    val categoryName: String,
    val price: Double, // in PKR
    val originalPrice: Double, // in PKR
    val rating: Float,
    val reviewCount: Int,
    val stock: Int,
    val description: String,
    val specifications: Map<String, String> = emptyMap(),
    val imageUrls: List<String> = emptyList(),
    val colors: List<String> = emptyList(),
    val sizes: List<String> = emptyList(),
    val isFeatured: Boolean = false,
    val isTrending: Boolean = false,
    val isFlashSale: Boolean = false,
    val isNewArrival: Boolean = false,
    val isBestSeller: Boolean = false,
    val sellerName: String = "Bazaar Official Flagship",
    val sellerRating: Float = 4.9f,
    val deliveryDays: String = "2-3 business days"
) {
    val discountPercent: Int
        get() = if (originalPrice > price) {
            (((originalPrice - price) / originalPrice) * 100).toInt()
        } else 0
}

data class Category(
    val id: String,
    val name: String,
    val iconName: String,
    val subcategories: List<String> = emptyList(),
    val productCount: Int = 0
)

data class CartItem(
    val id: String = UUID.randomUUID().toString(),
    val product: Product,
    val quantity: Int = 1,
    val selectedColor: String? = null,
    val selectedSize: String? = null,
    val isSelected: Boolean = true
) {
    val totalPrice: Double
        get() = product.price * quantity
}

enum class OrderStatus(val title: String, val stepIndex: Int) {
    PENDING("Pending Confirmation", 0),
    CONFIRMED("Order Confirmed", 1),
    PROCESSING("Processing at Warehouse", 2),
    SHIPPED("Dispatched / In Transit", 3),
    OUT_FOR_DELIVERY("Out for Delivery", 4),
    DELIVERED("Delivered", 5),
    CANCELLED("Cancelled", -1),
    RETURNED("Returned", -2)
}

data class Order(
    val id: String,
    val orderNumber: String,
    val items: List<CartItem>,
    val subtotal: Double,
    val deliveryFee: Double,
    val discountAmount: Double,
    val totalAmount: Double,
    val status: OrderStatus,
    val deliveryAddress: Address,
    val paymentMethod: String,
    val orderDate: String,
    val estimatedDeliveryDate: String,
    val trackingEvents: List<TrackingEvent> = emptyList(),
    val cancellationReason: String? = null,
    val returnReason: String? = null
)

data class TrackingEvent(
    val title: String,
    val description: String,
    val timestamp: String,
    val isCompleted: Boolean
)

data class Address(
    val id: String = UUID.randomUUID().toString(),
    val recipientName: String,
    val phoneNumber: String,
    val city: String,
    val area: String,
    val streetAddress: String,
    val postalCode: String,
    val isDefault: Boolean = false,
    val addressLabel: String = "Home" // Home, Work, Other
) {
    val formattedAddress: String
        get() = "$streetAddress, $area, $city, Pakistan ($postalCode)"
}

data class Review(
    val id: String = UUID.randomUUID().toString(),
    val productId: String,
    val userName: String,
    val rating: Int,
    val title: String,
    val comment: String,
    val date: String,
    val verifiedPurchase: Boolean = true,
    val helpfulCount: Int = 0
)

data class NotificationItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val message: String,
    val timestamp: String,
    val type: NotificationType,
    val isRead: Boolean = false,
    val actionUrl: String? = null
)

enum class NotificationType {
    ORDER_UPDATE,
    FLASH_SALE,
    PRICE_DROP,
    PROMO_COUPON,
    SYSTEM
}

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val avatarUrl: String = "",
    val isGuest: Boolean = false
)

enum class SortOption(val displayName: String) {
    POPULARITY("Most Popular"),
    PRICE_LOW_HIGH("Price: Low to High"),
    PRICE_HIGH_LOW("Price: High to Low"),
    NEWEST("Newest Arrivals"),
    RATING("Best Rated"),
    DISCOUNT("Biggest Discount")
}

data class FilterState(
    val query: String = "",
    val selectedCategory: String? = null,
    val selectedBrand: String? = null,
    val minPrice: Double = 0.0,
    val maxPrice: Double = 500000.0,
    val minRating: Float = 0f,
    val inStockOnly: Boolean = false,
    val sortOption: SortOption = SortOption.POPULARITY
)
