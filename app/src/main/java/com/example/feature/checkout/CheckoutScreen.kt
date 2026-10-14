package com.example.feature.checkout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.core.designsystem.ProductImage
import com.example.core.utils.Formatters
import com.example.domain.model.Address
import com.example.domain.model.CartItem
import com.example.ui.theme.*
import com.example.ui.viewmodel.BazaarViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    viewModel: BazaarViewModel,
    onNavigateBack: () -> Unit,
    onOrderPlaced: (String) -> Unit
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val addresses by viewModel.addresses.collectAsState()
    val appliedCoupon by viewModel.appliedCoupon.collectAsState()
    val couponDiscount by viewModel.couponDiscount.collectAsState()

    val selectedItems = remember(cartItems) { cartItems.filter { it.isSelected } }
    val scope = rememberCoroutineScope()

    var currentStep by remember { mutableStateOf(1) } // 1: Address, 2: Delivery, 3: Payment, 4: Review
    var selectedAddressId by remember(addresses) {
        mutableStateOf(addresses.firstOrNull { it.isDefault }?.id ?: addresses.firstOrNull()?.id ?: "")
    }
    val currentSelectedAddress = addresses.find { it.id == selectedAddressId } ?: addresses.firstOrNull()

    var showAddAddressDialog by remember { mutableStateOf(false) }

    // Delivery method
    var selectedDeliveryMethod by remember { mutableStateOf("STANDARD") } // "STANDARD" (150) or "EXPRESS" (350)

    // Payment method
    var selectedPaymentMethod by remember { mutableStateOf("COD") } // "COD", "CARD", "JAZZCASH", "EASYPAISA"
    var cardNumber by remember { mutableStateOf("4819 5412 8931 7710") }
    var cardExpiry by remember { mutableStateOf("09/28") }
    var cardCvv by remember { mutableStateOf("321") }
    var walletPhone by remember { mutableStateOf("+92 300 1234567") }

    val subtotal = remember(selectedItems) { selectedItems.sumOf { it.totalPrice } }
    val standardDeliveryFee = if (subtotal > 2000.0 || appliedCoupon?.contains("FREESHIP") == true) 0.0 else 150.0
    val deliveryFee = if (selectedDeliveryMethod == "EXPRESS") 350.0 else standardDeliveryFee
    val discountAmount = when {
        couponDiscount > 0.0 && couponDiscount <= 1.0 -> subtotal * couponDiscount
        else -> couponDiscount
    }
    val finalTotal = (subtotal + deliveryFee - discountAmount).coerceAtLeast(0.0)

    var isSubmitting by remember { mutableStateOf(false) }

    if (showAddAddressDialog) {
        AddAddressDialog(
            onSave = { newAddr ->
                viewModel.addAddress(newAddr)
                selectedAddressId = newAddr.id
                showAddAddressDialog = false
            },
            onDismiss = { showAddAddressDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Checkout",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Step $currentStep of 4",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentStep > 1) currentStep-- else onNavigateBack()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 16.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total Payable:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = Formatters.formatPkr(finalTotal),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = RoyalBluePrimary
                        )
                    }

                    Button(
                        onClick = {
                            if (currentStep < 4) {
                                currentStep++
                            } else {
                                // Place Order
                                if (currentSelectedAddress != null) {
                                    isSubmitting = true
                                    scope.launch {
                                        val paymentDesc = when (selectedPaymentMethod) {
                                            "COD" -> "Cash on Delivery"
                                            "CARD" -> "Debit/Credit Card (ending 7710)"
                                            "JAZZCASH" -> "JazzCash Wallet ($walletPhone)"
                                            else -> "Easypaisa Wallet ($walletPhone)"
                                        }
                                        val order = viewModel.placeOrder(
                                            items = selectedItems,
                                            deliveryAddress = currentSelectedAddress,
                                            paymentMethod = paymentDesc,
                                            deliveryFee = deliveryFee
                                        )
                                        isSubmitting = false
                                        onOrderPlaced(order.id)
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .height(50.dp)
                            .widthIn(min = 180.dp)
                            .testTag("checkout_action_btn"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (currentStep == 4) VibrantOrangeSecondary else RoyalBluePrimary
                        ),
                        enabled = !isSubmitting && currentSelectedAddress != null
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                        } else {
                            Text(
                                text = if (currentStep == 4) "Place Order" else "Continue",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
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
            // Checkout Step Indicator Header
            StepIndicator(currentStep = currentStep)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                when (currentStep) {
                    1 -> {
                        // STEP 1: DELIVERY ADDRESS
                        Text(
                            text = "Select Delivery Address",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        addresses.forEach { addr ->
                            val isSelected = addr.id == selectedAddressId
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surface,
                                shadowElevation = if (isSelected) 3.dp else 1.dp,
                                border = if (isSelected) BorderStroke(2.dp, RoyalBluePrimary) else null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clickable { selectedAddressId = addr.id }
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { selectedAddressId = addr.id }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = addr.recipientName,
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = Color(0xFFEFF6FF)
                                            ) {
                                                Text(
                                                    text = addr.addressLabel,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = RoyalBluePrimary,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = addr.formattedAddress,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Phone: ${addr.phoneNumber}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = { showAddAddressDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("add_new_address_btn"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = null, tint = RoyalBluePrimary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Add New Delivery Address", color = RoyalBluePrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                    2 -> {
                        // STEP 2: DELIVERY METHOD
                        Text(
                            text = "Choose Delivery Method",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Standard Delivery
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = if (selectedDeliveryMethod == "STANDARD") BorderStroke(2.dp, RoyalBluePrimary) else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedDeliveryMethod = "STANDARD" }
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedDeliveryMethod == "STANDARD",
                                    onClick = { selectedDeliveryMethod = "STANDARD" }
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Standard Courier Delivery", fontWeight = FontWeight.Bold)
                                    Text("Delivery within 2-3 business days via TCS / Trax", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                }
                                Text(
                                    text = if (standardDeliveryFee == 0.0) "FREE" else "Rs. 150",
                                    fontWeight = FontWeight.Bold,
                                    color = if (standardDeliveryFee == 0.0) SuccessGreen else RoyalBluePrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Express Delivery
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = if (selectedDeliveryMethod == "EXPRESS") BorderStroke(2.dp, RoyalBluePrimary) else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedDeliveryMethod = "EXPRESS" }
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedDeliveryMethod == "EXPRESS",
                                    onClick = { selectedDeliveryMethod = "EXPRESS" }
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Express Priority Delivery", fontWeight = FontWeight.Bold)
                                    Text("Guaranteed next-day delivery before 2:00 PM", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                }
                                Text("Rs. 350", fontWeight = FontWeight.Bold, color = RoyalBluePrimary)
                            }
                        }
                    }
                    3 -> {
                        // STEP 3: PAYMENT METHOD
                        Text(
                            text = "Select Payment Method",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Cash on Delivery
                        PaymentOptionCard(
                            title = "Cash on Delivery (COD)",
                            subtitle = "Pay cash to courier when order arrives at doorstep",
                            icon = Icons.Filled.Payments,
                            isSelected = selectedPaymentMethod == "COD",
                            onClick = { selectedPaymentMethod = "COD" }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Credit/Debit Card
                        PaymentOptionCard(
                            title = "Credit / Debit Card",
                            subtitle = "Visa, MasterCard, PayPak & UnionPay",
                            icon = Icons.Filled.CreditCard,
                            isSelected = selectedPaymentMethod == "CARD",
                            onClick = { selectedPaymentMethod = "CARD" }
                        )

                        if (selectedPaymentMethod == "CARD") {
                            CardInputForm(
                                cardNumber = cardNumber,
                                onCardNumberChange = { cardNumber = it },
                                expiry = cardExpiry,
                                onExpiryChange = { cardExpiry = it },
                                cvv = cardCvv,
                                onCvvChange = { cardCvv = it }
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // JazzCash
                        PaymentOptionCard(
                            title = "JazzCash Mobile Account",
                            subtitle = "Pay directly via mobile wallet PIN prompt",
                            icon = Icons.Filled.AccountBalanceWallet,
                            isSelected = selectedPaymentMethod == "JAZZCASH",
                            onClick = { selectedPaymentMethod = "JAZZCASH" }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Easypaisa
                        PaymentOptionCard(
                            title = "Easypaisa Wallet",
                            subtitle = "Instant approval via Easypaisa OTP",
                            icon = Icons.Filled.PhoneAndroid,
                            isSelected = selectedPaymentMethod == "EASYPAISA",
                            onClick = { selectedPaymentMethod = "EASYPAISA" }
                        )
                    }
                    4 -> {
                        // STEP 4: ORDER REVIEW
                        Text(
                            text = "Review Order Summary",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Address summary card
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Shipping Address", fontWeight = FontWeight.Bold)
                                    TextButton(onClick = { currentStep = 1 }) {
                                        Text("Change", color = RoyalBluePrimary)
                                    }
                                }
                                Text(currentSelectedAddress?.formattedAddress ?: "", style = MaterialTheme.typography.bodySmall)
                                Text("Contact: ${currentSelectedAddress?.phoneNumber}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Items list summary
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Items in Order (${selectedItems.size})", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))

                                selectedItems.forEach { item ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${item.quantity}x ${item.product.name}",
                                            style = MaterialTheme.typography.bodySmall,
                                            maxLines = 1,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = Formatters.formatPkr(item.totalPrice),
                                            fontWeight = FontWeight.SemiBold,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Total summary
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("Price Breakdown", fontWeight = FontWeight.Bold)
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Subtotal", style = MaterialTheme.typography.bodySmall)
                                    Text(Formatters.formatPkr(subtotal), style = MaterialTheme.typography.bodySmall)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Delivery Charges", style = MaterialTheme.typography.bodySmall)
                                    Text(if (deliveryFee == 0.0) "FREE" else Formatters.formatPkr(deliveryFee), style = MaterialTheme.typography.bodySmall)
                                }
                                if (discountAmount > 0.0) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("Discount Applied", style = MaterialTheme.typography.bodySmall, color = SuccessGreen)
                                        Text("-${Formatters.formatPkr(discountAmount)}", style = MaterialTheme.typography.bodySmall, color = SuccessGreen)
                                    }
                                }
                                Divider(modifier = Modifier.padding(vertical = 4.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Grand Total", fontWeight = FontWeight.Bold)
                                    Text(Formatters.formatPkr(finalTotal), fontWeight = FontWeight.Bold, color = RoyalBluePrimary, fontSize = 18.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StepIndicator(currentStep: Int) {
    val steps = listOf("Address", "Delivery", "Payment", "Review")
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, title ->
                val stepNum = index + 1
                val isCompleted = stepNum < currentStep
                val isCurrent = stepNum == currentStep

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        shape = CircleShape,
                        color = when {
                            isCompleted -> SuccessGreen
                            isCurrent -> RoyalBluePrimary
                            else -> Color(0xFFE2E8F0)
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (isCompleted) {
                                Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            } else {
                                Text(
                                    text = stepNum.toString(),
                                    color = if (isCurrent) Color.White else Color.Gray,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                        color = if (isCurrent) RoyalBluePrimary else Color.Gray
                    )
                }

                if (index < steps.size - 1) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(2.dp)
                            .padding(horizontal = 4.dp)
                            .background(if (stepNum < currentStep) SuccessGreen else Color(0xFFE2E8F0))
                    )
                }
            }
        }
    }
}

@Composable
fun PaymentOptionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = if (isSelected) BorderStroke(2.dp, RoyalBluePrimary) else null,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = isSelected, onClick = onClick)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(imageVector = icon, contentDescription = null, tint = RoyalBluePrimary)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}

@Composable
fun CardInputForm(
    cardNumber: String,
    onCardNumberChange: (String) -> Unit,
    expiry: String,
    onExpiryChange: (String) -> Unit,
    cvv: String,
    onCvvChange: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8FAFC),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            OutlinedTextField(
                value = cardNumber,
                onValueChange = onCardNumberChange,
                label = { Text("Card Number") },
                leadingIcon = { Icon(Icons.Filled.CreditCard, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = expiry,
                    onValueChange = onExpiryChange,
                    label = { Text("MM/YY") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                OutlinedTextField(
                    value = cvv,
                    onValueChange = onCvvChange,
                    label = { Text("CVV") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun AddAddressDialog(
    onSave: (Address) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("Lahore") }
    var area by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("54000") }
    var label by remember { mutableStateOf("Home") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Delivery Address", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Recipient Name") }, singleLine = true)
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number (+92)") }, singleLine = true)
                OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City (e.g. Karachi, Lahore, Islamabad)") }, singleLine = true)
                OutlinedTextField(value = area, onValueChange = { area = it }, label = { Text("Area / Sector / Town") }, singleLine = true)
                OutlinedTextField(value = street, onValueChange = { street = it }, label = { Text("House / Street Address") })
                OutlinedTextField(value = postalCode, onValueChange = { postalCode = it }, label = { Text("Postal Code") }, singleLine = true)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && phone.isNotBlank() && street.isNotBlank()) {
                        onSave(
                            Address(
                                recipientName = name,
                                phoneNumber = phone,
                                city = city,
                                area = area,
                                streetAddress = street,
                                postalCode = postalCode,
                                isDefault = true,
                                addressLabel = label
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBluePrimary)
            ) {
                Text("Save Address")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
