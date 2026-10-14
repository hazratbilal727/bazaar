package com.example.feature.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.User
import com.example.ui.theme.*
import com.example.ui.viewmodel.BazaarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: BazaarViewModel,
    onNavigateToOrders: () -> Unit,
    onNavigateToWishlist: () -> Unit,
    onNavigateToAddresses: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val user by viewModel.currentUser.collectAsState()
    val cartCount by viewModel.cartItemCount.collectAsState()
    val wishlistCount by viewModel.wishlistIds.collectAsState()
    val orders by viewModel.orders.collectAsState()

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var pushNotificationsEnabled by remember { mutableStateOf(true) }
    var selectedLanguage by remember { mutableStateOf("English (PK)") }

    if (showEditProfileDialog) {
        EditProfileDialog(
            user = user,
            onSave = { name, email, phone ->
                viewModel.updateProfile(name, email, phone)
                showEditProfileDialog = false
            },
            onDismiss = { showEditProfileDialog = false }
        )
    }

    if (showHelpDialog) {
        HelpSupportDialog(onDismiss = { showHelpDialog = false })
    }

    if (showTermsDialog) {
        TermsPrivacyDialog(onDismiss = { showTermsDialog = false })
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log Out from Bazaar?", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to log out? Your cart and wishlist will remain saved.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.logout()
                        showLogoutDialog = false
                        onNavigateToLogin()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Log Out")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Account", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { showEditProfileDialog = true }) {
                        Icon(Icons.Outlined.Edit, contentDescription = "Edit Profile")
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // User Avatar Card
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(68.dp),
                            shape = CircleShape,
                            color = RoyalBluePrimary
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = (user?.name?.take(1) ?: "B").uppercase(),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = user?.name ?: "Guest Shopper",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = user?.email ?: "guest@bazaar.pk",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = user?.phoneNumber ?: "+92 300 1234567",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = VibrantOrangeSecondary.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = if (user?.isGuest == true) "GUEST" else "VIP CLUB",
                                color = VibrantOrangeSecondary,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Quick Stats Row (Orders, Wishlist, Cart)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickStatCard(
                        count = orders.size.toString(),
                        label = "Orders",
                        icon = Icons.Outlined.ShoppingBag,
                        onClick = onNavigateToOrders,
                        modifier = Modifier.weight(1f)
                    )
                    QuickStatCard(
                        count = wishlistCount.size.toString(),
                        label = "Wishlist",
                        icon = Icons.Outlined.FavoriteBorder,
                        onClick = onNavigateToWishlist,
                        modifier = Modifier.weight(1f)
                    )
                    QuickStatCard(
                        count = cartCount.toString(),
                        label = "In Cart",
                        icon = Icons.Outlined.ShoppingCart,
                        onClick = onNavigateToOrders, // or cart
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Account & Preferences Menu Section
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        ProfileMenuItem(
                            icon = Icons.Outlined.Place,
                            title = "Saved Delivery Addresses",
                            subtitle = "Manage home, work & family addresses",
                            onClick = onNavigateToAddresses
                        )
                        Divider(modifier = Modifier.padding(horizontal = 16.dp))
                        ProfileMenuItem(
                            icon = Icons.Outlined.Notifications,
                            title = "Notifications & Alerts",
                            subtitle = "Order dispatches, flash sales & promos",
                            onClick = onNavigateToNotifications
                        )
                        Divider(modifier = Modifier.padding(horizontal = 16.dp))
                        ProfileMenuItem(
                            icon = Icons.Outlined.ConfirmationNumber,
                            title = "My Vouchers & Coupons",
                            subtitle = "BAZAAR10, WELCOME500 active",
                            onClick = { viewModel.showSnackbar("You have 2 active discount vouchers!") }
                        )
                    }
                }
            }

            // Settings & Preferences
            item {
                Text(
                    text = "App Settings",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Column {
                        // Push notifications switch
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.NotificationsActive, contentDescription = null, tint = RoyalBluePrimary)
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text("Push Notifications", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                                    Text("Alert me on flash sales and delivery", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                }
                            }
                            Switch(
                                checked = pushNotificationsEnabled,
                                onCheckedChange = { pushNotificationsEnabled = it }
                            )
                        }

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        // Language Toggle
                        ProfileMenuItem(
                            icon = Icons.Outlined.Language,
                            title = "Language",
                            subtitle = selectedLanguage,
                            onClick = {
                                selectedLanguage = if (selectedLanguage.startsWith("English")) "Urdu (اردو)" else "English (PK)"
                                viewModel.showSnackbar("Language set to $selectedLanguage")
                            }
                        )

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        // Help Center
                        ProfileMenuItem(
                            icon = Icons.Outlined.SupportAgent,
                            title = "24/7 Customer Help Center",
                            subtitle = "Chat support, WhatsApp, FAQs",
                            onClick = { showHelpDialog = true }
                        )

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        // Privacy & Terms
                        ProfileMenuItem(
                            icon = Icons.Outlined.Policy,
                            title = "Privacy Policy & Terms",
                            subtitle = "Buyer protection and data policy",
                            onClick = { showTermsDialog = true }
                        )
                    }
                }
            }

            // Log Out Button
            item {
                OutlinedButton(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("logout_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                    border = BorderStroke(1.dp, ErrorRed)
                ) {
                    Icon(Icons.Filled.Logout, contentDescription = null, tint = ErrorRed)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Log Out", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun QuickStatCard(
    count: String,
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = RoyalBluePrimary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = count, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = RoyalBluePrimary, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
            Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color.LightGray)
    }
}

@Composable
fun EditProfileDialog(
    user: User?,
    onSave: (String, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(user?.name ?: "Bilal Khan") }
    var email by remember { mutableStateOf(user?.email ?: "hazratbilalvlog@gmail.com") }
    var phone by remember { mutableStateOf(user?.phoneNumber ?: "+92 300 1234567") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile Details", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") })
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email Address") })
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") })
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(name, email, phone) },
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBluePrimary)
            ) {
                Text("Save Changes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun HelpSupportDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Bazaar 24/7 Support", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Our customer care champions are ready to assist you:")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Phone, contentDescription = null, tint = RoyalBluePrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("UAN Hotline: 042-111-BAZAAR (229227)", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodySmall)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Email, contentDescription = null, tint = RoyalBluePrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("support@bazaar.pk", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodySmall)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Chat, contentDescription = null, tint = SuccessGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("WhatsApp Priority: +92 300 9998877", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = RoyalBluePrimary)) {
                Text("Got It")
            }
        }
    )
}

@Composable
fun TermsPrivacyDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Terms & Buyer Protection", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("1. 100% Genuine Guaranteed: All Mall products are sourced from authorized Pakistani distributors.", style = MaterialTheme.typography.bodySmall)
                Text("2. 7-Day Hassle-Free Returns: Full refunds processed within 3 days of return verification.", style = MaterialTheme.typography.bodySmall)
                Text("3. Safe Payment: Cash on Delivery riders provide computer generated receipts.", style = MaterialTheme.typography.bodySmall)
                Text("4. Data Privacy: Your personal phone and address are strictly protected under end-to-end security.", style = MaterialTheme.typography.bodySmall)
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = RoyalBluePrimary)) {
                Text("Understood")
            }
        }
    )
}
