package com.example.feature.search

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
import com.example.domain.model.FilterState
import com.example.domain.model.Product
import com.example.domain.model.SortOption
import com.example.ui.theme.RoyalBluePrimary
import com.example.ui.theme.VibrantOrangeSecondary
import com.example.ui.viewmodel.BazaarViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    viewModel: BazaarViewModel,
    onNavigateBack: () -> Unit,
    onProductClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var showFilterSheet by remember { mutableStateOf(false) }
    var showVoiceDialog by remember { mutableStateOf(false) }
    var recentSearches by remember {
        mutableStateOf(mutableListOf("Samsung S24 Ultra", "Nike Pegasus", "J. Kurta", "Sony XM5", "Basmati Rice"))
    }
    val trendingSearches = listOf("Smart Watches", "iPhone 15", "Air Fryer", "Cricket Bat", "Dumbbell Set", "Wireless Earbuds")

    val filterState by viewModel.filterState.collectAsState()
    val wishlistIds by viewModel.wishlistIds.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val availableBrands by viewModel.productRepository.getAvailableBrands().collectAsState(initial = emptyList())

    val searchResults by viewModel.productRepository.searchProducts(searchQuery, filterState).collectAsState(initial = emptyList())

    if (showFilterSheet) {
        FilterBottomSheet(
            currentFilter = filterState,
            categories = categories.map { it.id to it.name },
            brands = availableBrands,
            onApply = { newFilter ->
                viewModel.filterState.value = newFilter
                showFilterSheet = false
            },
            onReset = {
                viewModel.filterState.value = FilterState()
                showFilterSheet = false
            },
            onDismiss = { showFilterSheet = false }
        )
    }

    if (showVoiceDialog) {
        VoiceSearchDialog(
            onQueryRecognized = { recognizedText ->
                searchQuery = recognizedText
                showVoiceDialog = false
            },
            onDismiss = { showVoiceDialog = false }
        )
    }

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search products, brands, or categories...") },
                        leadingIcon = {
                            Icon(Icons.Filled.Search, contentDescription = null, tint = RoyalBluePrimary)
                        },
                        trailingIcon = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Filled.Clear, contentDescription = "Clear")
                                    }
                                }
                                IconButton(onClick = { showVoiceDialog = true }) {
                                    Icon(Icons.Filled.Mic, contentDescription = "Voice Search", tint = VibrantOrangeSecondary)
                                }
                            }
                        },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("search_text_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    IconButton(
                        onClick = { showFilterSheet = true },
                        modifier = Modifier.testTag("search_filter_btn")
                    ) {
                        BadgedBox(
                            badge = {
                                if (filterState.selectedCategory != null || filterState.selectedBrand != null || filterState.minRating > 0f) {
                                    Badge(containerColor = VibrantOrangeSecondary)
                                }
                            }
                        ) {
                            Icon(Icons.Filled.Tune, contentDescription = "Filters", tint = RoyalBluePrimary)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (searchQuery.isBlank()) {
                // Recent & Trending searches view
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Recent Searches
                    if (recentSearches.isNotEmpty()) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Recent Searches",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                TextButton(onClick = { recentSearches = mutableListOf() }) {
                                    Text("Clear All", color = MaterialTheme.colorScheme.error)
                                }
                            }
                        }

                        items(recentSearches) { recent ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { searchQuery = recent }
                                    .padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Outlined.History, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(text = recent, style = MaterialTheme.typography.bodyMedium)
                                }
                                IconButton(
                                    onClick = {
                                        recentSearches = recentSearches.toMutableList().also { it.remove(recent) }
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Filled.Close, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                                }
                            }
                        }

                        item { Spacer(modifier = Modifier.height(16.dp)) }
                    }

                    // Trending Searches
                    item {
                        Text(
                            text = "Trending Searches in Pakistan 🔥",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    item {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            androidx.compose.foundation.layout.FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                trendingSearches.forEach { trending ->
                                    SuggestionChip(
                                        onClick = {
                                            searchQuery = trending
                                            if (!recentSearches.contains(trending)) {
                                                recentSearches.add(0, trending)
                                            }
                                        },
                                        label = { Text(trending) }
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Active Search Results
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${searchResults.size} results for '$searchQuery'",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "Sort: ${filterState.sortOption.displayName}",
                        style = MaterialTheme.typography.labelSmall,
                        color = RoyalBluePrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (searchResults.isEmpty()) {
                    EmptyStateView(
                        icon = Icons.Outlined.SearchOff,
                        title = "No products found",
                        description = "We couldn't find matches for '$searchQuery'. Try checking spelling or use broader keywords.",
                        buttonText = "Clear Search",
                        onButtonClick = { searchQuery = "" }
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(searchResults, key = { it.id }) { product ->
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    currentFilter: FilterState,
    categories: List<Pair<String, String>>,
    brands: List<String>,
    onApply: (FilterState) -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit
) {
    var minPrice by remember { mutableStateOf(currentFilter.minPrice.toFloat()) }
    var maxPrice by remember { mutableStateOf(currentFilter.maxPrice.toFloat()) }
    var selectedCat by remember { mutableStateOf(currentFilter.selectedCategory) }
    var selectedBrand by remember { mutableStateOf(currentFilter.selectedBrand) }
    var minRating by remember { mutableStateOf(currentFilter.minRating) }
    var inStockOnly by remember { mutableStateOf(currentFilter.inStockOnly) }
    var sortOption by remember { mutableStateOf(currentFilter.sortOption) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filter Products",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onReset) {
                    Text("Reset All", color = MaterialTheme.colorScheme.error)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sort By Options
            Text("Sort By", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(SortOption.values()) { opt ->
                    FilterChip(
                        selected = sortOption == opt,
                        onClick = { sortOption = opt },
                        label = { Text(opt.displayName, fontSize = 12.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // In Stock Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("In-Stock Only", style = MaterialTheme.typography.bodyMedium)
                Switch(checked = inStockOnly, onCheckedChange = { inStockOnly = it })
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Minimum Rating Filter
            Text("Minimum Rating", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(0f to "All", 3f to "3★ & above", 4f to "4★ & above", 4.5f to "4.5★+").forEach { (rating, label) ->
                    FilterChip(
                        selected = minRating == rating,
                        onClick = { minRating = rating },
                        label = { Text(label, fontSize = 12.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Brand Selector
            Text("Brand", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = selectedBrand == null,
                        onClick = { selectedBrand = null },
                        label = { Text("All Brands") }
                    )
                }
                items(brands) { brand ->
                    FilterChip(
                        selected = selectedBrand == brand,
                        onClick = { selectedBrand = if (selectedBrand == brand) null else brand },
                        label = { Text(brand) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Apply Button
            Button(
                onClick = {
                    onApply(
                        currentFilter.copy(
                            selectedCategory = selectedCat,
                            selectedBrand = selectedBrand,
                            minPrice = minPrice.toDouble(),
                            maxPrice = maxPrice.toDouble(),
                            minRating = minRating,
                            inStockOnly = inStockOnly,
                            sortOption = sortOption
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("apply_filter_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBluePrimary)
            ) {
                Text("Apply Filters", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun VoiceSearchDialog(
    onQueryRecognized: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var isListening by remember { mutableStateOf(true) }
    var recognizedText by remember { mutableStateOf("Listening to your voice...") }

    LaunchedEffect(Unit) {
        delay(1500L)
        recognizedText = "Samsung Galaxy S24"
        delay(1000L)
        onQueryRecognized("Samsung Galaxy S24")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Mic, contentDescription = null, tint = VibrantOrangeSecondary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Voice Search", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.size(72.dp),
                    shape = CircleShape,
                    color = VibrantOrangeSecondary.copy(alpha = 0.15f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = VibrantOrangeSecondary,
                            modifier = Modifier.size(54.dp),
                            strokeWidth = 3.dp
                        )
                        Icon(Icons.Filled.Mic, contentDescription = null, tint = VibrantOrangeSecondary)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = recognizedText,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Try saying 'Nike Shoes' or 'Cricket Bat'",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
