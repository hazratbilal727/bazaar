package com.example.feature.orders

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.core.designsystem.ProductImage
import com.example.core.utils.Formatters
import com.example.domain.model.Order
import com.example.domain.model.OrderStatus
import com.example.domain.model.TrackingEvent
import com.example.ui.theme.*
import com.example.ui.viewmodel.BazaarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    viewModel: BazaarViewModel,
    onNavigateBack: () -> Unit,
    onOrderClick: (String) -> Unit
) {
    val orders by viewModel.orders.collectAsState()
    var selectedFilterTab by remember { mutableStateOf(0) } // 0: All, 1: Active, 2: Delivered, 3: Cancelled

    val filteredOrders = remember(orders, selectedFilterTab) {
        when (selectedFilterTab) {
            1 -> orders.filter { it.status == OrderStatus.CONFIRMED || it.status == OrderStatus.PROCESSING || it.status == OrderStatus.SHIPPED || it.status == OrderStatus.OUT_FOR_DELIVERY }
            2 -> orders.filter { it.status == OrderStatus.DELIVERED }
            3 -> orders.filter { it.status == OrderStatus.CANCELLED || it.status == OrderStatus.RETURNED }
            else -> orders
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Orders",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
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
            // TabRow filter
            TabRow(
                selectedTabIndex = selectedFilterTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = RoyalBluePrimary
            ) {
                listOf("All", "Active", "Delivered", "Cancelled").forEachIndexed { index, title ->
                    Tab(
                        selected = selectedFilterTab == index,
                        onClick = { selectedFilterTab = index },
                        text = { Text(title, fontWeight = FontWeight.SemiBold) }
                    )
                }
            }

            if (filteredOrders.isEmpty()) {
                EmptyStateView(
                    icon = Icons.Outlined.ReceiptLong,
                    title = "No Orders Found",
                    description = "You don't have any orders matching this category."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredOrders, key = { it.id }) { order ->
                        OrderCardItem(
                            order = order,
                            onClick = { onOrderClick(order.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCardItem(
    order: Order,
    onClick: () -> Unit
) {
    val statusColor = when (order.status) {
        OrderStatus.DELIVERED -> SuccessGreen
        OrderStatus.CANCELLED, OrderStatus.RETURNED -> ErrorRed
        OrderStatus.SHIPPED, OrderStatus.OUT_FOR_DELIVERY -> VibrantOrangeSecondary
        else -> RoyalBluePrimary
    }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Order # & Status Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = order.orderNumber,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = statusColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = order.status.title,
                        color = statusColor,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = "Placed on ${order.orderDate}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Divider(modifier = Modifier.padding(vertical = 10.dp))

            // Items Preview Row
            order.items.take(2).forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProductImage(
                        imageName = item.product.imageUrls.firstOrNull() ?: "banner_mega_sale",
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.product.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1
                        )
                        Text(
                            text = "Qty: ${item.quantity} • ${Formatters.formatPkr(item.totalPrice)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                }
            }

            if (order.items.size > 2) {
                Text(
                    text = "+ ${order.items.size - 2} more items",
                    style = MaterialTheme.typography.labelSmall,
                    color = RoyalBluePrimary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Divider(modifier = Modifier.padding(vertical = 10.dp))

            // Footer: Total & View Details Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total Amount",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                    Text(
                        text = Formatters.formatPkr(order.totalAmount),
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.titleMedium,
                        color = RoyalBluePrimary
                    )
                }

                OutlinedButton(
                    onClick = onClick,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text("Track Order", fontWeight = FontWeight.Bold, color = RoyalBluePrimary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp), tint = RoyalBluePrimary)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    orderId: String,
    viewModel: BazaarViewModel,
    onNavigateBack: () -> Unit,
    onTrackOrder: (String) -> Unit
) {
    val orderFlow = remember(orderId) { viewModel.orderRepository.getOrderById(orderId) }
    val order by orderFlow.collectAsState(initial = null)

    var showCancelDialog by remember { mutableStateOf(false) }
    var showReturnDialog by remember { mutableStateOf(false) }
    var cancelReason by remember { mutableStateOf("Changed my mind") }
    var returnReason by remember { mutableStateOf("Defective / Wrong Item") }

    if (order == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = RoyalBluePrimary)
        }
        return
    }

    val currentOrder = order!!

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text("Cancel Order?", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Select reason for cancellation:")
                    Spacer(modifier = Modifier.height(8.dp))
                    listOf("Changed my mind", "Found better price elsewhere", "Delivery time too long", "Ordered by mistake").forEach { r ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { cancelReason = r }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(selected = cancelReason == r, onClick = { cancelReason = r })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(r, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.cancelOrder(currentOrder.id, cancelReason)
                        showCancelDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) {
                    Text("Confirm Cancellation")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) { Text("Keep Order") }
            }
        )
    }

    if (showReturnDialog) {
        AlertDialog(
            onDismissRequest = { showReturnDialog = false },
            title = { Text("Request 7-Day Return", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Under Bazaar Guarantee, items can be returned within 7 days.")
                    Spacer(modifier = Modifier.height(8.dp))
                    listOf("Defective / Not working", "Item damaged in transit", "Size does not fit", "Missing accessories").forEach { r ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { returnReason = r }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(selected = returnReason == r, onClick = { returnReason = r })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(r, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.requestReturn(currentOrder.id, returnReason)
                        showReturnDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VibrantOrangeSecondary)
                ) {
                    Text("Submit Return Request")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReturnDialog = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Order Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Status Banner
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(currentOrder.orderNumber, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                                Text("Placed: ${currentOrder.orderDate}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                            Button(
                                onClick = { onTrackOrder(currentOrder.id) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = RoyalBluePrimary),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(Icons.Filled.Timeline, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Track Timeline", fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Status: ${currentOrder.status.title}",
                            fontWeight = FontWeight.Bold,
                            color = RoyalBluePrimary
                        )
                        Text(
                            text = "Estimated Delivery: ${currentOrder.estimatedDeliveryDate}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }

            // Ordered Items Card
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 1.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Items Ordered (${currentOrder.items.size})", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))

                        currentOrder.items.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ProductImage(
                                    imageName = item.product.imageUrls.firstOrNull() ?: "banner_mega_sale",
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(item.product.name, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
                                    Text(
                                        text = listOfNotNull(item.selectedColor, item.selectedSize).joinToString(" | "),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = "${item.quantity} x ${Formatters.formatPkr(item.product.price)}",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = RoyalBluePrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Shipping & Payment
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 1.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Delivery Information", fontWeight = FontWeight.Bold)
                        Text(currentOrder.deliveryAddress.formattedAddress, style = MaterialTheme.typography.bodySmall)
                        Text("Recipient: ${currentOrder.deliveryAddress.recipientName} (${currentOrder.deliveryAddress.phoneNumber})", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        Divider(modifier = Modifier.padding(vertical = 4.dp))
                        Text("Payment Method", fontWeight = FontWeight.Bold)
                        Text(currentOrder.paymentMethod, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            // Cost Summary
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 1.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Payment Summary", fontWeight = FontWeight.Bold)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal", style = MaterialTheme.typography.bodySmall)
                            Text(Formatters.formatPkr(currentOrder.subtotal), style = MaterialTheme.typography.bodySmall)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Delivery Fee", style = MaterialTheme.typography.bodySmall)
                            Text(if (currentOrder.deliveryFee == 0.0) "FREE" else Formatters.formatPkr(currentOrder.deliveryFee), style = MaterialTheme.typography.bodySmall)
                        }
                        if (currentOrder.discountAmount > 0.0) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Discount", style = MaterialTheme.typography.bodySmall, color = SuccessGreen)
                                Text("-${Formatters.formatPkr(currentOrder.discountAmount)}", style = MaterialTheme.typography.bodySmall, color = SuccessGreen)
                            }
                        }
                        Divider(modifier = Modifier.padding(vertical = 4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Paid / Due", fontWeight = FontWeight.Bold)
                            Text(Formatters.formatPkr(currentOrder.totalAmount), fontWeight = FontWeight.Bold, color = RoyalBluePrimary, fontSize = 18.sp)
                        }
                    }
                }
            }

            // Action Buttons (Reorder, Cancel, Return)
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Reorder
                    Button(
                        onClick = {
                            currentOrder.items.forEach { item ->
                                viewModel.addToCart(item.product, item.quantity, item.selectedColor, item.selectedSize)
                            }
                            viewModel.showSnackbar("Items added back to Cart!")
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBluePrimary)
                    ) {
                        Icon(Icons.Filled.Replay, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Reorder Items")
                    }

                    // Cancel or Return
                    if (currentOrder.status == OrderStatus.PENDING || currentOrder.status == OrderStatus.CONFIRMED) {
                        OutlinedButton(
                            onClick = { showCancelDialog = true },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed)
                        ) {
                            Text("Cancel Order")
                        }
                    } else if (currentOrder.status == OrderStatus.DELIVERED) {
                        OutlinedButton(
                            onClick = { showReturnDialog = true },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = VibrantOrangeSecondary)
                        ) {
                            Text("Return Item")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingScreen(
    orderId: String,
    viewModel: BazaarViewModel,
    onNavigateBack: () -> Unit
) {
    val orderFlow = remember(orderId) { viewModel.orderRepository.getOrderById(orderId) }
    val order by orderFlow.collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Live Order Tracking", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        if (order == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = RoyalBluePrimary)
            }
            return@Scaffold
        }

        val currentOrder = order!!
        val events = currentOrder.trackingEvents

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Tracking #${currentOrder.orderNumber}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text("Carrier: TCS Express Logistics Nationwide", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Estimated Arrival: ${currentOrder.estimatedDeliveryDate}",
                            fontWeight = FontWeight.Bold,
                            color = RoyalBluePrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            items(events) { event ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    // Timeline step circle and line
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(36.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (event.isCompleted) SuccessGreen else Color(0xFFE2E8F0),
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (event.isCompleted) {
                                    Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(48.dp)
                                .background(if (event.isCompleted) SuccessGreen else Color(0xFFE2E8F0))
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = event.title,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (event.isCompleted) MaterialTheme.colorScheme.onSurface else Color.Gray
                        )
                        Text(
                            text = event.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = event.timestamp,
                            style = MaterialTheme.typography.labelSmall,
                            color = RoyalBluePrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
