package com.example.feature.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.EmptyStateView
import com.example.core.designsystem.ProductCard
import com.example.ui.theme.RoyalBluePrimary
import com.example.ui.viewmodel.BazaarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
    viewModel: BazaarViewModel,
    onProductClick: (String) -> Unit,
    onStartShopping: () -> Unit
) {
    val wishlistProducts by viewModel.wishlistProducts.collectAsState()
    val wishlistIds by viewModel.wishlistIds.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "My Wishlist",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "${wishlistProducts.size} saved items",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        if (wishlistProducts.isEmpty()) {
            EmptyStateView(
                icon = Icons.Outlined.FavoriteBorder,
                title = "Your Wishlist is Empty",
                description = "Tap the heart icon on any product to save it here for later price drops or quick purchase.",
                buttonText = "Explore Deals",
                onButtonClick = onStartShopping,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                items(wishlistProducts, key = { it.id }) { product ->
                    ProductCard(
                        product = product,
                        isWishlisted = true,
                        onProductClick = { onProductClick(product.id) },
                        onWishlistToggle = { viewModel.toggleWishlist(product) },
                        onAddToCartClick = {
                            viewModel.addToCart(product)
                            viewModel.toggleWishlist(product) // Move to cart
                        }
                    )
                }
            }
        }
    }
}
