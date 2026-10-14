package com.example.data.mock

import com.example.domain.model.*

object MockData {

    val categories = listOf(
        Category("cat_mobiles", "Mobile Phones", "ic_phone", listOf("Smartphones", "Feature Phones", "Tablets", "Cases & Covers"), 8),
        Category("cat_computers", "Computers", "ic_laptop", listOf("Laptops", "Desktops", "Monitors", "Keyboards & Mice"), 6),
        Category("cat_electronics", "Electronics", "ic_tv", listOf("Televisions", "Audio & Soundbars", "Cameras", "Headphones"), 7),
        Category("cat_fashion", "Fashion", "ic_fashion", listOf("Men's Wear", "Women's Ethnic", "Western Wear", "Kids"), 6),
        Category("cat_shoes", "Shoes", "ic_shoes", listOf("Sneakers", "Formal Shoes", "Traditional Chappals", "Boots"), 5),
        Category("cat_watches", "Watches", "ic_watch", listOf("Smartwatches", "Luxury Chronographs", "Casual", "Sports"), 5),
        Category("cat_beauty", "Beauty", "ic_beauty", listOf("Skincare", "Fragrances", "Hair Care", "Makeup"), 5),
        Category("cat_home", "Home & Living", "ic_home", listOf("Kitchen Appliances", "Furniture", "Bedding", "Home Decor"), 5),
        Category("cat_groceries", "Groceries", "ic_grocery", listOf("Rice & Flour", "Edible Oils", "Tea & Coffee", "Dry Fruits"), 5),
        Category("cat_sports", "Sports", "ic_sports", listOf("Cricket", "Football", "Fitness & Gym", "Racquet Sports"), 5),
        Category("cat_toys", "Toys", "ic_toys", listOf("Remote Control", "STEM & Building", "Board Games", "Plush"), 5),
        Category("cat_accessories", "Accessories", "ic_accessories", listOf("Power Banks", "Bags & Backpacks", "Wallets", "Sunglasses"), 5)
    )

    val products = listOf(
        // MOBILE PHONES (1-6)
        Product(
            id = "p_mob_1",
            name = "Samsung Galaxy S24 Ultra 5G (12GB RAM, 256GB)",
            brand = "Samsung",
            categoryId = "cat_mobiles",
            categoryName = "Mobile Phones",
            price = 379999.0,
            originalPrice = 425000.0,
            rating = 4.9f,
            reviewCount = 284,
            stock = 14,
            description = "Experience next-generation mobile intelligence with Galaxy AI, Titanium frame, 200MP quad camera with 100x Space Zoom, and built-in S Pen.",
            specifications = mapOf(
                "Display" to "6.8\" Dynamic AMOLED 2X, 120Hz",
                "Processor" to "Snapdragon 8 Gen 3 for Galaxy",
                "Battery" to "5,000 mAh with 45W Fast Charging",
                "Operating System" to "One UI 6.1 (Android 14)",
                "Warranty" to "1 Year Official PTA Approved Brand Warranty"
            ),
            imageUrls = listOf("banner_electronics", "banner_mega_sale"),
            colors = listOf("Titanium Gray", "Titanium Black", "Titanium Violet"),
            sizes = listOf("256GB", "512GB", "1TB"),
            isFeatured = true,
            isFlashSale = true,
            isBestSeller = true
        ),
        Product(
            id = "p_mob_2",
            name = "Apple iPhone 15 Pro Max (256GB) PTA Approved",
            brand = "Apple",
            categoryId = "cat_mobiles",
            categoryName = "Mobile Phones",
            price = 455000.0,
            originalPrice = 490000.0,
            rating = 4.9f,
            reviewCount = 340,
            stock = 8,
            description = "Forged in aerospace-grade titanium with the groundbreaking A17 Pro chip, customizable Action button, and 5x optical telephoto camera.",
            specifications = mapOf(
                "Display" to "6.7\" Super Retina XDR with ProMotion",
                "Chip" to "A17 Pro chip with 6-core GPU",
                "Camera" to "48MP Main | Ultra Wide | 5x Telephoto",
                "Port" to "USB-C with USB 3 speeds",
                "Warranty" to "1 Year Apple Official International + PTA"
            ),
            imageUrls = listOf("banner_electronics"),
            colors = listOf("Natural Titanium", "Blue Titanium", "Black Titanium"),
            sizes = listOf("256GB", "512GB"),
            isFeatured = true,
            isTrending = true
        ),
        Product(
            id = "p_mob_3",
            name = "Xiaomi Redmi Note 13 Pro+ 5G (12GB/512GB)",
            brand = "Xiaomi",
            categoryId = "cat_mobiles",
            categoryName = "Mobile Phones",
            price = 119999.0,
            originalPrice = 139999.0,
            rating = 4.7f,
            reviewCount = 188,
            stock = 25,
            description = "Featuring a 200MP OIS camera, 1.5K 120Hz curved AMOLED display, and blazing fast 120W HyperCharge that recharges in 19 minutes.",
            specifications = mapOf(
                "Display" to "6.67\" 1.5K 120Hz Curved AMOLED",
                "Charging" to "120W HyperCharge in-box",
                "Protection" to "IP68 Dust and Water Resistance",
                "Warranty" to "1 Year Official Warranty"
            ),
            imageUrls = listOf("banner_electronics"),
            colors = listOf("Midnight Black", "Aurora Purple"),
            sizes = listOf("256GB", "512GB"),
            isNewArrival = true
        ),
        Product(
            id = "p_mob_4",
            name = "OnePlus 12 (16GB RAM, 512GB)",
            brand = "OnePlus",
            categoryId = "cat_mobiles",
            categoryName = "Mobile Phones",
            price = 249000.0,
            originalPrice = 279000.0,
            rating = 4.8f,
            reviewCount = 112,
            stock = 11,
            description = "Extreme performance powered by Snapdragon 8 Gen 3, 4th Gen Hasselblad Camera System, and 5400 mAh battery with 100W SUPERVOOC.",
            specifications = mapOf(
                "Display" to "6.82\" 2K 120Hz ProXDR",
                "Processor" to "Snapdragon 8 Gen 3",
                "Camera" to "Hasselblad 50MP + 64MP 3x Periscope",
                "Warranty" to "1 Year Warranty"
            ),
            imageUrls = listOf("banner_electronics"),
            colors = listOf("Flowy Emerald", "Silky Black"),
            sizes = listOf("256GB", "512GB")
        ),
        Product(
            id = "p_mob_5",
            name = "Samsung Galaxy A55 5G (8GB/256GB)",
            brand = "Samsung",
            categoryId = "cat_mobiles",
            categoryName = "Mobile Phones",
            price = 124999.0,
            originalPrice = 139999.0,
            rating = 4.6f,
            reviewCount = 95,
            stock = 30,
            description = "Premium metal flat frame, Gorilla Glass Victus+, 50MP main camera with Nightography, and IP67 water resistance.",
            specifications = mapOf(
                "Display" to "6.6\" Super AMOLED 120Hz",
                "Processor" to "Exynos 1480 with Xclipse 530 GPU",
                "Battery" to "5,000 mAh"
            ),
            imageUrls = listOf("banner_electronics"),
            colors = listOf("Awesome Iceblue", "Awesome Navy"),
            sizes = listOf("128GB", "256GB"),
            isBestSeller = true
        ),
        Product(
            id = "p_mob_6",
            name = "Infinix Zero 30 5G (12GB RAM, 256GB)",
            brand = "Infinix",
            categoryId = "cat_mobiles",
            categoryName = "Mobile Phones",
            price = 79999.0,
            originalPrice = 94999.0,
            rating = 4.5f,
            reviewCount = 76,
            stock = 19,
            description = "Vlogging powerhouse with 50MP 4K 60FPS front camera, 108MP rear camera, and 144Hz 3D curved AMOLED screen.",
            specifications = mapOf(
                "Front Camera" to "50MP 4K 60fps Ultra HD",
                "Display" to "6.78\" 144Hz 3D Curved AMOLED",
                "RAM" to "12GB + 9GB Extended"
            ),
            imageUrls = listOf("banner_electronics"),
            colors = listOf("Golden Hour", "Rome Green"),
            sizes = listOf("256GB")
        ),

        // COMPUTERS & LAPTOPS (7-12)
        Product(
            id = "p_comp_1",
            name = "Apple MacBook Air 15\" M3 Chip (16GB, 512GB SSD)",
            brand = "Apple",
            categoryId = "cat_computers",
            categoryName = "Computers",
            price = 385000.0,
            originalPrice = 420000.0,
            rating = 4.9f,
            reviewCount = 142,
            stock = 10,
            description = "Strikingly thin and fast with the next-generation M3 chip, up to 18 hours of battery life, and brilliant Liquid Retina display.",
            specifications = mapOf(
                "Screen" to "15.3\" Liquid Retina display with True Tone",
                "Processor" to "Apple M3 (8-core CPU, 10-core GPU)",
                "Memory" to "16GB Unified Memory",
                "Storage" to "512GB Fast SSD",
                "Battery" to "Up to 18 hours wireless web"
            ),
            imageUrls = listOf("banner_electronics"),
            colors = listOf("Midnight", "Space Gray", "Starlight", "Silver"),
            sizes = listOf("512GB SSD", "1TB SSD"),
            isFeatured = true,
            isBestSeller = true
        ),
        Product(
            id = "p_comp_2",
            name = "Dell XPS 15 9530 Core i9 13th Gen (32GB, 1TB SSD, RTX 4070)",
            brand = "Dell",
            categoryId = "cat_computers",
            categoryName = "Computers",
            price = 545000.0,
            originalPrice = 590000.0,
            rating = 4.8f,
            reviewCount = 68,
            stock = 5,
            description = "Precision crafted with CNC machined aluminum and carbon fiber palm rest, 3.5K OLED touchscreen, and NVIDIA RTX graphics for heavy creators.",
            specifications = mapOf(
                "Display" to "15.6\" 3.5K OLED Touch 400-nit",
                "CPU" to "Intel Core i9-13900H (14 cores)",
                "GPU" to "NVIDIA GeForce RTX 4070 8GB GDDR6",
                "RAM" to "32GB DDR5 4800MHz"
            ),
            imageUrls = listOf("banner_electronics"),
            colors = listOf("Platinum Silver"),
            sizes = listOf("1TB SSD", "2TB SSD")
        ),
        Product(
            id = "p_comp_3",
            name = "ASUS ROG Strix G16 Gaming Laptop (RTX 4060, i7 13th Gen)",
            brand = "ASUS",
            categoryId = "cat_computers",
            categoryName = "Computers",
            price = 365000.0,
            originalPrice = 410000.0,
            rating = 4.7f,
            reviewCount = 92,
            stock = 12,
            description = "Dominate esports with ROG Nebula 165Hz display, Tri-Fan technology with Conductonaut Extreme liquid metal cooling.",
            specifications = mapOf(
                "Display" to "16\" FHD+ 165Hz 100% sRGB",
                "Graphics" to "NVIDIA GeForce RTX 4060 140W TGP",
                "CPU" to "Intel Core i7-13650HX",
                "Keyboard" to "4-Zone RGB Backlit"
            ),
            imageUrls = listOf("banner_electronics"),
            colors = listOf("Eclipse Gray"),
            sizes = listOf("16GB/512GB", "32GB/1TB"),
            isTrending = true
        ),
        Product(
            id = "p_comp_4",
            name = "HP Pavilion 15 Core i5 12th Gen (8GB, 512GB SSD)",
            brand = "HP",
            categoryId = "cat_computers",
            categoryName = "Computers",
            price = 148000.0,
            originalPrice = 165000.0,
            rating = 4.5f,
            reviewCount = 130,
            stock = 22,
            description = "Everyday workhorse laptop with B&O tuned dual speakers, narrow bezel micro-edge FHD screen, and long-lasting battery with fast charge.",
            specifications = mapOf(
                "Display" to "15.6\" Full HD IPS Anti-glare",
                "CPU" to "Intel Core i5-1235U",
                "Audio" to "Audio by Bang & Olufsen",
                "Weight" to "1.74 kg"
            ),
            imageUrls = listOf("banner_electronics"),
            colors = listOf("Natural Silver", "Warm Gold"),
            sizes = listOf("512GB SSD")
        ),
        Product(
            id = "p_comp_5",
            name = "Lenovo ThinkPad X1 Carbon Gen 11 (Core i7, 16GB, 512GB)",
            brand = "Lenovo",
            categoryId = "cat_computers",
            categoryName = "Computers",
            price = 420000.0,
            originalPrice = 460000.0,
            rating = 4.9f,
            reviewCount = 54,
            stock = 7,
            description = "The gold standard of corporate ultrabooks. Ultralight carbon fiber body, legendary ergonomic keyboard, and robust enterprise security.",
            specifications = mapOf(
                "Display" to "14\" WUXGA IPS Low Power 400 nits",
                "Weight" to "1.12 kg ultralight",
                "Security" to "dTPM 2.0, Fingerprint & IR Camera"
            ),
            imageUrls = listOf("banner_electronics"),
            colors = listOf("Deep Black"),
            sizes = listOf("512GB SSD", "1TB SSD")
        ),
        Product(
            id = "p_comp_6",
            name = "Samsung Odyssey G7 28\" 4K UHD 144Hz Gaming Monitor",
            brand = "Samsung",
            categoryId = "cat_computers",
            categoryName = "Computers",
            price = 165000.0,
            originalPrice = 195000.0,
            rating = 4.8f,
            reviewCount = 47,
            stock = 9,
            description = "IPS panel with 4K UHD resolution, 144Hz refresh rate, 1ms response time, HDMI 2.1 support, and CoreSync ambient lighting.",
            specifications = mapOf(
                "Resolution" to "3840 x 2160 4K UHD",
                "Refresh Rate" to "144Hz with 1ms GtG",
                "Sync" to "G-Sync Compatible & FreeSync Premium Pro"
            ),
            imageUrls = listOf("banner_electronics"),
            colors = listOf("Black"),
            sizes = listOf("28-inch")
        ),

        // ELECTRONICS & AUDIO (13-18)
        Product(
            id = "p_elec_1",
            name = "Sony WH-1000XM5 Wireless Noise-Cancelling Headphones",
            brand = "Sony",
            categoryId = "cat_electronics",
            categoryName = "Electronics",
            price = 88000.0,
            originalPrice = 105000.0,
            rating = 4.9f,
            reviewCount = 210,
            stock = 18,
            description = "Industry-leading noise cancelation with two processors and eight microphones, Auto NC Optimizer, and 30-hour battery life.",
            specifications = mapOf(
                "Battery Life" to "Up to 30 hours (ANC on)",
                "Codecs" to "LDAC, AAC, SBC with Hi-Res Audio",
                "Microphones" to "4 beamforming mics with AI noise reduction"
            ),
            imageUrls = listOf("banner_electronics", "banner_mega_sale"),
            colors = listOf("Silver", "Black", "Midnight Blue"),
            sizes = listOf("Over-Ear Standard"),
            isFeatured = true,
            isFlashSale = true
        ),
        Product(
            id = "p_elec_2",
            name = "TCL 55\" C645 QLED 4K Google Smart TV",
            brand = "TCL",
            categoryId = "cat_electronics",
            categoryName = "Electronics",
            price = 134999.0,
            originalPrice = 159999.0,
            rating = 4.7f,
            reviewCount = 85,
            stock = 14,
            description = "Quantum Dot technology with over 1 billion vibrant colors, Dolby Vision & Atmos, HDR 10+, and hands-free Google Assistant.",
            specifications = mapOf(
                "Screen Size" to "55-inch Quantum Dot QLED 4K",
                "Audio" to "ONKYO Sound System with Dolby Atmos",
                "Gaming" to "Game Accelerator 120Hz & HDMI 2.1",
                "Warranty" to "2 Years Official Brand Warranty"
            ),
            imageUrls = listOf("banner_electronics"),
            colors = listOf("Brushed Titanium"),
            sizes = listOf("55-inch", "65-inch")
        ),
        Product(
            id = "p_elec_3",
            name = "JBL Charge 5 Portable Waterproof Bluetooth Speaker",
            brand = "JBL",
            categoryId = "cat_electronics",
            categoryName = "Electronics",
            price = 38500.0,
            originalPrice = 46000.0,
            rating = 4.8f,
            reviewCount = 175,
            stock = 32,
            description = "Bold JBL Original Pro Sound with long excursion driver, separate tweeter, and dual passive bass radiators. IP67 waterproof with built-in powerbank.",
            specifications = mapOf(
                "Battery" to "20 hours playtime",
                "Durability" to "IP67 Waterproof and Dustproof",
                "Feature" to "PartyBoost pairing & USB powerbank"
            ),
            imageUrls = listOf("banner_electronics"),
            colors = listOf("Squad Green", "Deep Black", "Red", "Blue"),
            sizes = listOf("Standard"),
            isTrending = true
        ),
        Product(
            id = "p_elec_4",
            name = "Anker Soundcore Space A40 ANC Earbuds",
            brand = "Anker",
            categoryId = "cat_electronics",
            categoryName = "Electronics",
            price = 16999.0,
            originalPrice = 22000.0,
            rating = 4.6f,
            reviewCount = 140,
            stock = 45,
            description = "Ultra-compact earbuds reducing noise by up to 98% with Adaptive ANC, LDAC Hi-Res Wireless, and 50 hours of total playtime.",
            specifications = mapOf(
                "Noise Reduction" to "Adaptive Active Noise Cancellation",
                "Playtime" to "10 hours buds + 40 hours case",
                "Charging" to "Wireless charging & USB-C fast charge"
            ),
            imageUrls = listOf("banner_electronics"),
            colors = listOf("Black", "White", "Navy"),
            sizes = listOf("In-Ear"),
            isBestSeller = true
        ),
        Product(
            id = "p_elec_5",
            name = "Sony Alpha A6700 Mirrorless Camera Body",
            brand = "Sony",
            categoryId = "cat_electronics",
            categoryName = "Electronics",
            price = 375000.0,
            originalPrice = 410000.0,
            rating = 4.9f,
            reviewCount = 38,
            stock = 6,
            description = "Next-generation 26MP BSI APS-C sensor with dedicated AI Processing Unit for real-time subject recognition and 4K 120p video.",
            specifications = mapOf(
                "Sensor" to "26.0MP Exmor R CMOS APS-C",
                "Video" to "4K 60p oversampled from 6K, 4K 120p",
                "Stabilization" to "5-axis in-body optical stabilization"
            ),
            imageUrls = listOf("banner_electronics"),
            colors = listOf("Black"),
            sizes = listOf("Body Only", "16-50mm Kit")
        ),
        Product(
            id = "p_elec_6",
            name = "Apple iPad Air 11\" M2 Chip (128GB Wi-Fi)",
            brand = "Apple",
            categoryId = "cat_electronics",
            categoryName = "Electronics",
            price = 189000.0,
            originalPrice = 210000.0,
            rating = 4.8f,
            reviewCount = 89,
            stock = 15,
            description = "Supercharged by the M2 chip with landscape front camera, support for Apple Pencil Pro, and stunning Liquid Retina display.",
            specifications = mapOf(
                "Chip" to "Apple M2 8-core CPU, 9-core GPU",
                "Display" to "11\" Liquid Retina True Tone P3",
                "Biometrics" to "Touch ID in Top Button"
            ),
            imageUrls = listOf("banner_electronics"),
            colors = listOf("Space Gray", "Blue", "Purple", "Starlight"),
            sizes = listOf("128GB", "256GB")
        ),

        // FASHION (19-24)
        Product(
            id = "p_fash_1",
            name = "Junaid Jamshed Premium Cotton Kurta Shalwar Suit",
            brand = "J.",
            categoryId = "cat_fashion",
            categoryName = "Fashion",
            price = 7990.0,
            originalPrice = 10500.0,
            rating = 4.8f,
            reviewCount = 312,
            stock = 40,
            description = "Crafted from 100% fine Egyptian cotton with intricate tonal embroidery on the band collar and placket. Ideal for festive and formal occasions.",
            specifications = mapOf(
                "Fabric" to "100% Premium Egyptian Giza Cotton",
                "Cut" to "Regular Classic Fit with Straight Shalwar",
                "Care" to "Hand wash or gentle machine wash"
            ),
            imageUrls = listOf("banner_fashion", "banner_mega_sale"),
            colors = listOf("Royal Navy", "Jet Black", "Crisp White", "Charcoal Gray"),
            sizes = listOf("S", "M", "L", "XL", "XXL"),
            isFeatured = true,
            isFlashSale = true,
            isBestSeller = true
        ),
        Product(
            id = "p_fash_2",
            name = "Khaadi Embroidered 3-Piece Lawn Stitched Suit",
            brand = "Khaadi",
            categoryId = "cat_fashion",
            categoryName = "Fashion",
            price = 9450.0,
            originalPrice = 12500.0,
            rating = 4.9f,
            reviewCount = 280,
            stock = 35,
            description = "Luxury printed and embroidered lawn shirt paired with embroidered chiffon dupatta and solid dyed cambric trousers.",
            specifications = mapOf(
                "Shirt" to "Digital Printed Embroidered Lawn",
                "Dupatta" to "Pure Crinkle Chiffon Printed",
                "Trouser" to "Cambric Cotton Dyed"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("Emerald Green", "Blush Pink", "Mustard Yellow"),
            sizes = listOf("XS", "S", "M", "L"),
            isTrending = true
        ),
        Product(
            id = "p_fash_3",
            name = "Levi's 511 Slim Fit Stretch Denim Jeans",
            brand = "Levi's",
            categoryId = "cat_fashion",
            categoryName = "Fashion",
            price = 11990.0,
            originalPrice = 14990.0,
            rating = 4.7f,
            reviewCount = 160,
            stock = 28,
            description = "A modern slim with room to move. Added stretch for all-day comfort with signature arcuate back pocket stitching.",
            specifications = mapOf(
                "Material" to "99% Cotton, 1% Elastane",
                "Fit" to "Slim through hip and thigh, slim leg",
                "Closure" to "Zip fly with metal button"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("Dark Indigo", "Washed Blue", "Black Wash"),
            sizes = listOf("30/32", "32/32", "34/32", "36/32"),
            isBestSeller = true
        ),
        Product(
            id = "p_fash_4",
            name = "Sapphire Men's Casual Linen Oxford Shirt",
            brand = "Sapphire",
            categoryId = "cat_fashion",
            categoryName = "Fashion",
            price = 4490.0,
            originalPrice = 5990.0,
            rating = 4.6f,
            reviewCount = 88,
            stock = 35,
            description = "Breathable pure linen button-down shirt designed for smart casual wear in warm weather.",
            specifications = mapOf(
                "Fabric" to "100% Breathable Linen",
                "Collar" to "Button-Down Smart Collar",
                "Fit" to "Tailored Regular"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("Sky Blue", "Olive Green", "White"),
            sizes = listOf("S", "M", "L", "XL")
        ),
        Product(
            id = "p_fash_5",
            name = "Zara Floral Print Wrap Midi Dress",
            brand = "Zara",
            categoryId = "cat_fashion",
            categoryName = "Fashion",
            price = 12500.0,
            originalPrice = 16000.0,
            rating = 4.6f,
            reviewCount = 64,
            stock = 16,
            description = "Flowy midi dress with V-neckline, long puff sleeves, and tie closure at the waist with vibrant floral motifs.",
            specifications = mapOf(
                "Material" to "100% Viscose",
                "Length" to "Midi below the knee",
                "Style" to "A-Line Wrap Fit"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("Cobalt Floral", "Ruby Red"),
            sizes = listOf("S", "M", "L")
        ),
        Product(
            id = "p_fash_6",
            name = "Nike Sportswear Club Fleece Pullover Hoodie",
            brand = "Nike",
            categoryId = "cat_fashion",
            categoryName = "Fashion",
            price = 14500.0,
            originalPrice = 17500.0,
            rating = 4.8f,
            reviewCount = 115,
            stock = 24,
            description = "Classic comfort made with brushed-back fleece for a soft, warm feel and embroidered Futura logo on left chest.",
            specifications = mapOf(
                "Material" to "80% Cotton, 20% Polyester",
                "Hood" to "Drawstring adjustable hood",
                "Pocket" to "Kangaroo front pocket"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("Dark Grey Heather", "Black", "Navy"),
            sizes = listOf("M", "L", "XL", "XXL"),
            isNewArrival = true
        ),

        // SHOES (25-29)
        Product(
            id = "p_shoe_1",
            name = "Nike Air Zoom Pegasus 40 Running Shoes",
            brand = "Nike",
            categoryId = "cat_shoes",
            categoryName = "Shoes",
            price = 32500.0,
            originalPrice = 38000.0,
            rating = 4.9f,
            reviewCount = 195,
            stock = 20,
            description = "Springy ride for every run with dual Zoom Air units, React foam technology, and engineered single-layer mesh upper.",
            specifications = mapOf(
                "Cushioning" to "Nike React technology with 2 Zoom Air units",
                "Terrain" to "Road & Track Running",
                "Drop" to "10mm heel-to-toe drop"
            ),
            imageUrls = listOf("banner_fashion", "banner_mega_sale"),
            colors = listOf("White / Royal Blue", "Black / Metallic", "Volt"),
            sizes = listOf("US 8", "US 9", "US 10", "US 11"),
            isFeatured = true,
            isTrending = true
        ),
        Product(
            id = "p_shoe_2",
            name = "Traditional Charsadda Handmade Peshawari Chappal",
            brand = "Charsadda Heritage",
            categoryId = "cat_shoes",
            categoryName = "Shoes",
            price = 4850.0,
            originalPrice = 6500.0,
            rating = 4.8f,
            reviewCount = 260,
            stock = 50,
            description = "Master artisan hand-stitched authentic cowhide leather with durable tyre sole. The quintessential traditional Pakistani footwear.",
            specifications = mapOf(
                "Upper" to "100% Genuine Full Grain Cow Leather",
                "Sole" to "Lightweight recycled aircraft tyre rubber",
                "Insole" to "Cushioned memory foam layer"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("Mustard Tan", "Deep Brown", "Matte Black"),
            sizes = listOf("7", "8", "9", "10", "11"),
            isBestSeller = true
        ),
        Product(
            id = "p_shoe_3",
            name = "Bata Signature Italian Leather Oxford Dress Shoes",
            brand = "Bata",
            categoryId = "cat_shoes",
            categoryName = "Shoes",
            price = 13999.0,
            originalPrice = 16999.0,
            rating = 4.7f,
            reviewCount = 82,
            stock = 18,
            description = "Polished cap-toe Oxford shoes made from premium calfskin leather, designed for business suits and black-tie galas.",
            specifications = mapOf(
                "Upper" to "Supple Calf Leather",
                "Outsole" to "Non-slip stitched rubber sole",
                "Lining" to "Breathable leather lining"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("Rich Burgundy", "Classic Black"),
            sizes = listOf("40", "41", "42", "43", "44")
        ),
        Product(
            id = "p_shoe_4",
            name = "Adidas Ultraboost Light Running Shoes",
            brand = "Adidas",
            categoryId = "cat_shoes",
            categoryName = "Shoes",
            price = 39990.0,
            originalPrice = 46000.0,
            rating = 4.8f,
            reviewCount = 110,
            stock = 14,
            description = "Our lightest Ultraboost ever made with 30% lighter Light BOOST material and Continental Better Rubber outsole.",
            specifications = mapOf(
                "Midsole" to "Light BOOST cushioning with LEP System",
                "Upper" to "Adidas PRIMEKNIT+ textile",
                "Weight" to "293 grams (size UK 8.5)"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("Core Black", "Cloud White", "Solar Red"),
            sizes = listOf("UK 7", "UK 8", "UK 9", "UK 10")
        ),
        Product(
            id = "p_shoe_5",
            name = "Hush Puppies Classic Suede Penny Loafers",
            brand = "Hush Puppies",
            categoryId = "cat_shoes",
            categoryName = "Shoes",
            price = 15500.0,
            originalPrice = 19000.0,
            rating = 4.6f,
            reviewCount = 74,
            stock = 16,
            description = "Effortless slip-on loafers crafted with WorryFree water-resistant suede and Bounce Plus high-rebound footbed.",
            specifications = mapOf(
                "Material" to "Water-repellent genuine suede",
                "Comfort" to "Bounce Plus footbed with EVA foam",
                "Style" to "Casual Slip-On"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("Navy Suede", "Camel Brown", "Grey"),
            sizes = listOf("8", "9", "10", "11")
        ),

        // WATCHES (30-34)
        Product(
            id = "p_wat_1",
            name = "Apple Watch Series 9 GPS 45mm (Midnight)",
            brand = "Apple",
            categoryId = "cat_watches",
            categoryName = "Watches",
            price = 129000.0,
            originalPrice = 148000.0,
            rating = 4.9f,
            reviewCount = 165,
            stock = 15,
            description = "S9 SiP with Double Tap gesture, 2000 nits Always-On Retina display, ECG app, Crash Detection, and fast magnetic charging.",
            specifications = mapOf(
                "Case" to "45mm Midnight Aluminum Case",
                "Brightness" to "Up to 2,000 nits (1 nit min)",
                "Water Resistance" to "50 meters swimproof"
            ),
            imageUrls = listOf("banner_fashion", "banner_mega_sale"),
            colors = listOf("Midnight", "Starlight", "Silver", "(PRODUCT)RED"),
            sizes = listOf("41mm", "45mm"),
            isFeatured = true,
            isFlashSale = true
        ),
        Product(
            id = "p_wat_2",
            name = "Casio G-Shock GA-2100 Carbon Core 'CasiOak'",
            brand = "Casio",
            categoryId = "cat_watches",
            categoryName = "Watches",
            price = 32000.0,
            originalPrice = 38000.0,
            rating = 4.8f,
            reviewCount = 210,
            stock = 30,
            description = "Iconic octagonal bezel with slim 11.8mm profile, carbon fiber reinforced resin case, and 200-meter water resistance.",
            specifications = mapOf(
                "Shock Resistance" to "Carbon Core Guard Structure",
                "Water Resistance" to "200-meter / 20 bar",
                "Functions" to "World Time 31 zones, 1/100s stopwatch, 5 alarms"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("All Black Stealth", "Black & White", "Navy Blue"),
            sizes = listOf("One Size"),
            isBestSeller = true
        ),
        Product(
            id = "p_wat_3",
            name = "Seiko 5 Sports Automatic Stainless Steel Watch",
            brand = "Seiko",
            categoryId = "cat_watches",
            categoryName = "Watches",
            price = 85000.0,
            originalPrice = 98000.0,
            rating = 4.9f,
            reviewCount = 94,
            stock = 12,
            description = "Japanese Calibre 4R36 automatic movement with 41-hour power reserve, Hardlex crystal, unidirectional rotating bezel, and LumiBrite hands.",
            specifications = mapOf(
                "Movement" to "Automatic with manual winding capability",
                "Case Diameter" to "42.5mm Stainless Steel",
                "Dial" to "Sunburst Blue with Day/Date calendar"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("Sunburst Blue", "Classic Black", "Emerald Green"),
            sizes = listOf("42.5mm"),
            isTrending = true
        ),
        Product(
            id = "p_wat_4",
            name = "Samsung Galaxy Watch 6 Classic 47mm LTE",
            brand = "Samsung",
            categoryId = "cat_watches",
            categoryName = "Watches",
            price = 92000.0,
            originalPrice = 110000.0,
            rating = 4.7f,
            reviewCount = 76,
            stock = 14,
            description = "Return of the iconic rotating physical bezel, Sapphire Crystal glass, Body Composition BIA analysis, and sleep coaching.",
            specifications = mapOf(
                "Display" to "1.5\" Super AMOLED (480x480)",
                "Material" to "Stainless Steel with Sapphire Glass",
                "Sensors" to "ECG, Blood Pressure, BIA, Optical Heart Rate"
            ),
            imageUrls = listOf("banner_electronics"),
            colors = listOf("Silver", "Black"),
            sizes = listOf("43mm", "47mm")
        ),
        Product(
            id = "p_wat_5",
            name = "Fossil Grant Chronograph Leather Watch",
            brand = "Fossil",
            categoryId = "cat_watches",
            categoryName = "Watches",
            price = 39900.0,
            originalPrice = 48000.0,
            rating = 4.6f,
            reviewCount = 58,
            stock = 20,
            description = "Vintage-inspired Roman numeral dial with three-hand chronograph stopwatch sub-dials and rich interchangeable brown leather band.",
            specifications = mapOf(
                "Case Size" to "44mm Stainless Steel",
                "Strap" to "22mm Genuine Leather Brown",
                "Water Resistance" to "5 ATM"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("Brown Leather / Cream Dial", "Black Leather / Black Dial"),
            sizes = listOf("44mm")
        ),

        // BEAUTY & SKINCARE (35-39)
        Product(
            id = "p_beau_1",
            name = "The Ordinary Niacinamide 10% + Zinc 1% (60ml)",
            brand = "The Ordinary",
            categoryId = "cat_beauty",
            categoryName = "Beauty",
            price = 4250.0,
            originalPrice = 5500.0,
            rating = 4.8f,
            reviewCount = 420,
            stock = 60,
            description = "High-strength vitamin and mineral blemish formula that visibly targets uneven tone, textural irregularities, and controls excess sebum.",
            specifications = mapOf(
                "Volume" to "60ml Jumbo Size",
                "Key Actives" to "10% Niacinamide, 1% Zinc PCA",
                "Skin Type" to "All skin types, oily and blemish-prone"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("Standard"),
            sizes = listOf("30ml", "60ml"),
            isFeatured = true,
            isBestSeller = true
        ),
        Product(
            id = "p_beau_2",
            name = "CeraVe Hydrating Facial Cleanser for Normal to Dry Skin (473ml)",
            brand = "CeraVe",
            categoryId = "cat_beauty",
            categoryName = "Beauty",
            price = 6500.0,
            originalPrice = 7800.0,
            rating = 4.9f,
            reviewCount = 310,
            stock = 45,
            description = "Dermatologist recommended daily face wash with 3 essential ceramides, hyaluronic acid, and MVE technology to restore the skin barrier.",
            specifications = mapOf(
                "Volume" to "473ml (16 oz)",
                "Formula" to "Non-foaming lotion cleanser",
                "Certification" to "National Eczema Association accepted"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("Standard"),
            sizes = listOf("237ml", "473ml")
        ),
        Product(
            id = "p_beau_3",
            name = "La Roche-Posay Anthelios UVMune 400 Invisible Fluid SPF50+",
            brand = "La Roche-Posay",
            categoryId = "cat_beauty",
            categoryName = "Beauty",
            price = 7800.0,
            originalPrice = 9500.0,
            rating = 4.9f,
            reviewCount = 185,
            stock = 30,
            description = "Ultimate UV protection with revolutionary Mexoryl 400 filter against ultra-long UVA rays. Lightweight, invisible finish with zero white cast.",
            specifications = mapOf(
                "SPF" to "SPF 50+ Broad Spectrum",
                "Texture" to "Ultra-fluid non-greasy non-sticky",
                "Resistance" to "Water, sweat, and sand resistant"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("Invisible Tint-Free"),
            sizes = listOf("50ml"),
            isTrending = true
        ),
        Product(
            id = "p_beau_4",
            name = "Dior Sauvage Eau De Parfum (100ml)",
            brand = "Dior",
            categoryId = "cat_beauty",
            categoryName = "Beauty",
            price = 46000.0,
            originalPrice = 52000.0,
            rating = 4.9f,
            reviewCount = 190,
            stock = 15,
            description = "Exudes sensual and mysterious facets. Calabrian bergamot gives way to spicy Sichuan pepper and amber Papua New Guinean vanilla absolute.",
            specifications = mapOf(
                "Fragrance Type" to "Eau de Parfum (EDP)",
                "Top Notes" to "Reggio Bergamot",
                "Base Notes" to "Papua New Guinean Vanilla, Ambroxan"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("Original"),
            sizes = listOf("60ml", "100ml", "200ml")
        ),
        Product(
            id = "p_beau_5",
            name = "Huda Beauty Liquid Matte Ultra-Comfort Lipstick",
            brand = "Huda Beauty",
            categoryId = "cat_beauty",
            categoryName = "Beauty",
            price = 7950.0,
            originalPrice = 9500.0,
            rating = 4.7f,
            reviewCount = 145,
            stock = 25,
            description = "Transfer-proof, 8-hour wear liquid lipstick infused with L.O.V.E. complex (lycopene, olive oil, vitamins A&E) to keep lips hydrated.",
            specifications = mapOf(
                "Finish" to "Velvet Matte Transfer-Proof",
                "Wear Time" to "Up to 8 hours",
                "Weight" to "4.2ml"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("Bombshell", "Icon", "Trophy Wife", "Venus"),
            sizes = listOf("Full Size")
        ),

        // HOME & LIVING (40-44)
        Product(
            id = "p_home_1",
            name = "Philips XXL Digital Airfryer with Rapid Air Tech (7.2L)",
            brand = "Philips",
            categoryId = "cat_home",
            categoryName = "Home & Living",
            price = 48500.0,
            originalPrice = 58000.0,
            rating = 4.8f,
            reviewCount = 175,
            stock = 22,
            description = "Fry with up to 90% less fat using Rapid Air technology. 16-in-1 cooking functions with digital touch presets and QuickClean basket.",
            specifications = mapOf(
                "Capacity" to "7.2 Liters (1.4kg fries capacity)",
                "Power" to "2000 Watts",
                "Functions" to "Fry, bake, grill, roast, dehydrate, reheat"
            ),
            imageUrls = listOf("banner_mega_sale", "banner_electronics"),
            colors = listOf("Black & Copper"),
            sizes = listOf("XXL 7.2L"),
            isFeatured = true,
            isFlashSale = true
        ),
        Product(
            id = "p_home_2",
            name = "Anex Deluxe Cordless Stainless Steel Electric Kettle 1.7L",
            brand = "Anex",
            categoryId = "cat_home",
            categoryName = "Home & Living",
            price = 6800.0,
            originalPrice = 8500.0,
            rating = 4.6f,
            reviewCount = 230,
            stock = 40,
            description = "Food grade 304 stainless steel interior with auto shut-off, boil-dry protection, 360-degree swivel base, and LED indicator.",
            specifications = mapOf(
                "Capacity" to "1.7 Liters",
                "Power" to "2200W Rapid Boil",
                "Safety" to "Strix Controller Auto Shut-off"
            ),
            imageUrls = listOf("banner_mega_sale"),
            colors = listOf("Brushed Silver", "Matte Black"),
            sizes = listOf("1.7L"),
            isBestSeller = true
        ),
        Product(
            id = "p_home_3",
            name = "Diamond Supreme Foam Cool Gel Memory Foam Pillow",
            brand = "Diamond Foam",
            categoryId = "cat_home",
            categoryName = "Home & Living",
            price = 5400.0,
            originalPrice = 7000.0,
            rating = 4.7f,
            reviewCount = 140,
            stock = 35,
            description = "Ergonomic contour memory foam infused with cooling gel beads to relieve neck and shoulder pressure during sleep.",
            specifications = mapOf(
                "Dimensions" to "24\" x 16\" x 5\"",
                "Core" to "Orthopedic Gel-Infused Memory Foam",
                "Cover" to "Removable breathable bamboo jacquard cover"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("Cool Blue / White"),
            sizes = listOf("Standard", "King")
        ),
        Product(
            id = "p_home_4",
            name = "Ergonomic Mesh High-Back Executive Office Chair",
            brand = "ComfortPlus",
            categoryId = "cat_home",
            categoryName = "Home & Living",
            price = 32000.0,
            originalPrice = 39000.0,
            rating = 4.8f,
            reviewCount = 88,
            stock = 15,
            description = "Full mesh ergonomic chair with 3D adjustable armrests, dynamic lumbar support, tilt-lock mechanism, and heavy-duty nylon wheelbase.",
            specifications = mapOf(
                "Weight Capacity" to "150 kg",
                "Adjustability" to "Height, 3D arms, 135° recline, lumbar",
                "Base" to "Class 4 gas lift with smooth PU casters"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("All Black", "Grey & White Frame"),
            sizes = listOf("Universal Fit")
        ),
        Product(
            id = "p_home_5",
            name = "Xiaomi Mi Robot Vacuum-Mop 2 Pro (LDS Laser)",
            brand = "Xiaomi",
            categoryId = "cat_home",
            categoryName = "Home & Living",
            price = 98000.0,
            originalPrice = 115000.0,
            rating = 4.7f,
            reviewCount = 62,
            stock = 10,
            description = "High-frequency sonic vibration mopping with 3000Pa powerful suction, next-gen LDS navigation, and Mi Home app mapping.",
            specifications = mapOf(
                "Suction Power" to "3000 Pa",
                "Navigation" to "LDS Laser mapping with multi-floor support",
                "Battery" to "5200 mAh (up to 180 min runtime)"
            ),
            imageUrls = listOf("banner_electronics"),
            colors = listOf("Pearl White", "Black"),
            sizes = listOf("Standard")
        ),

        // GROCERIES (45-49)
        Product(
            id = "p_groc_1",
            name = "Guard Supreme Extra Long Grain Basmati Rice (5kg Bag)",
            brand = "Guard",
            categoryId = "cat_groceries",
            categoryName = "Groceries",
            price = 2850.0,
            originalPrice = 3400.0,
            rating = 4.9f,
            reviewCount = 510,
            stock = 100,
            description = "Aged naturally for exceptional aroma and elongation up to 2.5x after cooking. The premier choice for biryani and pulao.",
            specifications = mapOf(
                "Weight" to "5 Kilograms",
                "Grain" to "Extra Long Grain Basmati 1121",
                "Packaging" to "Heavy duty zipper bag"
            ),
            imageUrls = listOf("banner_mega_sale"),
            colors = listOf("Natural"),
            sizes = listOf("5kg", "10kg"),
            isBestSeller = true
        ),
        Product(
            id = "p_groc_2",
            name = "Borges Extra Virgin Olive Oil Cold Extracted (1 Liter)",
            brand = "Borges",
            categoryId = "cat_groceries",
            categoryName = "Groceries",
            price = 4950.0,
            originalPrice = 5800.0,
            rating = 4.8f,
            reviewCount = 240,
            stock = 60,
            description = "Produced from carefully selected premium olives in Spain through first cold pressing for dressing, dipping, and gentle sautéing.",
            specifications = mapOf(
                "Volume" to "1 Liter Glass Bottle",
                "Extraction" to "First Cold Pressed Extra Virgin",
                "Acidity" to "Maximum 0.5%"
            ),
            imageUrls = listOf("banner_mega_sale"),
            colors = listOf("Golden Olive"),
            sizes = listOf("500ml", "1L")
        ),
        Product(
            id = "p_groc_3",
            name = "Tapal Danedar Black Tea Economy Pack (900g)",
            brand = "Tapal",
            categoryId = "cat_groceries",
            categoryName = "Groceries",
            price = 1650.0,
            originalPrice = 1850.0,
            rating = 4.9f,
            reviewCount = 890,
            stock = 150,
            description = "Pakistan's favorite tea blend crafted from selected high-grown CTC tea leaves for strong golden color and rich aromatic flavor.",
            specifications = mapOf(
                "Weight" to "900 Grams",
                "Type" to "Granular Danedar CTC Black Tea",
                "Origin" to "Kenya & Ceylon blend"
            ),
            imageUrls = listOf("banner_mega_sale"),
            colors = listOf("Standard"),
            sizes = listOf("450g", "900g")
        ),
        Product(
            id = "p_groc_4",
            name = "Hunza Organic Mountain Raw Forest Honey (500g Jar)",
            brand = "Hunza Natural",
            categoryId = "cat_groceries",
            categoryName = "Groceries",
            price = 2400.0,
            originalPrice = 2950.0,
            rating = 4.8f,
            reviewCount = 135,
            stock = 45,
            description = "100% pure unfiltered sidr berry wildflower honey harvested from the pristine high valleys of Gilgit-Baltistan.",
            specifications = mapOf(
                "Net Weight" to "500 Grams Glass Jar",
                "Purity" to "Unpasteurized Raw Non-GMO Honey",
                "Source" to "Hunza Valley Flora"
            ),
            imageUrls = listOf("banner_mega_sale"),
            colors = listOf("Amber Gold"),
            sizes = listOf("250g", "500g", "1kg")
        ),
        Product(
            id = "p_groc_5",
            name = "Premium California Raw Whole Almonds Badam (500g)",
            brand = "DryFruit Hub",
            categoryId = "cat_groceries",
            categoryName = "Groceries",
            price = 1950.0,
            originalPrice = 2400.0,
            rating = 4.7f,
            reviewCount = 180,
            stock = 70,
            description = "Crisp, sweet, non-pareil whole almonds packed with healthy fats, vitamin E, and plant-based protein in vacuum sealed pouch.",
            specifications = mapOf(
                "Weight" to "500 Grams",
                "Grade" to "Grade A Extra No. 1 Non-pareil",
                "Nutrients" to "High in magnesium, antioxidant vitamin E"
            ),
            imageUrls = listOf("banner_mega_sale"),
            colors = listOf("Natural"),
            sizes = listOf("250g", "500g", "1kg")
        ),

        // SPORTS & FITNESS (50-54)
        Product(
            id = "p_sport_1",
            name = "CA Plus 15000 Player Edition English Willow Cricket Bat",
            brand = "CA Sports",
            categoryId = "cat_sports",
            categoryName = "Sports",
            price = 34500.0,
            originalPrice = 42000.0,
            rating = 4.9f,
            reviewCount = 155,
            stock = 14,
            description = "Handcrafted from selected Grade 1 English Willow with massive sweet spot, 8-10 straight clean grains, and fitted toe guard.",
            specifications = mapOf(
                "Willow" to "Grade 1 Super English Willow",
                "Weight" to "2 lbs 8 oz - 2 lbs 10 oz",
                "Handle" to "12-piece cane handle with rubber grip",
                "Endorsement" to "Used by international test cricketers"
            ),
            imageUrls = listOf("banner_fashion", "banner_mega_sale"),
            colors = listOf("Natural Willow"),
            sizes = listOf("Short Handle (SH)"),
            isFeatured = true,
            isFlashSale = true
        ),
        Product(
            id = "p_sport_2",
            name = "Adidas FIFA World Cup Pro Official Match Football",
            brand = "Adidas",
            categoryId = "cat_sports",
            categoryName = "Sports",
            price = 28000.0,
            originalPrice = 34000.0,
            rating = 4.8f,
            reviewCount = 92,
            stock = 18,
            description = "Thermally bonded seamless surface construction for more predictable trajectory, better touch, and low water uptake. FIFA Quality Pro certified.",
            specifications = mapOf(
                "Certification" to "FIFA Quality Pro Highest Rating",
                "Material" to "100% Polyurethane surface",
                "Bladder" to "High-grade butyl bladder for air retention"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("White / Blue / Orange Metallic"),
            sizes = listOf("Size 5 Official")
        ),
        Product(
            id = "p_sport_3",
            name = "Adjustable Quick-Select Dumbbell Pair (2.5kg to 24kg each)",
            brand = "ProIron",
            categoryId = "cat_sports",
            categoryName = "Sports",
            price = 45000.0,
            originalPrice = 54000.0,
            rating = 4.8f,
            reviewCount = 78,
            stock = 12,
            description = "Replaces 15 sets of dumbbells with a single turn of the dial from 2.5kg up to 24kg per hand. Includes storage cradle trays.",
            specifications = mapOf(
                "Weight Range" to "2.5kg to 24kg (5 to 52.5 lbs)",
                "Increments" to "15 weight settings",
                "Mechanism" to "Heavy duty anti-lock steel gear dial"
            ),
            imageUrls = listOf("banner_electronics"),
            colors = listOf("Black & Red"),
            sizes = listOf("Pair (48kg total)")
        ),
        Product(
            id = "p_sport_4",
            name = "Yonex Astrox 88D Pro Professional Badminton Racket",
            brand = "Yonex",
            categoryId = "cat_sports",
            categoryName = "Sports",
            price = 42000.0,
            originalPrice = 49000.0,
            rating = 4.9f,
            reviewCount = 65,
            stock = 10,
            description = "Made in Japan with Rotational Generator System and Namd graphite for lethal steep smashes and rapid attack from the backcourt.",
            specifications = mapOf(
                "Flex" to "Stiff",
                "Frame" to "HM Graphite, VOLUME CUT RESIN, Tungsten",
                "Weight / Grip" to "4U (Avg. 83g) G5"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("Camel Gold / Black"),
            sizes = listOf("4U-G5")
        ),
        Product(
            id = "p_sport_5",
            name = "Eco-Friendly 6mm Non-Slip TPE Alignment Yoga Mat",
            brand = "Liforme Style",
            categoryId = "cat_sports",
            categoryName = "Sports",
            price = 4950.0,
            originalPrice = 6500.0,
            rating = 4.7f,
            reviewCount = 120,
            stock = 30,
            description = "Double-layer dual-color non-slip textured surface with body alignment guidance lines. Includes carrying strap.",
            specifications = mapOf(
                "Thickness" to "6mm high-density cushioning",
                "Dimensions" to "72\" x 24\" (183cm x 61cm)",
                "Material" to "Non-toxic biodegradable TPE"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("Ocean Teal", "Deep Violet", "Charcoal"),
            sizes = listOf("Standard 6mm")
        ),

        // TOYS & KIDS (55-59)
        Product(
            id = "p_toy_1",
            name = "LEGO Icons Porsche 911 2-in-1 Turbo & Targa (1458 pcs)",
            brand = "LEGO",
            categoryId = "cat_toys",
            categoryName = "Toys",
            price = 49500.0,
            originalPrice = 58000.0,
            rating = 4.9f,
            reviewCount = 110,
            stock = 8,
            description = "Build an icon of motoring history with working steering, gearshift, emergency brake, tilting seats, and rear-mounted air-cooled boxer engine.",
            specifications = mapOf(
                "Pieces" to "1,458 Pieces",
                "Age Group" to "18+ Adult Collectors",
                "Model" to "Build either Turbo or Targa version"
            ),
            imageUrls = listOf("banner_mega_sale"),
            colors = listOf("Classic Alpine White"),
            sizes = listOf("1458 Pieces"),
            isFeatured = true
        ),
        Product(
            id = "p_toy_2",
            name = "High-Speed 4WD RC Rock Crawler Off-Road Monster Truck",
            brand = "HyperGo",
            categoryId = "cat_toys",
            categoryName = "Toys",
            price = 14500.0,
            originalPrice = 18500.0,
            rating = 4.7f,
            reviewCount = 145,
            stock = 25,
            description = "Brushless motor delivering up to 45 km/h with oil-filled metal shocks, 2.4GHz remote control, and dual rechargeable Li-Po battery packs.",
            specifications = mapOf(
                "Scale" to "1:14 Scale All-Terrain 4WD",
                "Top Speed" to "45 km/h",
                "Playtime" to "40 minutes with 2 batteries"
            ),
            imageUrls = listOf("banner_electronics"),
            colors = listOf("Flame Orange", "Electric Blue"),
            sizes = listOf("1:14 Scale")
        ),
        Product(
            id = "p_toy_3",
            name = "12-in-1 Solar Powered STEM Robot Educational Building Kit",
            brand = "Discovery Kids",
            categoryId = "cat_toys",
            categoryName = "Toys",
            price = 3850.0,
            originalPrice = 5000.0,
            rating = 4.6f,
            reviewCount = 95,
            stock = 40,
            description = "Teaches green energy and robotics by building 12 different walking and crawling robots powered directly by sunlight. No batteries required.",
            specifications = mapOf(
                "Pieces" to "190 Building Components",
                "Power" to "Real Solar Cell Included",
                "Age" to "8 - 14 Years"
            ),
            imageUrls = listOf("banner_electronics"),
            colors = listOf("Grey & Yellow"),
            sizes = listOf("Standard")
        ),
        Product(
            id = "p_toy_4",
            name = "Giant 100cm Cuddle Soft Plush Teddy Bear",
            brand = "Hamleys",
            categoryId = "cat_toys",
            categoryName = "Toys",
            price = 6800.0,
            originalPrice = 8500.0,
            rating = 4.8f,
            reviewCount = 160,
            stock = 20,
            description = "Ultra-soft premium huggable plush fur with cute embroidered paws and satin bow ribbon. Hypoallergenic PP cotton filling.",
            specifications = mapOf(
                "Height" to "100 cm (Over 3 Feet tall)",
                "Material" to "High-pile hypoallergenic plush",
                "Safety" to "Child safe embroidered features"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("Golden Honey", "Milky White", "Chocolate Brown"),
            sizes = listOf("100cm")
        ),
        Product(
            id = "p_toy_5",
            name = "Magnetic 3D Building Tiles 100-Piece Creative Play Set",
            brand = "Magformers",
            categoryId = "cat_toys",
            categoryName = "Toys",
            price = 5900.0,
            originalPrice = 7500.0,
            rating = 4.9f,
            reviewCount = 180,
            stock = 35,
            description = "Translucent rainbow magnetic geometric tiles that click together effortlessly for engineering 3D castles, rockets, and vehicles.",
            specifications = mapOf(
                "Pieces" to "100 Clear Color Geometric Tiles",
                "Safety" to "BPA-Free ultrasonically welded with rivets",
                "Certification" to "EN71 & ASTM Certified"
            ),
            imageUrls = listOf("banner_mega_sale"),
            colors = listOf("Rainbow Translucent"),
            sizes = listOf("100 Pieces")
        ),

        // ACCESSORIES & GADGETS (60-64)
        Product(
            id = "p_acc_1",
            name = "Anker 737 Power Bank PowerCore 24K (140W Two-Way)",
            brand = "Anker",
            categoryId = "cat_accessories",
            categoryName = "Accessories",
            price = 28500.0,
            originalPrice = 34000.0,
            rating = 4.9f,
            reviewCount = 215,
            stock = 18,
            description = "Ultra-powerful 140W fast charging with GaNPrime technology, smart digital color display, and 24,000mAh capacity to fast charge laptops and phones.",
            specifications = mapOf(
                "Capacity" to "24,000 mAh",
                "Max Output" to "140W USB-C Power Delivery 3.1",
                "Screen" to "Smart digital display shows power & recharge time"
            ),
            imageUrls = listOf("banner_electronics", "banner_mega_sale"),
            colors = listOf("Black & Silver"),
            sizes = listOf("24,000mAh"),
            isFeatured = true,
            isFlashSale = true
        ),
        Product(
            id = "p_acc_2",
            name = "Ray-Ban Classic Aviator Polarized Sunglasses (RB3025)",
            brand = "Ray-Ban",
            categoryId = "cat_accessories",
            categoryName = "Accessories",
            price = 36000.0,
            originalPrice = 42000.0,
            rating = 4.9f,
            reviewCount = 170,
            stock = 14,
            description = "Timeless gold metal teardrop frame with polarized G-15 crystal green lenses providing 100% UV protection and glare reduction.",
            specifications = mapOf(
                "Frame" to "Arista Gold Metal Wire",
                "Lens" to "G-15 Polarized Green Crystal Glass",
                "Protection" to "100% UVA & UVB Ray Filtering"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("Gold / G-15 Green", "Gunmetal / Polarized Grey"),
            sizes = listOf("Standard 58mm")
        ),
        Product(
            id = "p_acc_3",
            name = "Jafferjees Full Grain Leather Executive Bi-Fold Wallet",
            brand = "Jafferjees",
            categoryId = "cat_accessories",
            categoryName = "Accessories",
            price = 6800.0,
            originalPrice = 8500.0,
            rating = 4.8f,
            reviewCount = 195,
            stock = 40,
            description = "Handmade from Pakistan's most revered leather house with 8 card slots, two cash compartments, RFID blocking lining, and gift box.",
            specifications = mapOf(
                "Leather" to "100% Genuine Full Grain Cowhide",
                "Slots" to "8 Credit Card Slots, 2 Slip Pockets, 2 Billfolds",
                "Protection" to "Certified RFID Shielding"
            ),
            imageUrls = listOf("banner_fashion"),
            colors = listOf("Espresso Brown", "Midnight Black", "Tan"),
            sizes = listOf("Standard Bi-Fold"),
            isBestSeller = true
        ),
        Product(
            id = "p_acc_4",
            name = "Mark Ryden Water-Resistant Anti-Theft Laptop Backpack (17.3\")",
            brand = "Mark Ryden",
            categoryId = "cat_accessories",
            categoryName = "Accessories",
            price = 11500.0,
            originalPrice = 14500.0,
            rating = 4.7f,
            reviewCount = 135,
            stock = 26,
            description = "TSA approved lock, external USB-A & USB-C charging ports, waterproof Oxford fabric, and padded 17.3-inch laptop shock compartment.",
            specifications = mapOf(
                "Capacity" to "28 - 35 Liters expandable",
                "Laptop Size" to "Up to 17.3-inch gaming laptops",
                "Security" to "Customs TSA combination lock + hidden back pocket"
            ),
            imageUrls = listOf("banner_electronics"),
            colors = listOf("Carbon Black", "Space Grey"),
            sizes = listOf("Expandable 35L")
        ),
        Product(
            id = "p_acc_5",
            name = "Baseus Magnetic Wireless 15W Fast Car Charger Mount",
            brand = "Baseus",
            categoryId = "cat_accessories",
            categoryName = "Accessories",
            price = 6200.0,
            originalPrice = 7900.0,
            rating = 4.7f,
            reviewCount = 110,
            stock = 32,
            description = "MagSafe compatible 16 powerful NdFeB magnets with 15W Qi fast charging and sturdy 360-degree ball joint air vent clamp.",
            specifications = mapOf(
                "Wireless Output" to "15W / 10W / 7.5W Qi Fast Charging",
                "Mount" to "Reinforced steel hook air vent clip",
                "Compatibility" to "iPhone 12/13/14/15 & MagSafe Cases"
            ),
            imageUrls = listOf("banner_electronics"),
            colors = listOf("Matte Black"),
            sizes = listOf("Standard")
        )
    )

    val sampleAddresses = listOf(
        Address(
            id = "addr_1",
            recipientName = "Bilal Khan",
            phoneNumber = "+92 300 1234567",
            city = "Lahore",
            area = "Gulberg III",
            streetAddress = "House 42, Block C, M.M. Alam Road",
            postalCode = "54660",
            isDefault = true,
            addressLabel = "Home"
        ),
        Address(
            id = "addr_2",
            recipientName = "Bilal Khan (Office)",
            phoneNumber = "+92 321 9876543",
            city = "Lahore",
            area = "DHA Phase 5",
            streetAddress = "Plaza 18, Commercial Broadway",
            postalCode = "54792",
            isDefault = false,
            addressLabel = "Work"
        ),
        Address(
            id = "addr_3",
            recipientName = "Hazrat Bilal",
            phoneNumber = "+92 333 4567890",
            city = "Islamabad",
            area = "Sector F-7/2",
            streetAddress = "Street 14, House 9",
            postalCode = "44000",
            isDefault = false,
            addressLabel = "Other"
        )
    )

    val sampleOrders = listOf(
        Order(
            id = "ord_101",
            orderNumber = "BZ-2026-89412",
            items = listOf(
                CartItem(
                    id = "ci_1",
                    product = products[0], // Samsung S24 Ultra
                    quantity = 1,
                    selectedColor = "Titanium Black",
                    selectedSize = "256GB"
                )
            ),
            subtotal = 379999.0,
            deliveryFee = 0.0,
            discountAmount = 10000.0,
            totalAmount = 369999.0,
            status = OrderStatus.SHIPPED,
            deliveryAddress = sampleAddresses[0],
            paymentMethod = "Online Card Payment (MasterCard **** 4819)",
            orderDate = "Oct 12, 2026 14:30",
            estimatedDeliveryDate = "Oct 15, 2026",
            trackingEvents = listOf(
                TrackingEvent("Order Placed", "Your order was successfully recorded.", "Oct 12, 14:30", true),
                TrackingEvent("Order Confirmed", "Seller verified payment and confirmed stock.", "Oct 12, 15:15", true),
                TrackingEvent("Packed & QC Passed", "Item packed securely in Bazaar Tamper-Evident Bag.", "Oct 13, 09:20", true),
                TrackingEvent("Dispatched with Courier", "Package handed over to TCS Express (Tracking: 78942189).", "Oct 13, 16:45", true),
                TrackingEvent("Out for Delivery", "Rider assigned for delivery to your doorstep.", "Expected Oct 15", false),
                TrackingEvent("Delivered", "Delivered to recipient.", "Pending", false)
            )
        ),
        Order(
            id = "ord_102",
            orderNumber = "BZ-2026-78320",
            items = listOf(
                CartItem(
                    id = "ci_2",
                    product = products[18], // J. Kurta
                    quantity = 2,
                    selectedColor = "Royal Navy",
                    selectedSize = "L"
                ),
                CartItem(
                    id = "ci_3",
                    product = products[25], // Chappal
                    quantity = 1,
                    selectedColor = "Mustard Tan",
                    selectedSize = "9"
                )
            ),
            subtotal = 20830.0,
            deliveryFee = 0.0,
            discountAmount = 1500.0,
            totalAmount = 19330.0,
            status = OrderStatus.DELIVERED,
            deliveryAddress = sampleAddresses[0],
            paymentMethod = "Cash on Delivery (COD)",
            orderDate = "Oct 05, 2026 11:20",
            estimatedDeliveryDate = "Oct 08, 2026",
            trackingEvents = listOf(
                TrackingEvent("Order Placed", "Order placed via Bazaar App.", "Oct 05, 11:20", true),
                TrackingEvent("Confirmed", "Order approved by merchant.", "Oct 05, 12:00", true),
                TrackingEvent("Dispatched", "In transit via Trax Logistics.", "Oct 06, 14:30", true),
                TrackingEvent("Delivered", "Signed and received by Bilal Khan.", "Oct 08, 13:40", true)
            )
        )
    )

    val sampleReviews = listOf(
        Review(
            productId = "p_mob_1",
            userName = "Hamza Farooq",
            rating = 5,
            title = "Absolute beast of a phone!",
            comment = "Received within 48 hours in original sealed box. PTA was already active. Galaxy AI features and the anti-reflective glass screen are incredible. 10/10 service from Bazaar Mall!",
            date = "Oct 10, 2026",
            helpfulCount = 42
        ),
        Review(
            productId = "p_mob_1",
            userName = "Dr. Ayesha Malik",
            rating = 5,
            title = "Top notch camera and battery",
            comment = "Upgraded from S21 Ultra. The battery easily lasts a full day and a half with 8 hours screen-on time. The flat display feels so much better to write with S-pen.",
            date = "Oct 07, 2026",
            helpfulCount = 19
        ),
        Review(
            productId = "p_fash_1",
            userName = "Usman Tariq",
            rating = 5,
            title = "J. Fabric never disappoints",
            comment = "Pure Egyptian cotton feels luxurious and cool. The collar embroidery is subtle and elegant. Size L fits perfectly.",
            date = "Sep 28, 2026",
            helpfulCount = 31
        ),
        Review(
            productId = "p_elec_1",
            userName = "Zainab Shah",
            rating = 5,
            title = "Silence like magic",
            comment = "Best headphones for studying and flights. The noise cancellation completely silences fan and traffic noises. Worth every rupee.",
            date = "Oct 02, 2026",
            helpfulCount = 27
        )
    )

    val sampleNotifications = listOf(
        NotificationItem(
            id = "notif_1",
            title = "Order Dispatched! 🚚",
            message = "Your order #BZ-2026-89412 is on its way with TCS Express. Expected delivery on Oct 15.",
            timestamp = "2 hours ago",
            type = NotificationType.ORDER_UPDATE,
            isRead = false
        ),
        NotificationItem(
            id = "notif_2",
            title = "Flash Sale Alert! ⚡ 70% OFF",
            message = "Mega Shopping Festival is live! Massive discounts on Laptops, Smartphones, and Festive Fashion.",
            timestamp = "5 hours ago",
            type = NotificationType.FLASH_SALE,
            isRead = false
        ),
        NotificationItem(
            id = "notif_3",
            title = "Price Drop on Saved Item! 📉",
            message = "Sony WH-1000XM5 just dropped by Rs. 17,000! Tap to grab before stock runs out.",
            timestamp = "1 day ago",
            type = NotificationType.PRICE_DROP,
            isRead = true
        ),
        NotificationItem(
            id = "notif_4",
            title = "Welcome Voucher Rs. 500 OFF 🎁",
            message = "Use code 'WELCOME500' on your first order over Rs. 3,000. Valid for 7 days.",
            timestamp = "2 days ago",
            type = NotificationType.PROMO_COUPON,
            isRead = true
        )
    )

    val currentUser = User(
        id = "usr_001",
        name = "Bilal Khan",
        email = "hazratbilalvlog@gmail.com",
        phoneNumber = "+92 300 1234567",
        avatarUrl = "",
        isGuest = false
    )
}
