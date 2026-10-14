package com.example.feature.product

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.core.designsystem.ProductCard
import com.example.core.designsystem.ProductImage
import com.example.core.designsystem.StarRatingBar
import com.example.core.utils.Formatters
import com.example.domain.model.Product
import com.example.domain.model.Review
import com.example.feature.reviews.WriteReviewDialog
import com.example.ui.theme.*
import com.example.ui.viewmodel.BazaarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    viewModel: BazaarViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToCheckout: () -> Unit,
    onProductClick: (String) -> Unit
) {
    val context = LocalContext.current
    val productFlow = remember(productId) { viewModel.productRepository.getProductById(productId) }
    val product by productFlow.collectAsState(initial = null)
    val wishlistIds by viewModel.wishlistIds.collectAsState()
    val reviews by viewModel.reviewRepository.getReviewsForProduct(productId).collectAsState(initial = emptyList())
    val allProducts by viewModel.productRepository.getAllProducts().collectAsState(initial = emptyList())

    val isWishlisted = product?.let { wishlistIds.contains(it.id) } ?: false

    var selectedColor by remember(product) { mutableStateOf(product?.colors?.firstOrNull()) }
    var selectedSize by remember(product) { mutableStateOf(product?.sizes?.firstOrNull()) }
    var quantity by remember { mutableStateOf(1) }
    var selectedTab by remember { mutableStateOf(0) } // 0: Description, 1: Specifications, 2: Delivery & Return, 3: Reviews
    var showZoomDialog by remember { mutableStateOf(false) }
    var showWriteReviewDialog by remember { mutableStateOf(false) }

    if (product == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = RoyalBluePrimary)
        }
        return
    }

    val currentProduct = product!!
    val relatedProducts = remember(currentProduct, allProducts) {
        allProducts.filter { it.categoryId == currentProduct.categoryId && it.id != currentProduct.id }.take(6)
    }

    if (showZoomDialog) {
        Dialog(onDismissRequest = { showZoomDialog = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = { showZoomDialog = false }) {
                            Icon(Icons.Filled.Close, contentDescription = "Close")
                        }
                    }
                    ProductImage(
                        imageName = currentProduct.imageUrls.firstOrNull() ?: "banner_mega_sale",
                        contentDescription = currentProduct.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = currentProduct.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

    if (showWriteReviewDialog) {
        WriteReviewDialog(
            productId = currentProduct.id,
            onReviewSubmit = { review ->
                viewModel.addReview(review)
                showWriteReviewDialog = false
            },
            onDismiss = { showWriteReviewDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Share button using Android Sharesheet
                    IconButton(onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "Check out '${currentProduct.name}' on Bazaar for ${Formatters.formatPkr(currentProduct.price)}! Download Bazaar App.")
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "Share Product via"))
                    }) {
                        Icon(Icons.Outlined.Share, contentDescription = "Share")
                    }

                    // Cart icon
                    IconButton(onClick = onNavigateToCart) {
                        Icon(Icons.Outlined.ShoppingCart, contentDescription = "Cart")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            // Sticky Purchase Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 16.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Wishlist Icon Button
                    IconButton(
                        onClick = { viewModel.toggleWishlist(currentProduct) },
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
                            .testTag("detail_wishlist_btn")
                    ) {
                        Icon(
                            imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Wishlist",
                            tint = if (isWishlisted) ErrorRed else Color.Gray
                        )
                    }

                    // Add to Cart Button
                    OutlinedButton(
                        onClick = {
                            viewModel.addToCart(currentProduct, quantity, selectedColor, selectedSize)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("detail_add_to_cart_btn"),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.5.dp, RoyalBluePrimary)
                    ) {
                        Icon(Icons.Filled.AddShoppingCart, contentDescription = null, tint = RoyalBluePrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add to Cart", color = RoyalBluePrimary, fontWeight = FontWeight.Bold)
                    }

                    // Buy Now Button
                    Button(
                        onClick = {
                            viewModel.addToCart(currentProduct, quantity, selectedColor, selectedSize)
                            onNavigateToCheckout()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("detail_buy_now_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VibrantOrangeSecondary)
                    ) {
                        Text("Buy Now", fontWeight = FontWeight.Bold, color = Color.White)
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
            // Product Images Gallery Carousel
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .background(Color(0xFFF8FAFC))
                ) {
                    ProductImage(
                        imageName = currentProduct.imageUrls.firstOrNull() ?: "banner_mega_sale",
                        contentDescription = currentProduct.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { showZoomDialog = true }
                    )

                    // Zoom icon hint
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp),
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.6f)
                    ) {
                        IconButton(
                            onClick = { showZoomDialog = true },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Filled.ZoomIn, contentDescription = "Zoom", tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }

                    // Discount badge
                    if (currentProduct.discountPercent > 0) {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(16.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = VibrantOrangeSecondary
                        ) {
                            Text(
                                text = "${currentProduct.discountPercent}% OFF",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Product Details Core Card
            item {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = currentProduct.brand.uppercase(),
                                style = MaterialTheme.typography.labelMedium,
                                color = RoyalBluePrimary,
                                fontWeight = FontWeight.Bold
                            )

                            // Stock status
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = if (currentProduct.stock > 0) SuccessGreen.copy(alpha = 0.12f) else ErrorRed.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = if (currentProduct.stock > 0) "In Stock (${currentProduct.stock} units left)" else "Out of Stock",
                                    color = if (currentProduct.stock > 0) SuccessGreen else ErrorRed,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = currentProduct.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Rating & Reviews Row
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            StarRatingBar(rating = currentProduct.rating, starSize = 18.dp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${currentProduct.rating}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "(${currentProduct.reviewCount} customer ratings)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Price and Savings
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = Formatters.formatPkr(currentProduct.price),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = RoyalBluePrimary
                            )
                            if (currentProduct.originalPrice > currentProduct.price) {
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = Formatters.formatPkr(currentProduct.originalPrice),
                                    style = MaterialTheme.typography.titleMedium,
                                    textDecoration = TextDecoration.LineThrough,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Variant Selectors (Colors & Sizes) & Quantity
            item {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Colors
                        if (currentProduct.colors.isNotEmpty()) {
                            Text(
                                text = "Select Color: ${selectedColor ?: ""}",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(currentProduct.colors) { color ->
                                    val isSelected = color == selectedColor
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedColor = color },
                                        label = { Text(color) }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Sizes / Storage
                        if (currentProduct.sizes.isNotEmpty()) {
                            Text(
                                text = "Select Size / Configuration: ${selectedSize ?: ""}",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(currentProduct.sizes) { size ->
                                    val isSelected = size == selectedSize
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedSize = size },
                                        label = { Text(size) }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Quantity Selector
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Quantity",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                IconButton(
                                    onClick = { if (quantity > 1) quantity-- },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Filled.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                                }
                                Text(
                                    text = quantity.toString(),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )
                                IconButton(
                                    onClick = { if (quantity < currentProduct.stock) quantity++ },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Filled.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }

            // Tabbed Information Section (Description, Specs, Delivery, Reviews)
            item {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Column {
                        TabRow(
                            selectedTabIndex = selectedTab,
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = RoyalBluePrimary
                        ) {
                            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Details", fontSize = 12.sp) })
                            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Specs", fontSize = 12.sp) })
                            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Delivery", fontSize = 12.sp) })
                            Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }, text = { Text("Reviews", fontSize = 12.sp) })
                        }

                        Box(modifier = Modifier.padding(16.dp)) {
                            when (selectedTab) {
                                0 -> {
                                    Column {
                                        Text(
                                            text = currentProduct.description,
                                            style = MaterialTheme.typography.bodyMedium,
                                            lineHeight = 22.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                                1 -> {
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        if (currentProduct.specifications.isEmpty()) {
                                            Text("Official standard specifications apply.")
                                        } else {
                                            currentProduct.specifications.forEach { (key, value) ->
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .background(Color(0xFFF8FAFC), RoundedCornerShape(6.dp))
                                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(text = key, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                                    Text(text = value, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.bodySmall)
                                                }
                                            }
                                        }
                                    }
                                }
                                2 -> {
                                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Filled.LocalShipping, contentDescription = null, tint = RoyalBluePrimary)
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text("Estimated Delivery: 2-3 business days", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                                                Text("Shipping by TCS Express / Trax nationwide.", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                            }
                                        }
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Filled.Payments, contentDescription = null, tint = SuccessGreen)
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text("Cash on Delivery (COD) Available", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                                                Text("Pay cash to courier upon inspecting sealed box.", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                            }
                                        }
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Filled.VerifiedUser, contentDescription = null, tint = VibrantOrangeSecondary)
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text("7-Day Return Policy & Warranty", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                                                Text("100% money-back guarantee for damaged or defective items.", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                            }
                                        }
                                    }
                                }
                                3 -> {
                                    Column {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Customer Reviews (${reviews.size})",
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                            Button(
                                                onClick = { showWriteReviewDialog = true },
                                                shape = RoundedCornerShape(8.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = RoyalBluePrimary),
                                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                            ) {
                                                Text("Write Review", fontSize = 12.sp)
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))

                                        reviews.forEach { rev ->
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = Color(0xFFF8FAFC),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 4.dp)
                                            ) {
                                                Column(modifier = Modifier.padding(10.dp)) {
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(rev.userName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                                                        Text(rev.date, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                                    }
                                                    StarRatingBar(rating = rev.rating.toFloat(), starSize = 12.dp)
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(rev.title, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodySmall)
                                                    Text(rev.comment, style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
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

            // Related Products
            item {
                if (relatedProducts.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        Text(
                            text = "Related Products",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(relatedProducts) { related ->
                                ProductCard(
                                    product = related,
                                    isWishlisted = wishlistIds.contains(related.id),
                                    onProductClick = { onProductClick(related.id) },
                                    onWishlistToggle = { viewModel.toggleWishlist(related) },
                                    onAddToCartClick = { viewModel.addToCart(related) },
                                    modifier = Modifier.width(180.dp)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
