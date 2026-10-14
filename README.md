# Bazaar — Premium Android E-Commerce Shopping App

**Bazaar** is a modern, premium-quality Android e-commerce application designed and architected with Jetpack Compose, Material 3, Clean Architecture, and MVVM.

- **Package Name:** `com.bilal.bazaar`
- **Platform:** Android (Min SDK 26, Target SDK 36)
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (M3)
- **Currency:** Pakistani Rupees (PKR / Rs.)

---

## 🎨 Design System & Visuals

- **Primary Color:** Deep Royal Blue (`#1646A0`)
- **Secondary Color:** Vibrant Orange (`#FF8A00`)
- **Background:** `#F8FAFC`
- **Surface:** `#FFFFFF`
- **Typography:** Material 3 Type System with dynamic accessibility scaling
- **Adaptive App Icon:** Custom logo foreground centered on Deep Royal Blue with PNG rasters across all density buckets (`mdpi` to `xxxhdpi`).

---

## 🚀 Key Features

1. **Splash & Onboarding:**
   - Animated entrance of the Bazaar logo.
   - 3 interactive onboarding slides explaining deals, nationwide delivery across Pakistan, and Buyer Protection.
   - Skip and Get Started flows.

2. **Authentication Flow (Mock-Ready):**
   - Login, Registration, Forgot Password, and OTP Verification (Demo code: `1234`).
   - "Continue as Guest" one-tap guest browsing.
   - Google Sign-In and Social login placeholders.

3. **Homepage:**
   - Delivery Location selector modal (Karachi, Lahore, Islamabad, etc.).
   - Promotional Banner Carousel (Mega Shopping Festival, Electronics Cyber Showcase, Trendy Fashion Apparel).
   - Quick Service Guarantees (100% Authentic, Fast Nationwide Delivery, 7-Day Returns, COD).
   - 12 Category quick shortcuts with custom vector icons.
   - Real-time Flash Sale Countdown Timer (hours, minutes, seconds).
   - Featured Products, Trending Now, Official Brand Stores, and Best Sellers sections.

4. **Product Discovery & Category Browsing:**
   - Complete 12-category catalog with nested subcategory browsing.
   - Category-specific product listing grid with real-time in-stock filtering and sorting.

5. **Advanced Search & Filters:**
   - Instant query execution against 64+ mock products.
   - Recent search query chips with individual and bulk clear.
   - Trending search tags ("Wireless Earbuds", "Air Fryer", "Cricket Bat", etc.).
   - Simulated voice search dialog.
   - Multi-criteria Filter Bottom Sheet (price range, brands, minimum customer rating, in-stock only, and sort criteria).

6. **Product Detail Screen:**
   - High-resolution gallery with full-screen zoom preview dialog.
   - Brand, ratings, verified review count, stock availability badge.
   - Color selection chips and size/storage configuration options.
   - Interactive quantity stepper (- / +).
   - Tabbed specs: Details, Technical Specifications table, Delivery & Return guarantee, and Customer Reviews.
   - Customer review submission dialog (star picker, headline, and comment).
   - Native Android Sharesheet intent sharing.
   - Sticky bottom purchase bar with Add to Cart and Buy Now.

7. **Shopping Cart:**
   - Interactive item selection (select all / individual).
   - Real-time quantity updates and item removal with confirmation dialog.
   - Promo coupon engine supporting codes:
     - `BAZAAR10`: 10% discount
     - `WELCOME500`: Flat Rs. 500 off
     - `FREESHIP`: Free courier delivery
   - Automatic free delivery threshold (> Rs. 2,000).

8. **Multi-Step Checkout:**
   - Step 1: Delivery Address (select existing, add new address modal with Pakistani cities).
   - Step 2: Delivery Method (Standard 2-3 day delivery vs. Express next-day delivery).
   - Step 3: Payment Method (Cash on Delivery (COD), Credit/Debit Card form, JazzCash, Easypaisa).
   - Step 4: Order Review & Instant Order Placement.

9. **Orders & Live Tracking:**
   - Order history with tabbed status filtering (All, Active, Delivered, Cancelled).
   - Comprehensive Order Details screen with item summaries and payment records.
   - Real-time Order Tracking Timeline (Placed -> Confirmed -> Packed & QC -> Dispatched with TCS Express -> Out for Delivery -> Delivered).
   - Order Cancellation dialog (for pending orders) and 7-day return request flow (for delivered items).
   - One-tap "Reorder Items" into Cart.

10. **Wishlist:**
    - Real-time synchronized heart icon across all product cards and detail screens.
    - One-tap "Move to Cart" action.

11. **User Profile & Settings:**
    - VIP club badge and user profile avatar.
    - Profile editing modal.
    - 24/7 Support dialog with UAN hotline, email, and WhatsApp help.
    - Push notifications toggle and language switch (English / Urdu).

12. **Notifications:**
    - Order dispatched alerts, 70% Flash Sale alerts, price drop notifications, and promotional vouchers.
    - Read/unread state badges and bulk mark-as-read.

---

## 🏗️ Architecture & Backend Integration Guide

The app follows **Clean Architecture** and **MVVM** principles with strict separation of concerns:

```text
com.example
├── core
│   ├── designsystem (ProductCard, RatingBar, Shimmer, Badges)
│   ├── di (AppContainer, ServiceLocator)
│   ├── navigation (Screen destinations, BottomNavTab)
│   └── utils (Pakistani Rupee Formatters)
├── data
│   ├── mock (MockData: 64 products, 12 categories, addresses, orders, reviews)
│   └── repository (MockProductRepository, MockCartRepository, MockOrderRepository, etc.)
├── domain
│   ├── model (Product, Category, CartItem, Order, Address, Review, User)
│   └── repository (ProductRepository, CartRepository, OrderRepository interfaces)
├── feature
│   ├── splash & onboarding
│   ├── auth (Login, Register, Forgot Password, OTP)
│   ├── home (Promotional carousel, Flash sale timer, Brand showcase)
│   ├── category & search (Instant search, Filter bottom sheet)
│   ├── product (Gallery, specs, reviews, share intent)
│   ├── cart & checkout (Voucher engine, delivery options, payment options)
│   ├── orders (Timeline tracking, cancel, return, reorder)
│   ├── wishlist & profile
│   └── notification & reviews
└── ui.theme (Royal Blue & Vibrant Orange color tokens, Typography)
```

### Future Backend / API / Database Integration:

To plug in a real REST API, Firebase Firestore, or Supabase:
1. Implement the domain repository interfaces in `com.example.domain.repository` (e.g., `RemoteProductRepository : ProductRepository`, `FirebaseOrderRepository : OrderRepository`).
2. Replace the mock instance bindings in `com.example.core.di.AppContainer`.
3. The Jetpack Compose UI requires **zero changes** as it only interacts with the repository interfaces and ViewModel StateFlows!
