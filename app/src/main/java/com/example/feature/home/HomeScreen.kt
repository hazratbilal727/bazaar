package com.example.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.core.designsystem.EmptyStateView
import com.example.core.designsystem.ProductCard
import com.example.core.designsystem.ShimmerLoadingBox
import com.example.domain.model.Category
import com.example.domain.model.Product
import com.example.ui.theme.*
import com.example.ui.viewmodel.BazaarViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: BazaarViewModel,
    onNavigateToSearch: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToCategory: (String, String) -> Unit,
    onNavigateToProduct: (String) -> Unit
) {
    val cartCount by viewModel.cartItemCount.collectAsState()
    val notifCount by viewModel.unreadNotificationCount.collectAsState()
    val deliveryLocation by viewModel.deliveryLocation.collectAsState()
    val wishlistIds by viewModel.wishlistIds.collectAsState()
    val categories by viewModel.categories.collectAsState()

    var showLocationDialog by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }
    var isShimmerLoading by remember { mutableStateOf(false) }

    val flashSaleProducts by viewModel.productRepository.getFlashSaleProducts().collectAsState(initial = emptyList())
    val featuredProducts by viewModel.productRepository.getFeaturedProducts().collectAsState(initial = emptyList())
    val trendingProducts by viewModel.productRepository.getTrendingProducts().collectAsState(initial = emptyList())
    val bestSellers by viewModel.productRepository.getBestSellers().collectAsState(initial = emptyList())
    val newArrivals by viewModel.productRepository.getNewArrivals().collectAsState(initial = emptyList())

    // Flash sale countdown timer state
    var hours by remember { mutableStateOf(4) }
    var minutes by remember { mutableStateOf(32) }
    var seconds by remember { mutableStateOf(45) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            if (seconds > 0) {
                seconds--
            } else {
                seconds = 59
                if (minutes > 0) {
                    minutes--
                } else {
                    minutes = 59
                    if (hours > 0) hours-- else hours = 12
                }
            }
        }
    }

    if (showLocationDialog) {
        LocationSelectorDialog(
            currentLocation = deliveryLocation,
            onSelect = {
                viewModel.updateDeliveryLocation(it)
                showLocationDialog = false
            },
            onDismiss = { showLocationDialog = false }
        )
    }

    Scaffold(
        topBar = {
            Surface(
                color = RoyalBluePrimary,
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    // Top Row: Logo, Location, Notification & Cart Badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Brand Logo & Name
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { /* Refresh */ }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_bazaar_logo),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "BAZAAR",
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                        }

                        // Location Selector Pill
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha = 0.18f),
                            modifier = Modifier
                                .clickable { showLocationDialog = true }
                                .testTag("location_selector_btn")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Place,
                                    contentDescription = null,
                                    tint = VibrantOrangeSecondary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = deliveryLocation,
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.widthIn(max = 120.dp)
                                )
                                Icon(
                                    imageVector = Icons.Filled.ArrowDropDown,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        // Actions: Notifications & Cart
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Notification Icon with Badge
                            IconButton(
                                onClick = onNavigateToNotifications,
                                modifier = Modifier.testTag("home_notifications_btn")
                            ) {
                                BadgedBox(
                                    badge = {
                                        if (notifCount > 0) {
                                            Badge(containerColor = VibrantOrangeSecondary) {
                                                Text(notifCount.toString())
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Notifications,
                                        contentDescription = "Notifications",
                                        tint = Color.White
                                    )
                                }
                            }

                            // Cart Icon with Badge
                            IconButton(
                                onClick = onNavigateToCart,
                                modifier = Modifier.testTag("home_cart_btn")
                            ) {
                                BadgedBox(
                                    badge = {
                                        if (cartCount > 0) {
                                            Badge(containerColor = VibrantOrangeSecondary) {
                                                Text(cartCount.toString())
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.ShoppingCart,
                                        contentDescription = "Cart",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Search Bar Trigger
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clickable { onNavigateToSearch() }
                            .testTag("home_search_bar_trigger"),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Search in 60+ products, Samsung, Nike...",
                                color = Color(0xFF64748B),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Filled.Mic,
                                contentDescription = "Voice Search",
                                tint = VibrantOrangeSecondary
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Promotional Banner Carousel
            item {
                BannerCarousel(onBannerClick = { onNavigateToSearch() })
            }

            // Quick Service Badges (Official Mall, COD, Free Returns)
            item {
                ServiceGuaranteeBar()
            }

            // Categories Shortcut Grid
            item {
                CategoryShortcutsSection(
                    categories = categories,
                    onCategoryClick = onNavigateToCategory
                )
            }

            // Flash Sale Section with Countdown Timer
            item {
                FlashSaleSection(
                    hours = hours,
                    minutes = minutes,
                    seconds = seconds,
                    products = flashSaleProducts,
                    wishlistIds = wishlistIds,
                    onProductClick = { onNavigateToProduct(it.id) },
                    onWishlistToggle = { viewModel.toggleWishlist(it) },
                    onAddToCart = { viewModel.addToCart(it) },
                    onSeeAll = onNavigateToSearch
                )
            }

            // Featured Products Horizontal Carousel
            item {
                ProductHorizontalSection(
                    title = "Featured Products",
                    subtitle = "Handpicked best values for you",
                    badgeText = "TOP PICKS",
                    products = featuredProducts,
                    wishlistIds = wishlistIds,
                    onProductClick = { onNavigateToProduct(it.id) },
                    onWishlistToggle = { viewModel.toggleWishlist(it) },
                    onAddToCart = { viewModel.addToCart(it) },
                    onSeeAll = onNavigateToSearch
                )
            }

            // Popular Brands Showcase
            item {
                PopularBrandsSection(onBrandClick = { brand ->
                    viewModel.filterState.value = viewModel.filterState.value.copy(selectedBrand = brand)
                    onNavigateToSearch()
                })
            }

            // Trending Products
            item {
                ProductHorizontalSection(
                    title = "Trending Now",
                    subtitle = "What Pakistan is buying this week",
                    badgeText = "HOT",
                    products = trendingProducts,
                    wishlistIds = wishlistIds,
                    onProductClick = { onNavigateToProduct(it.id) },
                    onWishlistToggle = { viewModel.toggleWishlist(it) },
                    onAddToCart = { viewModel.addToCart(it) },
                    onSeeAll = onNavigateToSearch
                )
            }

            // New Arrivals
            item {
                ProductHorizontalSection(
                    title = "New Arrivals",
                    subtitle = "Fresh items added today",
                    badgeText = "NEW",
                    products = newArrivals,
                    wishlistIds = wishlistIds,
                    onProductClick = { onNavigateToProduct(it.id) },
                    onWishlistToggle = { viewModel.toggleWishlist(it) },
                    onAddToCart = { viewModel.addToCart(it) },
                    onSeeAll = onNavigateToSearch
                )
            }

            // Best Sellers Grid Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Best Sellers",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Highest rated by verified customers",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Best Sellers 2-Column Grid
            items(bestSellers.chunked(2)) { pair ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProductCard(
                        product = pair[0],
                        isWishlisted = wishlistIds.contains(pair[0].id),
                        onProductClick = { onNavigateToProduct(it.id) },
                        onWishlistToggle = { viewModel.toggleWishlist(it) },
                        onAddToCartClick = { viewModel.addToCart(it) },
                        modifier = Modifier.weight(1f)
                    )

                    if (pair.size > 1) {
                        ProductCard(
                            product = pair[1],
                            isWishlisted = wishlistIds.contains(pair[1].id),
                            onProductClick = { onNavigateToProduct(it.id) },
                            onWishlistToggle = { viewModel.toggleWishlist(it) },
                            onAddToCartClick = { viewModel.addToCart(it) },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            // Bottom Spacing for navigation bar
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun BannerCarousel(onBannerClick: () -> Unit) {
    val banners = listOf(
        R.drawable.banner_mega_sale,
        R.drawable.banner_electronics,
        R.drawable.banner_fashion
    )
    val pagerState = rememberPagerState(pageCount = { banners.size })
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clickable { onBannerClick() }
        ) { page ->
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(banners[page])
                    .crossfade(true)
                    .build(),
                contentDescription = "Promotional Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }

        // Indicator dots
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(banners.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .height(6.dp)
                        .width(if (isSelected) 18.dp else 6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(if (isSelected) VibrantOrangeSecondary else Color.LightGray)
                )
            }
        }
    }
}

@Composable
fun ServiceGuaranteeBar() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFEFF6FF)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ServiceBadgeItem(icon = Icons.Filled.Verified, label = "100% Authentic")
            ServiceBadgeItem(icon = Icons.Filled.LocalShipping, label = "Fast Delivery")
            ServiceBadgeItem(icon = Icons.Filled.Replay, label = "7 Days Return")
            ServiceBadgeItem(icon = Icons.Filled.Payments, label = "COD Available")
        }
    }
}

@Composable
fun ServiceBadgeItem(icon: ImageVector, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = RoyalBluePrimary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = RoyalBluePrimary
        )
    }
}

@Composable
fun CategoryShortcutsSection(
    categories: List<Category>,
    onCategoryClick: (String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Shop by Category",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories) { category ->
                CategoryCircleItem(
                    category = category,
                    onClick = { onCategoryClick(category.id, category.name) }
                )
            }
        }
    }
}

@Composable
fun CategoryCircleItem(
    category: Category,
    onClick: () -> Unit
) {
    val icon = when (category.id) {
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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(68.dp)
            .clickable { onClick() }
    ) {
        Surface(
            modifier = Modifier.size(56.dp),
            shape = CircleShape,
            color = Color(0xFFEFF6FF),
            shadowElevation = 1.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = category.name,
                    tint = RoyalBluePrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = category.name,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun FlashSaleSection(
    hours: Int,
    minutes: Int,
    seconds: Int,
    products: List<Product>,
    wishlistIds: Set<String>,
    onProductClick: (Product) -> Unit,
    onWishlistToggle: (Product) -> Unit,
    onAddToCart: (Product) -> Unit,
    onSeeAll: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .background(Color(0xFFFFF7ED))
            .padding(vertical = 12.dp)
    ) {
        // Header with Countdown Timer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.FlashOn,
                    contentDescription = null,
                    tint = VibrantOrangeSecondary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "FLASH SALE",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = VibrantOrangeDark
                )
                Spacer(modifier = Modifier.width(8.dp))

                // Timer Chips
                TimerBox(text = String.format("%02d", hours))
                Text(text = ":", fontWeight = FontWeight.Bold, color = VibrantOrangeDark)
                TimerBox(text = String.format("%02d", minutes))
                Text(text = ":", fontWeight = FontWeight.Bold, color = VibrantOrangeDark)
                TimerBox(text = String.format("%02d", seconds))
            }

            TextButton(onClick = onSeeAll) {
                Text(
                    text = "See All",
                    color = VibrantOrangeDark,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products) { product ->
                ProductCard(
                    product = product,
                    isWishlisted = wishlistIds.contains(product.id),
                    onProductClick = onProductClick,
                    onWishlistToggle = onWishlistToggle,
                    onAddToCartClick = onAddToCart,
                    modifier = Modifier.width(190.dp)
                )
            }
        }
    }
}

@Composable
fun TimerBox(text: String) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = VibrantOrangeDark,
        modifier = Modifier.padding(horizontal = 2.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun ProductHorizontalSection(
    title: String,
    subtitle: String,
    badgeText: String,
    products: List<Product>,
    wishlistIds: Set<String>,
    onProductClick: (Product) -> Unit,
    onWishlistToggle: (Product) -> Unit,
    onAddToCart: (Product) -> Unit,
    onSeeAll: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = RoyalBluePrimary.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = badgeText,
                            style = MaterialTheme.typography.labelSmall,
                            color = RoyalBluePrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            TextButton(onClick = onSeeAll) {
                Text(
                    text = "View All",
                    color = RoyalBluePrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products) { product ->
                ProductCard(
                    product = product,
                    isWishlisted = wishlistIds.contains(product.id),
                    onProductClick = onProductClick,
                    onWishlistToggle = onWishlistToggle,
                    onAddToCartClick = onAddToCart,
                    modifier = Modifier.width(190.dp)
                )
            }
        }
    }
}

@Composable
fun PopularBrandsSection(onBrandClick: (String) -> Unit) {
    val brands = listOf("Samsung", "Apple", "Nike", "Khaadi", "Sony", "Dell", "Sapphire", "Anker")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = "Official Brand Stores",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(brands) { brand ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 2.dp,
                    modifier = Modifier.clickable { onBrandClick(brand) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = RoyalBluePrimary.copy(alpha = 0.1f),
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = brand.take(1),
                                    fontWeight = FontWeight.Bold,
                                    color = RoyalBluePrimary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = brand,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LocationSelectorDialog(
    currentLocation: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val locations = listOf(
        "Gulberg III, Lahore",
        "DHA Phase 5, Lahore",
        "Clifton, Karachi",
        "DHA Phase 6, Karachi",
        "Sector F-7, Islamabad",
        "Bahria Town, Rawalpindi",
        "University Town, Peshawar",
        "Cantt, Multan",
        "Kohinoor City, Faisalabad"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Select Delivery Location",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(modifier = Modifier.heightIn(max = 320.dp)) {
                items(locations) { loc ->
                    val isSelected = loc == currentLocation
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(loc) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { onSelect(loc) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = loc,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) RoyalBluePrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
