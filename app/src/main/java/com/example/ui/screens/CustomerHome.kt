package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Brush
import com.example.R
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.AppViewModel
import com.example.data.Category
import com.example.data.Booking
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun CustomerAppShell(
    viewModel: AppViewModel,
    onNavigateToCategory: (String) -> Unit, // categoryId
    onNavigateToBookService: (String, Category) -> Unit, // serviceName, category
    onNavigateToRequestQuote: (Category) -> Unit, // category
    onNavigateToVendorProfile: (String) -> Unit, // vendorId
    onNavigateToEstimator: () -> Unit,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("home") }
    val user by viewModel.loggedInUser.collectAsStateWithLifecycle()
    val city by viewModel.selectedCity.collectAsStateWithLifecycle()

    var showLocationDialog by remember { mutableStateOf(false) }
    var showChatbot by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = White,
                tonalElevation = 8.dp
            ) {
                val tabs = listOf(
                    Triple("home", "Home", Icons.Default.Home),
                    Triple("services", "Services", Icons.Default.Construction),
                    Triple("marketplace", "Marketplace", Icons.Default.ShoppingCart),
                    Triple("bookings", "Bookings", Icons.Default.Assignment),
                    Triple("profile", "Profile", Icons.Default.Person)
                )

                tabs.forEach { (route, label, icon) ->
                    NavigationBarItem(
                        selected = selectedTab == route,
                        onClick = { selectedTab = route },
                        icon = { Icon(icon, contentDescription = label, modifier = Modifier.size(24.dp)) },
                        label = { Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Primary,
                            selectedTextColor = Primary,
                            indicatorColor = PrimaryPale,
                            unselectedIconColor = Slate,
                            unselectedTextColor = Slate
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showChatbot = true },
                containerColor = Primary,
                contentColor = White,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🤖", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Setu AI", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Paper)
        ) {
            when (selectedTab) {
                "home" -> HomeScreenView(
                    viewModel = viewModel,
                    userCity = city,
                    onChangeCityClick = { showLocationDialog = true },
                    onNavigateToCategory = onNavigateToCategory,
                    onNavigateToBookService = onNavigateToBookService,
                    onNavigateToEstimator = onNavigateToEstimator
                )
                "services" -> CategoriesScreenView(
                    viewModel = viewModel,
                    onCategoryTap = onNavigateToCategory
                )
                "marketplace" -> MarketplaceScreenView(
                    viewModel = viewModel,
                    onNavigateToQuote = {
                        val list = viewModel.categories.value
                        if (list.isNotEmpty()) {
                            onNavigateToRequestQuote(list[0])
                        }
                    }
                )
                "bookings" -> BookingsScreenView(
                    viewModel = viewModel
                )
                "profile" -> ProfileScreenView(
                    viewModel = viewModel,
                    onNavigateToEstimator = onNavigateToEstimator,
                    onLogoutClick = onLogout
                )
            }

            if (showLocationDialog) {
                LocationSwitcherDialog(
                    currentCity = city,
                    onCitySelected = {
                        viewModel.selectCity(it)
                        showLocationDialog = false
                    },
                    onDismiss = { showLocationDialog = false }
                )
            }

            if (showChatbot) {
                SetuAIChatbotDialog(
                    onDismiss = { showChatbot = false },
                    onDeepLinkToCategory = { catId ->
                        selectedTab = "services"
                        onNavigateToCategory(catId)
                        showChatbot = false
                    },
                    onDeepLinkToEstimator = {
                        onNavigateToEstimator()
                        showChatbot = false
                    },
                    onDeepLinkToMarketplace = { subTab ->
                        selectedTab = "marketplace"
                        showChatbot = false
                    }
                )
            }
        }
    }
}

// Extension to simulate easy coroutines launch on main viewModelScope
private fun kotlinx.coroutines.CoroutineScope.launchInVM(block: suspend kotlinx.coroutines.CoroutineScope.() -> Unit) {
    this.launchInVM(block)
}

@Composable
fun LocationSwitcherDialog(
    currentCity: String,
    onCitySelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val cities = listOf("Greater Noida", "Noida", "Ghaziabad", "Delhi", "Faridabad")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Operations City", fontWeight = FontWeight.Bold, color = DarkInk) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Abhiyantri Setu operates in select regions under the Delhi NCR cluster. Select a region to view specific professionals and local service rates.",
                    fontSize = 12.sp,
                    color = Slate,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                cities.forEach { city ->
                    val isSelected = city == currentCity
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCitySelected(city) }
                            .background(
                                color = if (isSelected) PrimaryPale else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = city,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Primary else DarkInk,
                            fontSize = 14.sp
                        )
                        if (isSelected) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Primary)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Slate, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = White,
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun HomeScreenView(
    viewModel: AppViewModel,
    userCity: String,
    onChangeCityClick: () -> Unit,
    onNavigateToCategory: (String) -> Unit,
    onNavigateToBookService: (String, Category) -> Unit,
    onNavigateToEstimator: () -> Unit
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    
    // Interactive Cost Estimator State directly on the landing page
    var sliderArea by remember { mutableFloatStateOf(1200f) }
    var sliderFloors by remember { mutableFloatStateOf(2f) }
    var premiumMaterials by remember { mutableStateOf(false) }
    
    val basePerSqFt = if (premiumMaterials) 2100 else 1650
    val costTotal = (sliderArea * sliderFloors * basePerSqFt).toLong()
    
    val cementBags = (sliderArea * sliderFloors * 0.40f).toLong()
    val steelTons = (sliderArea * sliderFloors * 0.0035f * 1.2f).toLong()
    val bricksEstimate = (sliderArea * sliderFloors * 18.5f).toLong()

    val projectsList = listOf(
        Pair("Home Construction & Renovation", "From excavation & footing layouts to RCC columns & paint finishes. Save 15% on bundle contracts."),
        Pair("Interior Design & Finishing", "Modular premium kitchens, false gypsum ceilings, custom wardrobes, and modular electrical integrations."),
        Pair("Engineering Consultancy", "Statutory structural designs, soil load-bearing logs, customized blueprints, & municipal clearance."),
        Pair("Structural Repair & Renovation", "Rebar polymer repairs, premium roof membrane polyurea waterproofing, and foundation cracks injections.")
    )

    var showBeforeState by remember { mutableStateOf(false) }

    GlassmorphicBackgroundColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // App glass-themed top navbar layout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.5f))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AbhiyantriSetuLogo(
                    showTagline = true,
                    modifier = Modifier.height(28.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Location selection pill
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.75f), RoundedCornerShape(100.dp))
                            .border(BorderStroke(1.dp, Primary.copy(alpha = 0.25f)), RoundedCornerShape(100.dp))
                            .clickable { onChangeCityClick() }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "location",
                            tint = Primary,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = userCity,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "dropdown",
                            tint = Primary,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(PrimaryPale),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            tint = Primary,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // Centralized fully responsive column
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            ) {
                
                // 1. HERO HEADER (Website style)
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(Primary.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "NORTH INDIA'S PRE-EMINENT CIVIL & ENGINEERING BRIDGE 🇮🇳",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    Text(
                        text = "Build, Renovate & Supply\nwith Absolute Vetted Trust",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DarkInk,
                        lineHeight = 30.sp
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Connecting developers, homebuilders, and families in Delhi-NCR with background-vetted contractors, structural load experts, heavy crane rentals, and raw materials supply in one single transparent escrow-safeguarded platform.",
                        fontSize = 12.sp,
                        color = Slate,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Transparent Search field
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search civil builders, raw bricks, cement price...", fontSize = 13.sp, color = Mist) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White.copy(alpha = 0.8f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.6f),
                            focusedBorderColor = Primary,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                        ),
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Slate) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = null, tint = Slate)
                                }
                            }
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Suggesion chips scrollable
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val quickActions = listOf(
                            Pair("🏗️ Build Structure", "home-construction"),
                            Pair("🚿 Book Plumber", "plumbing"),
                            Pair("🧱 Buy Bricks", "marketplace"),
                            Pair("🚜 Rent Crane", "marketplace"),
                            Pair("📝 Cost Estimate", "estimator")
                        )
                        items(quickActions) { item ->
                            val (label, action) = item
                            Box(
                                modifier = Modifier
                                    .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(100.dp))
                                    .border(BorderStroke(1.dp, Primary.copy(alpha = 0.2f)), RoundedCornerShape(100.dp))
                                    .clickable {
                                        if (action == "estimator") {
                                            onNavigateToEstimator()
                                        } else {
                                            onNavigateToCategory(action)
                                        }
                                    }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(text = label, color = Primary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Glass statistics row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("18.5k+ M³", fontWeight = FontWeight.Black, color = DarkInk2, fontSize = 14.sp)
                            Text("Concrete Transported", fontSize = 9.sp, color = Slate, textAlign = TextAlign.Center)
                        }
                        Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color.White.copy(alpha = 0.6f)))
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("150+ Vetted", fontWeight = FontWeight.Black, color = DarkInk2, fontSize = 14.sp)
                            Text("Licensed Engineers", fontSize = 9.sp, color = Slate, textAlign = TextAlign.Center)
                        }
                        Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color.White.copy(alpha = 0.6f)))
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("0% Fraud", fontWeight = FontWeight.Black, color = Emerald, fontSize = 14.sp)
                            Text("Escrow Record NCR", fontSize = 9.sp, color = Slate, textAlign = TextAlign.Center)
                        }
                    }
                }

                // 2. INTERACTIVE ESTIMATOR WEB HUB (Exactly matching custom estimator interface of lovable website)
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "📊 Live Estimator Panel",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkInk2
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(PrimaryPale, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Box(modifier = Modifier.size(6.dp).background(Emerald, CircleShape))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("DSR rates active", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Emerald)
                        }
                    }
                    
                    Text(
                        "Drag sliders to simulate layout dimensions and estimate structural materials cost instantaneously.",
                        fontSize = 11.sp,
                        color = Slate,
                        modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                    )

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Built-up Area", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate)
                            Text("${sliderArea.toInt()} Sq Ft", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Primary)
                        }
                        Slider(
                            value = sliderArea,
                            onValueChange = { sliderArea = it },
                            valueRange = 500f..5000f,
                            colors = SliderDefaults.colors(
                                thumbColor = Primary,
                                activeTrackColor = Primary,
                                inactiveTrackColor = PrimaryPale
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Contractor Floors", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate)
                            Text("${sliderFloors.toInt()} Floors", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Primary)
                        }
                        Slider(
                            value = sliderFloors,
                            onValueChange = { sliderFloors = it },
                            valueRange = 1f..5f,
                            steps = 3,
                            colors = SliderDefaults.colors(
                                thumbColor = Primary,
                                activeTrackColor = Primary,
                                inactiveTrackColor = PrimaryPale
                            )
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text("Premium Material Specs (+25%)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate)
                            Switch(
                                checked = premiumMaterials,
                                onCheckedChange = { premiumMaterials = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Primary, checkedTrackColor = PrimaryPale)
                            )
                        }

                        // Estimates grid output
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                                .border(BorderStroke(1.dp, Color.White), RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Estimated Total Bid:", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = DarkInk3)
                                    Text("₹${String.format("%,d", costTotal)}", fontSize = 15.sp, fontWeight = FontWeight.Black, color = Primary)
                                }
                                Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.White.copy(alpha = 0.5f))
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(horizontalAlignment = Alignment.Start) {
                                        Text("OPC Cement", fontSize = 10.sp, color = Slate)
                                        Text("~$cementBags Bags", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkInk2)
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("TMT Steel", fontSize = 10.sp, color = Slate)
                                        Text("~$steelTons Tons", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkInk2)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("Solid Bricks", fontSize = 10.sp, color = Slate)
                                        Text("~$bricksEstimate Pcs", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkInk2)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        AppButton(
                            label = "Save Live Estimate & Apply Quotes",
                            onTap = {
                                onNavigateToEstimator()
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // 3. PROMO BANNER (LAUNCH OFFER ₹99)
                Card(
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.2.dp, Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .padding(bottom = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.img_promo_banner),
                            contentDescription = "Special Promo Launch Campaign - ₹99 service",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                  .fillMaxSize()
                                  .background(
                                      Brush.verticalGradient(
                                          colors = listOf(Color.Transparent, DarkInk.copy(alpha = 0.85f))
                                      )
                                  )
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(Primary, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("LIMITED TIME LAUNCH PROMO", color = White, fontSize = 8.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Get your first service at ₹99 — limited time", color = White, fontSize = 14.sp, fontWeight = FontWeight.Black)
                            Text("Save big on electrical checkups, pipeline audits, or wood planning consultations this week only", color = Mist, fontSize = 10.sp)
                        }
                    }
                }

                // 3B. FEATURED VERIFIED PROFESSIONALS (With 80x80 px circular headshots)
                SectionHeader(
                    title = "Featured Verified Professionals",
                    subtitle = "Escrow-safeguarded Delhi builders with ISO certified skills",
                    onAction = {}
                )
                
                val verifiedPros = listOf(
                    Triple("Sharma Structural Contracts", "Building & RCC Spec", R.drawable.img_pro_avatar),
                    Triple("Gautam Plumbing NCR", "Sanitary Pipe Grid", R.drawable.img_civil_engineer),
                    Triple("Verma Electricals", "High-Tension Wiring", R.drawable.img_logo)
                )
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                ) {
                    items(verifiedPros) { (name, trade, avatarRes) ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f)),
                            border = BorderStroke(1.dp, Fog),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .width(170.dp)
                                .padding(vertical = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // 80x80 px circular headshot
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryPale),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = avatarRes),
                                        contentDescription = name,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = name,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 12.sp,
                                    color = DarkInk,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = trade,
                                    fontSize = 10.sp,
                                    color = Slate,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Gold,
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text("4.9", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = DarkInk)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .background(Emerald.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    ) {
                                        Text("ISO VETTED", color = Emerald, fontSize = 8.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                    }
                                }
                            }
                        }
                    }
                }
                
                // 4. POPULAR QUICK SERVICES (Urban Company / Lovable Website style list)
                SectionHeader(
                    title = "Popular Quick Services",
                    subtitle = "Book certified technicians in background-checked slots",
                    actionLabel = "View All",
                    onAction = { onNavigateToCategory("home-maintenance") }
                )
                
                val quickServices = listOf(
                    Triple("Electrician", "₹299", "30-60 mins"),
                    Triple("Plumber", "₹299", "60-90 mins"),
                    Triple("AC Servicing", "₹499", "60 mins"),
                    Triple("Deep Cleaning", "₹399", "120 mins"),
                    Triple("Carpenter", "₹399", "60-90 mins")
                )
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(quickServices) { (name, price, duration) ->
                        val matchedCategory = when (name) {
                            "Electrician" -> "electrical"
                            "Plumber" -> "plumbing"
                            "AC Servicing" -> "ac-hvac"
                            "Deep Cleaning" -> "home-maintenance"
                            else -> "home-maintenance"
                        }
                        val currentCat = categories.find { it.id == matchedCategory } ?: Category(
                            id = matchedCategory,
                            name = "Home Maintenance",
                            icon = "🔧",
                            description = "",
                            subServices = ""
                        )
                        
                        // Render glassmorphic card for quick service
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.70f)),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.2.dp, Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                            modifier = Modifier
                                .width(155.dp)
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .background(PrimaryPale, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("⚡", fontSize = 18.sp)
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkInk, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text("Starts $price", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Primary)
                                Text(duration, fontSize = 10.sp, color = Slate)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Primary, RoundedCornerShape(6.dp))
                                        .clickable { onNavigateToBookService(name, currentCat) }
                                        .padding(vertical = 6.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Book", color = White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 5. ROADMAP STRUCTURAL PROJECTS (Delhi NCR Contracts)
                SectionHeader(
                    title = "Construction Projects & Contracts",
                    subtitle = "End-to-end engineered corporate & villa structures"
                )

                projectsList.forEach { (title, description) ->
                    val associatedCatId = when {
                        title.contains("Interior") -> "interior-finishing"
                        title.contains("Engineering") -> "engineering-consultancy"
                        else -> "home-construction"
                    }
                    
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable { onNavigateToCategory(associatedCatId) }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⚡", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkInk2
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = description,
                            fontSize = 11.sp,
                            color = Slate,
                            lineHeight = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 6. SPOT MATERIALS & MACHINERY RATINGS INDEX
                SectionHeader(
                    title = "Spot Materials & Heavy Machinery Index",
                    subtitle = "Noida NCR cluster wholesale dispatch tracker"
                )

                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Live Marketplace Rates",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkInk
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val materialsArray = listOf(
                        Triple("OPC Solid Cement (Ultratech)", "₹385 / Bag", "↑ 0.5%"),
                        Triple("Yamuna River Coarse Sand", "₹72 / Cubic Ft", "↓ 1.2%"),
                        Triple("Tata Tiscon Steel Rebar (TMT)", "₹62,500 / Ton", "↑ 2.1%"),
                        Triple("JCB Loader rental (Diesel)", "₹11,500 / Day", "0.0%"),
                        Triple("Soil Compactor Roller", "₹4,800 / Day", "↑ 0.8%")
                    )

                    materialsArray.forEach { (mat, rate, change) ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Text(mat, fontSize = 11.sp, color = DarkInk3, modifier = Modifier.weight(1f))
                            Text(rate, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkInk2)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                change,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (change.contains("↑")) Emerald else if (change.contains("↓")) Color.Red else Slate
                            )
                        }
                        Divider(color = Color.White.copy(alpha = 0.4f))
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 7. HOW IT WORKS
                SectionHeader(
                    title = "Transparency Safeguards Protocol",
                    subtitle = "Milestone escrow & automated civil audits"
                )

                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    val stepsList = listOf(
                        Triple("1", "Explore & Select Services", "Browse background-verified civil contracts, structural engineering consultants, heavy rental machinery, or home maintenance experts."),
                        Triple("2", "Define Your Specifications", "Tell us your specific villa dimensions, soil conditions, materials weight capacity, or standard repair problems directly."),
                        Triple("3", "Compare Dynamic Bids", "Get up to 3 competitive quotation proposals from authorized engineers & certified regional builders with active reviews."),
                        Triple("4", "Secure Milestone Escrow", "Lock your payments safely. Hand over funds in customized installment milestones only when work passes validation checks.")
                    )

                    stepsList.forEachIndexed { idx, (num, heading, description) ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Primary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(num, color = White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = heading,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkInk2
                                )
                                Text(
                                    text = description,
                                    fontSize = 11.sp,
                                    color = Slate,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 8. PROJECTS GALLERY BEFORE AFTER SLIDER
                SectionHeader(
                    title = "Featured Dual-State Transformations",
                    subtitle = "Verify before-plastering & post-granite stages visually"
                )

                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkInk3),
                        contentAlignment = Alignment.Center
                    ) {
                        if (showBeforeState) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(DarkInk3)
                            ) {
                                Text("🏚️ PRE-PLASTER CIVIL STAGE", fontWeight = FontWeight.Black, color = Color.Red, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Exposed rough clay masonry, ungrounded PVC pipelines,\nno structural gypsum coatings.", color = Mist, fontSize = 11.sp, textAlign = TextAlign.Center)
                            }
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(PrimaryPale)
                            ) {
                                Text("🏠 PREMIUM FINISHING STAGE", fontWeight = FontWeight.Black, color = Emerald, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Granite panel flooring, flush false LED-panel ceiling,\nwarm modular acrylic paints.", color = DarkInk3, fontSize = 11.sp, textAlign = TextAlign.Center)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Dual-State Bedroom Makeover Noida NCR",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkInk2
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Before", fontSize = 10.sp, color = Slate)
                            Switch(
                                checked = !showBeforeState,
                                onCheckedChange = { showBeforeState = !it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Primary, checkedTrackColor = PrimaryPale)
                            )
                            Text("After", fontSize = 10.sp, color = Slate)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 9. EXPERT HELP DESK BANNER
                Card(
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.2.dp, Color.White.copy(alpha = 0.45f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.img_civil_engineer),
                            contentDescription = "Civil Engineering Supervisor",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(DarkInk2.copy(alpha = 0.95f), DarkInk2.copy(alpha = 0.40f))
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .background(Primary, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("EXPERT CONSULT DESK", color = White, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Need Help Vetting Your Architect?",
                                    fontWeight = FontWeight.Bold,
                                    color = White,
                                    fontSize = 15.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Speak with certified technical inspectors regarding civil soil load parameters, water supply alignments, & local structural contracts.",
                                    color = Mist,
                                    fontSize = 11.sp,
                                    lineHeight = 14.sp,
                                    modifier = Modifier.fillMaxWidth(0.9f)
                                )
                            }

                            AppButton(
                                label = "Consult Technical Desk (Free)",
                                onTap = { onNavigateToCategory("engineering-consultancy") },
                                type = ButtonType.PRIMARY,
                                backgroundColor = White,
                                contentColor = Primary,
                                modifier = Modifier.height(36.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Why Us details footer
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "🛡️ Why Choose Abhiyantri Setu?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    val benefits = listOf(
                        "• Verified Professionals: Identity, licenses & feedback verified.",
                        "• Multiple Instant Quotes: Compare and save up to 25%.",
                        "• Milestone Escrow: Release payment after work passes inspection.",
                        "• Dedicated Tech Support: Engineers guiding step-by-step."
                    )
                    benefits.forEach { text ->
                        Text(text = text, fontSize = 11.sp, color = DarkInk3, modifier = Modifier.padding(vertical = 2.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SetuAIChatbotDialog(
    onDismiss: () -> Unit,
    onDeepLinkToCategory: (String) -> Unit,
    onDeepLinkToEstimator: () -> Unit,
    onDeepLinkToMarketplace: (String) -> Unit
) {
    var userMessageInput by remember { mutableStateOf("") }
    val chatHistory = remember {
        mutableStateListOf(
            Pair("assistant", "Hello! I am your Setu AI assistant. 🤖\nAs an automated civil engineering dispatch bot, I understand building codes, raw materials pricing, local contractors in Noida NCR, and structural design scopes.\n\nType what you need, or tap a quick parameter option below! 👇")
        )
    }

    val scrollState = rememberScrollState()

    val quickChips = listOf(
        "📊 Estimate Duplex building cost",
        "🚿 Need premium Noida electrician",
        "🚜 JCB Backhoe diesel rental price",
        "🏗️ Hire expert blueprint designer",
        "🧱 Buy OPC Solid Cement bags"
    )

    fun handleSendMessage(txt: String) {
        if (txt.isBlank()) return
        chatHistory.add(Pair("user", txt))
        val lower = txt.lowercase()

        val response = when {
            lower.contains("cost") || lower.contains("estimate") || lower.contains("duplex") || lower.contains("floor") || lower.contains("calculator") -> {
                "Based on latest Delhi NCR schedule of rates (DSR): A standard duplex construction ranges around ₹1,600 to ₹2,200 per sq ft. I've prepared our Interactive Cost Estimator with dynamic civil, electrical & structural cost split calculations.\n\nDeep-link button added below: 👇\n[Open Cost Estimator]"
            }
            lower.contains("electrician") || lower.contains("plumber") || lower.contains("wire") || lower.contains("leak") || lower.contains("ac") -> {
                "Our registered local emergency maintenance crew covers electrical boards, modular wiring, and plumbing leaks instantly. You can book custom slots directly using background-verified contractors.\n\nDeep-link button added below: 👇\n[Book Maintenance Service]"
            }
            lower.contains("jcb") || lower.contains("mixer") || lower.contains("machinery") || lower.contains("scaffold") || lower.contains("rental") -> {
                "We operate the Noida NCR's premier heavy machinery leasing desk, offering JCB Backhoe loaders (₹12K/day), diesel concrete mixers (₹1.8K/day), and modular steel scaffolds on hire contracts.\n\nDeep-link button added below: 👇\n[Go to Machinery Rental]"
            }
            lower.contains("cement") || lower.contains("brick") || lower.contains("sand") || lower.contains("material") -> {
                "Abhiyantri marketplace supplies certified OPC/PPC Cement (₹380/bag), high density red bricks (₹6,500/1k), and Yamuna sand with instant delivery tracking and escrow safeguards.\n\nDeep-link button added below: 👇\n[Browse Materials]"
            }
            lower.contains("architect") || lower.contains("engineer") || lower.contains("blueprint") || lower.contains("structural") -> {
                "Our Engineering Consultancy desk covers statutory structural design, soil mechanics load analysis, and fast municipal authority blueprints filing.\n\nDeep-link button added below: 👇\n[Consult Engineer Panel]"
            }
            else -> {
                "I understand! Abhiyantri Setu operates civil & masonry contracts, licensed engineer consultancy, and materials procurement. Ask me to estimate cost, rent heavy JCB machinery, book plumbers/electricians, or buy solid bricks!"
            }
        }
        chatHistory.add(Pair("assistant", response))
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(32.dp).background(PrimaryPale, CircleShape), contentAlignment = Alignment.Center) {
                    Text("🤖", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Setu AI Chat Desk", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkInk)
                    Text("Automated Civil dispatch officer", fontSize = 10.sp, color = Slate)
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth().height(360.dp)) {
                // Messages log Scrollable
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState)
                        .background(Paper, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    chatHistory.forEach { (sender, text) ->
                        val isUser = sender == "user"
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = if (isUser) Primary else White,
                                        shape = RoundedCornerShape(
                                            topStart = 12.dp,
                                            topEnd = 12.dp,
                                            bottomStart = if (isUser) 12.dp else 0.dp,
                                            bottomEnd = if (isUser) 0.dp else 12.dp
                                        )
                                    )
                                    .border(
                                        BorderStroke(1.dp, if (isUser) Color.Transparent else Fog),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(10.dp)
                                    .widthIn(max = 220.dp)
                            ) {
                                Column {
                                    Text(
                                        text = text,
                                        color = if (isUser) White else DarkInk2,
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp
                                    )

                                    if (text.contains("[Open Cost Estimator]")) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Button(
                                            onClick = onDeepLinkToEstimator,
                                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPale, contentColor = Primary),
                                            shape = RoundedCornerShape(4.dp),
                                            modifier = Modifier.fillMaxWidth(),
                                            contentPadding = PaddingValues(4.dp)
                                        ) {
                                            Text("👉 Launch Cost Estimator", fontSize = 10.sp, fontWeight = FontWeight.Black)
                                        }
                                    }
                                    if (text.contains("[Book Maintenance Service]")) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Button(
                                            onClick = { onDeepLinkToCategory("home-maintenance") },
                                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPale, contentColor = Primary),
                                            shape = RoundedCornerShape(4.dp),
                                            modifier = Modifier.fillMaxWidth(),
                                            contentPadding = PaddingValues(4.dp)
                                        ) {
                                            Text("👉 Book Verified Plumber/AC", fontSize = 10.sp, fontWeight = FontWeight.Black)
                                        }
                                    }
                                    if (text.contains("[Go to Machinery Rental]")) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Button(
                                            onClick = { onDeepLinkToMarketplace("machinery") },
                                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPale, contentColor = Primary),
                                            shape = RoundedCornerShape(4.dp),
                                            modifier = Modifier.fillMaxWidth(),
                                            contentPadding = PaddingValues(4.dp)
                                        ) {
                                            Text("👉 Launch Machinery Rental", fontSize = 10.sp, fontWeight = FontWeight.Black)
                                        }
                                    }
                                    if (text.contains("[Browse Materials]")) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Button(
                                            onClick = { onDeepLinkToMarketplace("materials") },
                                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPale, contentColor = Primary),
                                            shape = RoundedCornerShape(4.dp),
                                            modifier = Modifier.fillMaxWidth(),
                                            contentPadding = PaddingValues(4.dp)
                                        ) {
                                            Text("👉 Open Materials Hub", fontSize = 10.sp, fontWeight = FontWeight.Black)
                                        }
                                    }
                                    if (text.contains("[Consult Engineer Panel]")) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Button(
                                            onClick = { onDeepLinkToCategory("engineering-consultancy") },
                                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPale, contentColor = Primary),
                                            shape = RoundedCornerShape(4.dp),
                                            modifier = Modifier.fillMaxWidth(),
                                            contentPadding = PaddingValues(4.dp)
                                        ) {
                                            Text("👉 Consult Structural Engineers", fontSize = 10.sp, fontWeight = FontWeight.Black)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(quickChips) { chip ->
                        Box(
                            modifier = Modifier
                                .background(Fog, RoundedCornerShape(100.dp))
                                .clickable { handleSendMessage(chip) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(chip, fontSize = 10.sp, color = DarkInk3)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = userMessageInput,
                        onValueChange = { userMessageInput = it },
                        placeholder = { Text("Ask Setu AI anything...", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        maxLines = 1,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        onClick = {
                            if (userMessageInput.isNotBlank()) {
                                handleSendMessage(userMessageInput)
                                userMessageInput = ""
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Primary)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, tint = White, modifier = Modifier.size(16.dp))
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss Desk", color = Slate, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = White,
        shape = RoundedCornerShape(12.dp)
    )
}
