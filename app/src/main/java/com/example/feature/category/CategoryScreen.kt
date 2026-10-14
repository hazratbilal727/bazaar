package com.example.feature.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.designsystem.EmptyStateView
import com.example.core.designsystem.ProductCard
import com.example.domain.model.Category
import com.example.domain.model.Product
import com.example.domain.model.SortOption
import com.example.ui.theme.RoyalBluePrimary
import com.example.ui.theme.VibrantOrangeSecondary
import com.example.ui.viewmodel.BazaarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    viewModel: BazaarViewModel,
    onCategoryClick: (String, String) -> Unit
) {
    val categories by viewModel.categories.collectAsState()
    var selectedCategoryId by remember { mutableStateOf(categories.firstOrNull()?.id ?: "cat_mobiles") }
    val selectedCategory = categories.find { it.id == selectedCategoryId } ?: categories.firstOrNull()

    val allProducts by viewModel.productRepository.getAllProducts().collectAsState(initial = emptyList())
    val categoryProducts = remember(selectedCategoryId, allProducts) {
        allProducts.filter { it.categoryId == selectedCategoryId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "All Categories",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Left sidebar: Category list
            LazyColumn(
                modifier = Modifier
                    .width(110.dp)
                    .fillMaxHeight()
                    .background(Color(0xFFF1F5F9))
            ) {
                items(categories) { category ->
                    val isSelected = category.id == selectedCategoryId
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedCategoryId = category.id }
                            .background(if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent)
                            .padding(vertical = 14.dp, horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Surface(
                                shape = CircleShape,
                                color = if (isSelected) RoyalBluePrimary else Color.White,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = getCategoryIcon(category.id),
                                        contentDescription = null,
                                        tint = if (isSelected) Color.White else RoyalBluePrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) RoyalBluePrimary else MaterialTheme.colorScheme.onSurface,
                                maxLines = 2
                            )
                        }
                    }
                }
            }

            // Right pane: Subcategories & Products
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(12.dp)
            ) {
                if (selectedCategory != null) {
                    Text(
                        text = selectedCategory.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Subcategory Chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        items(selectedCategory.subcategories) { sub ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFEFF6FF),
                                modifier = Modifier.clickable {
                                    onCategoryClick(selectedCategory.id, "${selectedCategory.name} - $sub")
                                }
                            ) {
                                Text(
                                    text = sub,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = RoyalBluePrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${categoryProducts.size} Items Available",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        TextButton(onClick = { onCategoryClick(selectedCategory.id, selectedCategory.name) }) {
                            Text("View All", fontWeight = FontWeight.Bold, color = RoyalBluePrimary)
                        }
                    }

                    // Products in this Category
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(categoryProducts) { product ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surface,
                                shadowElevation = 1.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onCategoryClick(selectedCategory.id, product.name) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = product.name,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            maxLines = 2
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Rs. ${product.price.toInt()}",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = RoyalBluePrimary
                                        )
                                    }
                                    IconButton(
                                        onClick = { viewModel.addToCart(product) },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(RoyalBluePrimary, CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.AddShoppingCart,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryProductsScreen(
    categoryId: String,
    categoryName: String,
    viewModel: BazaarViewModel,
    onNavigateBack: () -> Unit,
    onProductClick: (String) -> Unit
) {
    val allProducts by viewModel.productRepository.getAllProducts().collectAsState(initial = emptyList())
    val wishlistIds by viewModel.wishlistIds.collectAsState()

    var sortOption by remember { mutableStateOf(SortOption.POPULARITY) }
    var inStockOnly by remember { mutableStateOf(false) }

    val products = remember(categoryId, allProducts, sortOption, inStockOnly) {
        var list = allProducts.filter { it.categoryId == categoryId }
        if (inStockOnly) {
            list = list.filter { it.stock > 0 }
        }
        when (sortOption) {
            SortOption.PRICE_LOW_HIGH -> list.sortedBy { it.price }
            SortOption.PRICE_HIGH_LOW -> list.sortedByDescending { it.price }
            SortOption.RATING -> list.sortedByDescending { it.rating }
            SortOption.DISCOUNT -> list.sortedByDescending { it.discountPercent }
            SortOption.NEWEST -> list.sortedByDescending { if (it.isNewArrival) 1 else 0 }
            SortOption.POPULARITY -> list.sortedByDescending { it.reviewCount }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = categoryName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${products.size} Products",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Sort & Filter Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // In Stock Toggle
                FilterChip(
                    selected = inStockOnly,
                    onClick = { inStockOnly = !inStockOnly },
                    label = { Text("In Stock Only") },
                    leadingIcon = {
                        if (inStockOnly) Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                )

                // Sort Dropdown
                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedButton(
                        onClick = { expanded = true },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Filled.Sort, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(sortOption.displayName, fontSize = 12.sp)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        SortOption.values().forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.displayName) },
                                onClick = {
                                    sortOption = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            if (products.isEmpty()) {
                EmptyStateView(
                    icon = Icons.Outlined.Inventory2,
                    title = "No Products in $categoryName",
                    description = "We couldn't find items matching your current filters."
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(products, key = { it.id }) { product ->
                        ProductCard(
                            product = product,
                            isWishlisted = wishlistIds.contains(product.id),
                            onProductClick = { onProductClick(product.id) },
                            onWishlistToggle = { viewModel.toggleWishlist(product) },
                            onAddToCartClick = { viewModel.addToCart(product) }
                        )
                    }
                }
            }
        }
    }
}

fun getCategoryIcon(id: String) = when (id) {
    "cat_mobiles" -> Icons.Filled.Smartphone
    "cat_computers" -> Icons.Filled.Laptop
    "cat_electronics" -> Icons.Filled.Tv
    "cat_fashion" -> Icons.Filled.Checkroom
    "cat_shoes" -> Icons.Filled.DirectionsWalk
    "cat_watches" -> Icons.Filled.Watch
    "cat_beauty" -> Icons.Filled.Face
    "cat_home" -> Icons.Filled.Home
    "cat_groceries" -> Icons.Filled.LocalGroceryStore
    "cat_sports" -> Icons.Filled.SportsCricket
    "cat_toys" -> Icons.Filled.SmartToy
    else -> Icons.Filled.ShoppingBag
}
