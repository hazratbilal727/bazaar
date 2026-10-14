package com.example

import com.example.data.mock.MockData
import com.example.data.repository.*
import com.example.domain.model.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BazaarUnitTest {

    private lateinit var productRepository: MockProductRepository
    private lateinit var cartRepository: MockCartRepository
    private lateinit var wishlistRepository: MockWishlistRepository
    private lateinit var addressRepository: MockAddressRepository
    private lateinit var orderRepository: MockOrderRepository

    @Before
    fun setup() {
        productRepository = MockProductRepository()
        cartRepository = MockCartRepository()
        wishlistRepository = MockWishlistRepository()
        addressRepository = MockAddressRepository()
        orderRepository = MockOrderRepository(addressRepository)
    }

    @Test
    fun testMockProductsCountAndCategories() = runBlocking {
        val products = productRepository.getAllProducts().first()
        assertTrue("Product catalog should have at least 60 items", products.size >= 60)

        val categories = MockData.categories
        assertEquals("Should have 12 categories", 12, categories.size)
    }

    @Test
    fun testProductSearchAndFiltering() = runBlocking {
        // Search by query
        val samsungPhones = productRepository.searchProducts("Samsung", FilterState()).first()
        assertTrue("Search for Samsung should return matches", samsungPhones.isNotEmpty())
        assertTrue("All matches should contain Samsung", samsungPhones.all { it.name.contains("Samsung") || it.brand.contains("Samsung") })

        // Filter by inStockOnly
        val inStockList = productRepository.searchProducts("", FilterState(inStockOnly = true)).first()
        assertTrue("In-stock items should have stock > 0", inStockList.all { it.stock > 0 })

        // Filter by min rating
        val topRated = productRepository.searchProducts("", FilterState(minRating = 4.8f)).first()
        assertTrue("All items should have rating >= 4.8", topRated.all { it.rating >= 4.8f })
    }

    @Test
    fun testProductSortingPriceLowToHigh() = runBlocking {
        val sorted = productRepository.searchProducts("", FilterState(sortOption = SortOption.PRICE_LOW_HIGH)).first()
        for (i in 0 until sorted.size - 1) {
            assertTrue("Price should be ascending", sorted[i].price <= sorted[i + 1].price)
        }
    }

    @Test
    fun testProductSortingPriceHighToLow() = runBlocking {
        val sorted = productRepository.searchProducts("", FilterState(sortOption = SortOption.PRICE_HIGH_LOW)).first()
        for (i in 0 until sorted.size - 1) {
            assertTrue("Price should be descending", sorted[i].price >= sorted[i + 1].price)
        }
    }

    @Test
    fun testCartOperationsAndTotals() = runBlocking {
        cartRepository.clearCart()
        val emptyCart = cartRepository.getCartItems().first()
        assertEquals(0, emptyCart.size)

        val product = MockData.products[0]
        cartRepository.addToCart(product, 2, "Black", "256GB")

        val updatedCart = cartRepository.getCartItems().first()
        assertEquals(1, updatedCart.size)
        assertEquals(2, updatedCart[0].quantity)
        assertEquals(product.price * 2, updatedCart[0].totalPrice, 0.01)

        // Update quantity
        cartRepository.updateQuantity(updatedCart[0].id, 3)
        val modifiedCart = cartRepository.getCartItems().first()
        assertEquals(3, modifiedCart[0].quantity)

        // Remove from cart
        cartRepository.removeFromCart(modifiedCart[0].id)
        val finalCart = cartRepository.getCartItems().first()
        assertEquals(0, finalCart.size)
    }

    @Test
    fun testWishlistToggle() = runBlocking {
        val testProdId = "p_test_999"
        assertFalse(wishlistRepository.isWishlisted(testProdId))

        wishlistRepository.toggleWishlist(testProdId)
        assertTrue(wishlistRepository.isWishlisted(testProdId))

        wishlistRepository.toggleWishlist(testProdId)
        assertFalse(wishlistRepository.isWishlisted(testProdId))
    }

    @Test
    fun testOrderPlacement() = runBlocking {
        val address = MockData.sampleAddresses[0]
        val cartItems = listOf(
            CartItem(product = MockData.products[0], quantity = 1)
        )

        val order = orderRepository.placeOrder(
            items = cartItems,
            deliveryAddress = address,
            paymentMethod = "Cash on Delivery",
            subtotal = MockData.products[0].price,
            deliveryFee = 150.0,
            discountAmount = 500.0
        )

        assertNotNull(order.id)
        assertTrue(order.orderNumber.startsWith("BZ-"))
        assertEquals(OrderStatus.CONFIRMED, order.status)
        assertEquals(MockData.products[0].price + 150.0 - 500.0, order.totalAmount, 0.01)
    }
}
