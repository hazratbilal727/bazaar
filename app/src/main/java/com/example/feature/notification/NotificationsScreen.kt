package com.example.feature.notification

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.EmptyStateView
import com.example.domain.model.NotificationItem
import com.example.domain.model.NotificationType
import com.example.ui.theme.*
import com.example.ui.viewmodel.BazaarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    viewModel: BazaarViewModel,
    onNavigateBack: () -> Unit
) {
    val notifications by viewModel.notifications.collectAsState()
    var selectedFilter by remember { mutableStateOf(0) } // 0: All, 1: Orders, 2: Offers

    val filteredNotifications = remember(notifications, selectedFilter) {
        when (selectedFilter) {
            1 -> notifications.filter { it.type == NotificationType.ORDER_UPDATE }
            2 -> notifications.filter { it.type == NotificationType.FLASH_SALE || it.type == NotificationType.PRICE_DROP || it.type == NotificationType.PROMO_COUPON }
            else -> notifications
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (notifications.any { !it.isRead }) {
                        TextButton(onClick = { viewModel.markAllNotificationsAsRead() }) {
                            Text("Mark All Read", color = RoyalBluePrimary, fontWeight = FontWeight.Bold)
                        }
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
            // Filter Tabs
            TabRow(
                selectedTabIndex = selectedFilter,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = RoyalBluePrimary
            ) {
                listOf("All", "Orders", "Offers & Deals").forEachIndexed { index, title ->
                    Tab(
                        selected = selectedFilter == index,
                        onClick = { selectedFilter = index },
                        text = { Text(title, fontWeight = FontWeight.SemiBold) }
                    )
                }
            }

            if (filteredNotifications.isEmpty()) {
                EmptyStateView(
                    icon = Icons.Outlined.NotificationsNone,
                    title = "No Notifications",
                    description = "You're all caught up! When you place orders or when flash sales launch, you'll see alerts here."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredNotifications, key = { it.id }) { notif ->
                        NotificationCard(
                            item = notif,
                            onClick = { viewModel.markNotificationAsRead(notif.id) },
                            onDelete = { viewModel.deleteNotification(notif.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationCard(
    item: NotificationItem,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val (icon, tint) = when (item.type) {
        NotificationType.ORDER_UPDATE -> Icons.Filled.LocalShipping to RoyalBluePrimary
        NotificationType.FLASH_SALE -> Icons.Filled.FlashOn to VibrantOrangeSecondary
        NotificationType.PRICE_DROP -> Icons.Filled.TrendingDown to SuccessGreen
        NotificationType.PROMO_COUPON -> Icons.Filled.CardGiftcard to Color(0xFF8B5CF6)
        else -> Icons.Filled.Notifications to RoyalBluePrimary
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (item.isRead) MaterialTheme.colorScheme.surface else Color(0xFFEFF6FF),
        shadowElevation = if (item.isRead) 1.dp else 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = CircleShape,
                color = tint.copy(alpha = 0.15f),
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(22.dp))
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        fontWeight = if (item.isRead) FontWeight.SemiBold else FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = "Delete", tint = Color.Gray, modifier = Modifier.size(14.dp))
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}
