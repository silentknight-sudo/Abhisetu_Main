package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.AppViewModel
import com.example.data.Category
import com.example.data.Booking
import com.example.data.Vendor
import com.example.data.Review
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun CategoriesScreenView(
    viewModel: AppViewModel,
    onCategoryTap: (String) -> Unit
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    var searchFilter by remember { mutableStateOf("") }

    val filteredList = categories.filter {
        it.name.contains(searchFilter, ignoreCase = true) || it.subServices.contains(searchFilter, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Explore App Services",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = DarkInk
        )
        Text(
            text = "Select from 12 distinct civil engineering & maintenance categories.",
            fontSize = 12.sp,
            color = Slate,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = searchFilter,
            onValueChange = { searchFilter = it },
            placeholder = { Text("Filter categories or specific sub-services...") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            shape = RoundedCornerShape(10.dp),
            leadingIcon = { Icon(Icons.Default.FilterList, contentDescription = null, tint = Slate) },
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredList) { cat ->
                CategoryCard(
                    icon = cat.icon,
                    name = cat.name,
                    servicesCount = cat.servicesCount,
                    onTap = { onCategoryTap(cat.id) }
                )
            }
        }
    }
}

@Composable
fun CategoryDetailScreenView(
    categoryId: String,
    viewModel: AppViewModel,
    onBookService: (String, Category) -> Unit, // subService, Category
    onRequestQuote: (Category) -> Unit,
    onBack: () -> Unit
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val vendors by viewModel.vendors.collectAsStateWithLifecycle()
    
    val category = categories.find { it.id == categoryId }

    if (category == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: Category Not Found.")
        }
        return
    }

    val subServicesList = category.subServices.split(",")
    val matchedVendors = vendors.filter { it.category == category.name }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Paper)
            .statusBarsPadding()
    ) {
        // App header bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = category.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkInk
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Description card
            Card(
                colors = CardDefaults.cardColors(containerColor = PrimaryPale),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(48.dp).background(White, CircleShape), contentAlignment = Alignment.Center) {
                        Text(category.icon, fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Category Scope", fontSize = 11.sp, color = Primary, fontWeight = FontWeight.Bold)
                        Text(category.description, fontSize = 13.sp, color = DarkInk2)
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Sub-Services List
            Text("Available Sub-Services", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkInk)
            Spacer(modifier = Modifier.height(8.dp))

            subServicesList.forEach { service ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = White),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Fog),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(service, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkInk)
                            Text("Fully compliant tools & service guarantee", fontSize = 11.sp, color = Slate)
                        }
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            TextButton(onClick = { onRequestQuote(category) }) {
                                Text("Quote", color = Primary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            AppButton(
                                label = "Book Now",
                                onTap = { onBookService(service, category) },
                                type = ButtonType.PRIMARY,
                                modifier = Modifier.height(34.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Local Registered Professionals
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Local Certified Contractors",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkInk
                )
                Text("${matchedVendors.size} Pros Online", fontSize = 12.sp, color = Emerald, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))

            if (matchedVendors.isEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = White),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "No direct category contractors in this pin register. Our administrative team is expanding operations! You can request details via Request Quote.",
                        fontSize = 12.sp,
                        color = Slate,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                matchedVendors.forEach { vendor ->
                    VendorCard(
                        vendorName = vendor.businessName,
                        category = vendor.category,
                        verifiedLevel = if (vendor.verificationLevel == "gold") VerifiedLevel.GOLD else VerifiedLevel.STANDARD,
                        rating = vendor.rating,
                        reviewCount = vendor.totalReviews,
                        experience = vendor.experience,
                        projectCount = vendor.totalProjects,
                        location = "${vendor.city}, UP",
                        onViewProfile = { /* can show details or go direct booking */ },
                        onRequestQuote = { onRequestQuote(category) }
                    )
                }
            }
        }
    }
}

@Composable
fun MarketplaceScreenView(
    viewModel: AppViewModel,
    onNavigateToQuote: () -> Unit
) {
    var activeTab by remember { mutableStateOf("materials") } // "materials" | "machinery" | "contractors" | "jobs"
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val vendors by viewModel.vendors.collectAsStateWithLifecycle()

    val materials = listOf(
        MarketplaceItem("OPC/PPC Cement Bags", "₹380", "per bag", "🔗 High strength cement for structural foundations", "🏗️"),
        MarketplaceItem("Red Clay Solid Bricks", "₹6,500", "per 1000 pcs", "🧱 High temperature fired structural density bricks", "🧱"),
        MarketplaceItem("Filtered Yamuna River Sand", "₹4,200", "per brass", "🏖️ Pure construction sand, minimum silt content", "🌅"),
        MarketplaceItem("Premium TMT Thermo Steel Bars", "₹54,000", "per metric ton", "💪 Grade Fe550 seismic earthquake protection steel", "🔩")
    )

    val machinery = listOf(
        MarketplaceItem("JCB Backhoe Loader", "₹12,000", "per day", "🚜 High power excavation & grading buckets", "🚜"),
        MarketplaceItem("Concrete Mixer - Diesel", "₹1,800", "per day", "🔄 Silent 250L heavy drums uniform mix", "🔄"),
        MarketplaceItem("Metal Scaffolding Sets", "₹2,500", "month / 100 sets", "👷 Steel tubes with dual locks & platform decks", "🪜"),
        MarketplaceItem("Vibratory Soil Compactor", "₹3,500", "per day", "🔋 1.5 ton surface compaction and leveling", "🔋")
    )

    val jobListings = listOf(
        JobItem("Structural CAD Supervisor", "₹38,000 / Mo", "Sharma Design Co.", "Greater Noida", "Drafting statutory drawings, civil load charts. Requires 3y AutoCAD experience.", "📐"),
        JobItem("RCC Layout Mason Supervisor", "₹25,000 / Mo", "Devi Construction", "Noida Sec 62", "Laying concrete grids, supervising 15+ daily loaders on site.", "🧱"),
        JobItem("Senior Plumbing Grid Engineer", "₹45,000 / Mo", "Gautam Plumbing NCR", "Ghaziabad", "Designing dual pipe layouts, sanitary drainage layouts for standard villa duplexes.", "🚿")
    )

    var expandedJobIndex by remember { mutableStateOf(-1) }
    var appliedJobIndices by remember { mutableStateOf(setOf<Int>()) }
    var mockPortfolioAttached by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Construction Marketplace",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DarkInk
            )
            Text(
                text = "Procure materials, rent machinery, search local contractors, or browse job listings.",
                fontSize = 12.sp,
                color = Slate,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Tabs Switcher
            val tabs = listOf(
                Pair("materials", "Materials"),
                Pair("machinery", "Heavy Rental"),
                Pair("contractors", "Find Contractor"),
                Pair("jobs", "Jobs Board 💼")
            )
            val currentTabIndex = tabs.indexOfFirst { it.first == activeTab }

            ScrollableTabRow(
                selectedTabIndex = currentTabIndex,
                containerColor = Fog,
                edgePadding = 0.dp,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .height(44.dp)
                    .fillMaxWidth(),
                indicator = { Box(Modifier.fillMaxSize()) }
            ) {
                tabs.forEachIndexed { idx, (id, label) ->
                    val isSel = activeTab == id
                    Tab(
                        selected = isSel,
                        onClick = { activeTab = id },
                        text = { Text(label, fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                        selectedContentColor = Primary,
                        unselectedContentColor = Slate,
                        modifier = Modifier
                            .background(if (isSel) PrimaryPale else Color.Transparent)
                            .padding(horizontal = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                if (activeTab == "materials" || activeTab == "machinery") {
                    val currentList = if (activeTab == "materials") materials else machinery
                    items(currentList) { item ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = White),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Fog),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(PrimaryPale, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(item.emoji, fontSize = 24.sp)
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(item.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkInk)
                                    Text(item.desc, fontSize = 11.sp, color = Slate, modifier = Modifier.padding(vertical = 2.dp), maxLines = 2)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(item.price, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Primary)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(item.unit, fontSize = 10.sp, color = Slate)
                                    }
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                AppButton(
                                    label = if (activeTab == "materials") "INQUIRE" else "RENT",
                                    onTap = {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                "Purchase request logged. Abhiyantri coordinator will match with Noida supplier bids shortly!"
                                            )
                                        }
                                        onNavigateToQuote()
                                    },
                                    type = ButtonType.PRIMARY,
                                    modifier = Modifier.height(34.dp)
                                )
                            }
                        }
                    }
                } else if (activeTab == "contractors") {
                    items(vendors) { vendor ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = White),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Fog),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .background(PrimaryPale, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("👷", fontSize = 22.sp)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(vendor.businessName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkInk)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            val verifiedLevel = when (vendor.subscriptionPlan.lowercase()) {
                                                "gold", "premium" -> VerifiedLevel.GOLD
                                                "standard", "silver" -> VerifiedLevel.STANDARD
                                                else -> VerifiedLevel.NONE
                                            }
                                            VerifiedBadge(level = verifiedLevel)
                                        }
                                        Text("Category Cluster: ${vendor.category}", fontSize = 11.sp, color = Slate)
                                        Text("★ ${vendor.rating} • ${vendor.experience} years active • ${vendor.city}", fontSize = 11.sp, color = Slate)
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    AppButton(
                                        label = "CALL CONTRACTOR",
                                        onTap = {
                                            scope.launch {
                                                snackbarHostState.showSnackbar("Initiating direct call connection to ${vendor.ownerName}...")
                                            }
                                        },
                                        type = ButtonType.OUTLINED,
                                        modifier = Modifier.weight(1f).height(32.dp)
                                    )
                                    AppButton(
                                        label = "SUBMIT BLUEPRINTS / GET BID",
                                        onTap = onNavigateToQuote,
                                        type = ButtonType.PRIMARY,
                                        modifier = Modifier.weight(1.3f).height(32.dp)
                                    )
                                }
                            }
                        }
                    }
                } else if (activeTab == "jobs") {
                    items(jobListings.size) { index ->
                        val job = jobListings[index]
                        val isExpanded = expandedJobIndex == index
                        val isApplied = appliedJobIndices.contains(index)

                        Card(
                            colors = CardDefaults.cardColors(containerColor = White),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, if (isExpanded) Primary else Fog),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { expandedJobIndex = if (isExpanded) -1 else index }
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .background(Emerald.copy(0.15f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(job.emoji, fontSize = 22.sp)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(job.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkInk)
                                        Text("Recruiter: ${job.company} • ${job.city}", fontSize = 11.sp, color = Slate)
                                        Text("Offered Budget: ${job.salary}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Emerald)
                                    }
                                    Icon(
                                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = null,
                                        tint = Slate
                                    )
                                }

                                AnimatedVisibility(visible = isExpanded) {
                                    Column(modifier = Modifier.padding(top = 12.dp)) {
                                        Divider(color = Fog, modifier = Modifier.padding(bottom = 12.dp))
                                        Text("Job Scope Specifications:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DarkInk)
                                        Text(job.description, fontSize = 11.sp, color = DarkInk3, modifier = Modifier.padding(top = 4.dp, bottom = 12.dp))

                                        if (isApplied) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth().background(PrimaryPale, RoundedCornerShape(8.dp)).padding(8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                Icon(Icons.Default.Check, contentDescription = null, tint = Primary, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text("Application Submitted Successfully!", color = Primary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                            }
                                        } else {
                                            // Portfolio Upload
                                            Text("Apply with Construction Portfolio / Credentials:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DarkInk)
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(80.dp)
                                                    .background(Paper, RoundedCornerShape(8.dp))
                                                    .border(BorderStroke(1.dp, Primary.copy(alpha = 0.4f)), RoundedCornerShape(8.dp))
                                                    .clickable { mockPortfolioAttached = !mockPortfolioAttached },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                if (mockPortfolioAttached) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Emerald, modifier = Modifier.size(20.dp))
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Text("Attached: Engineering_Portfolio_Noida.pdf", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkInk)
                                                    }
                                                } else {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp))
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Text("Click to attach portfolio / CV (PDF/JPG)", fontSize = 11.sp, color = Slate)
                                                    }
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(10.dp))

                                            AppButton(
                                                label = "SUBMIT PORTFOLIO & APPLY",
                                                onTap = {
                                                    if (!mockPortfolioAttached) {
                                                        scope.launch {
                                                            snackbarHostState.showSnackbar("Please attach your portfolio document first!")
                                                        }
                                                    } else {
                                                        appliedJobIndices = appliedJobIndices + index
                                                        scope.launch {
                                                            snackbarHostState.showSnackbar("Success! Application registered at ${job.company}.")
                                                        }
                                                    }
                                                },
                                                type = ButtonType.PRIMARY,
                                                modifier = Modifier.fillMaxWidth().height(36.dp)
                                            )
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
}

data class MarketplaceItem(val name: String, val price: String, val unit: String, val desc: String, val emoji: String)
data class JobItem(val title: String, val salary: String, val company: String, val city: String, val description: String, val emoji: String)

@Composable
fun CostEstimatorScreenView(
    viewModel: AppViewModel,
    onBack: () -> Unit
) {
    var projectType by remember { mutableStateOf("New Build") }
    var plotArea by remember { mutableStateOf("1500") }
    var floors by remember { mutableStateOf(1) }
    var quality by remember { mutableStateOf("Standard") }
    
    var includeInterior by remember { mutableStateOf(true) }
    var includeWaterproofing by remember { mutableStateOf(false) }
    var includeElectrical by remember { mutableStateOf(true) }
    var includePlumbing by remember { mutableStateOf(true) }

    val result by viewModel.estimatorResult.collectAsStateWithLifecycle()

    // Run first calculation on load
    LaunchedEffect(Unit) {
         viewModel.calculateCost(
             projectType = projectType,
             plotAreaDp = plotArea.toDoubleOrNull() ?: 1500.0,
             floors = floors,
             quality = quality,
             includeInterior = includeInterior,
             includeWaterproof = includeWaterproofing,
             includeElectrical = includeElectrical,
             includePlumbing = includePlumbing
         )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Paper)
            .statusBarsPadding()
    ) {
         Row(
             modifier = Modifier
                 .fillMaxWidth()
                 .background(White)
                 .padding(16.dp),
             verticalAlignment = Alignment.CenterVertically
         ) {
             IconButton(onClick = onBack) {
                 Icon(Icons.Default.ArrowBack, contentDescription = "back")
             }
             Spacer(modifier = Modifier.width(8.dp))
             Text(
                 text = "Construction Cost Estimator",
                 fontSize = 18.sp,
                 fontWeight = FontWeight.Bold,
                 color = DarkInk
             )
         }

         Column(
             modifier = Modifier
                 .fillMaxSize()
                 .verticalScroll(rememberScrollState())
                 .padding(16.dp)
         ) {
              Card(
                  colors = CardDefaults.cardColors(containerColor = White),
                  shape = RoundedCornerShape(10.dp),
                  border = BorderStroke(1.dp, Fog),
                  modifier = Modifier.fillMaxWidth()
              ) {
                  Column(modifier = Modifier.padding(16.dp)) {
                      Text("Estimate Parameter Matrix", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkInk)
                      Spacer(modifier = Modifier.height(14.dp))

                      // Project Type
                      Text("Project Type", fontSize = 12.sp, color = Slate)
                      Row(
                          modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 12.dp),
                          horizontalArrangement = Arrangement.spacedBy(8.dp)
                      ) {
                          listOf("New Build", "Renovation", "Extension").forEach { type ->
                              val isSel = type == projectType
                              Button(
                                  onClick = { projectType = type },
                                  colors = ButtonDefaults.buttonColors(
                                      containerColor = if (isSel) Primary else Fog,
                                      contentColor = if (isSel) White else DarkInk
                                  ),
                                  shape = RoundedCornerShape(8.dp),
                                  modifier = Modifier.weight(1f),
                                  contentPadding = PaddingValues(0.dp)
                              ) {
                                  Text(type, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                              }
                          }
                      }

                      // Plot Area
                      OutlinedTextField(
                          value = plotArea,
                          onValueChange = { plotArea = it },
                          label = { Text("Plot Area (Sq.Ft)") },
                          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                          modifier = Modifier.fillMaxWidth(),
                          colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary)
                      )

                      Spacer(modifier = Modifier.height(12.dp))

                      // Floor incrementer
                      Row(
                          modifier = Modifier.fillMaxWidth(),
                          horizontalArrangement = Arrangement.SpaceBetween,
                          verticalAlignment = Alignment.CenterVertically
                      ) {
                          Column {
                              Text("Number of Floors", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkInk)
                              Text("Includes G + specified floors", fontSize = 11.sp, color = Slate)
                          }
                          Row(verticalAlignment = Alignment.CenterVertically) {
                              IconButton(
                                  onClick = { if (floors > 1) floors-- },
                                  colors = IconButtonDefaults.iconButtonColors(containerColor = Fog)
                              ) {
                                  Icon(Icons.Default.Remove, contentDescription = null, tint = DarkInk)
                              }
                              Text(
                                  text = floors.toString(),
                                  fontWeight = FontWeight.Bold,
                                  fontSize = 16.sp,
                                  modifier = Modifier.padding(horizontal = 14.dp)
                              )
                              IconButton(
                                  onClick = { if (floors < 5) floors++ },
                                  colors = IconButtonDefaults.iconButtonColors(containerColor = Fog)
                              ) {
                                  Icon(Icons.Default.Add, contentDescription = null, tint = DarkInk)
                              }
                          }
                      }

                      Spacer(modifier = Modifier.height(12.dp))

                      // Quality Class Selection
                      Text("Quality Bracket & Specifications", fontSize = 12.sp, color = Slate)
                      Row(
                          modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 12.dp),
                          horizontalArrangement = Arrangement.spacedBy(4.dp)
                      ) {
                          listOf("Economy", "Standard", "Premium", "Luxury").forEach { q ->
                              val isSel = q == quality
                              Box(
                                  modifier = Modifier
                                      .weight(1f)
                                      .background(
                                          color = if (isSel) Primary else Fog,
                                          shape = RoundedCornerShape(8.dp)
                                      )
                                      .clickable { quality = q }
                                      .padding(vertical = 10.dp),
                                  contentAlignment = Alignment.Center
                              ) {
                                  Text(
                                      text = q, 
                                      fontSize = 10.sp, 
                                      fontWeight = FontWeight.Bold, 
                                      color = if (isSel) White else DarkInk
                                  )
                              }
                          }
                      }

                      Spacer(modifier = Modifier.height(8.dp))

                      // Component Checklist
                      Text("Include Aux Services Scope", fontSize = 12.sp, color = Slate)
                      Spacer(modifier = Modifier.height(6.dp))

                      val listChecks = listOf(
                          Triple("Interior Design & Carpentry", includeInterior) { includeInterior = !includeInterior },
                          Triple("Base Waterproofing Shield", includeWaterproofing) { includeWaterproofing = !includeWaterproofing },
                          Triple("Preconfigured Electrical Wiring Grid", includeElectrical) { includeElectrical = !includeElectrical },
                          Triple("Plumbing and Sanitary System", includePlumbing) { includePlumbing = !includePlumbing }
                      )

                      listChecks.forEach { (label, value, onToggle) ->
                          Row(
                              modifier = Modifier.fillMaxWidth().clickable { onToggle() }.padding(vertical = 6.dp),
                              verticalAlignment = Alignment.CenterVertically
                          ) {
                              Checkbox(
                                  checked = value,
                                  onCheckedChange = { onToggle() },
                                  colors = CheckboxDefaults.colors(checkedColor = Primary)
                              )
                              Spacer(modifier = Modifier.width(8.dp))
                              Text(label, fontSize = 13.sp, color = DarkInk3)
                          }
                      }

                      Spacer(modifier = Modifier.height(16.dp))

                      AppButton(
                          label = "CALCULATE ESTIMATED COST",
                          onTap = {
                              viewModel.calculateCost(
                                  projectType = projectType,
                                  plotAreaDp = plotArea.toDoubleOrNull() ?: 1500.0,
                                  floors = floors,
                                  quality = quality,
                                  includeInterior = includeInterior,
                                  includeWaterproof = includeWaterproofing,
                                  includeElectrical = includeElectrical,
                                  includePlumbing = includePlumbing
                              )
                          },
                          modifier = Modifier.fillMaxWidth()
                      )
                  }
              }

              Spacer(modifier = Modifier.height(20.dp))

              // Show Computed Results View
              result?.let { item ->
                  Card(
                      colors = CardDefaults.cardColors(containerColor = White),
                      shape = RoundedCornerShape(10.dp),
                      border = BorderStroke(1.dp, Fog),
                      modifier = Modifier.fillMaxWidth()
                  ) {
                      Column(modifier = Modifier.padding(16.dp)) {
                          Text("Breakdown Analysis Result", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DarkInk)
                          
                          Spacer(modifier = Modifier.height(14.dp))

                          Box(
                              modifier = Modifier
                                  .fillMaxWidth()
                                  .background(PrimaryPale, RoundedCornerShape(8.dp))
                                  .padding(16.dp),
                              contentAlignment = Alignment.Center
                          ) {
                              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                  Text("Estimated Overall Investment Range", fontSize = 11.sp, color = Primary, fontWeight = FontWeight.Bold)
                                  Spacer(modifier = Modifier.height(4.dp))
                                  Text(
                                      text = "₹${formatCurrency(item.minCost)} – ₹${formatCurrency(item.maxCost)}",
                                      fontSize = 20.sp,
                                      fontWeight = FontWeight.Black,
                                      color = Primary,
                                      fontFamily = FontFamily.Monospace
                                  )
                                  Spacer(modifier = Modifier.height(4.dp))
                                  Text("Local rates computed for Noida NCR", fontSize = 11.sp, color = Slate)
                              }
                          }

                          Spacer(modifier = Modifier.height(24.dp))

                          Text("Material & Cost Distribution Grid", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkInk)
                          Spacer(modifier = Modifier.height(12.dp))

                          // High-fidelity custom canvas drawing pie chart representing material breakdown
                          Box(
                              modifier = Modifier
                                  .fillMaxWidth()
                                  .height(140.dp),
                              contentAlignment = Alignment.Center
                          ) {
                              Canvas(modifier = Modifier.size(110.dp)) {
                                  val sizeOuter = size
                                  val center = Offset(sizeOuter.width / 2, sizeOuter.height / 2)
                                  val radius = sizeOuter.width / 2

                                  val sweepCivil = (item.civilPct / 100.0f) * 360f
                                  val sweepFinishing = (item.finishingPct / 100.0f) * 360f
                                  val sweepElec = (item.electricalPct / 100.0f) * 360f
                                  val sweepPlumb = (item.plumbingPct / 100.0f) * 360f

                                  drawArc(Primary, 0f, sweepCivil, false, size = Size(radius * 2, radius * 2), style = Stroke(25f))
                                  drawArc(Gold, sweepCivil, sweepFinishing, false, size = Size(radius * 2, radius * 2), style = Stroke(25f))
                                  drawArc(Emerald, sweepCivil + sweepFinishing, sweepElec, false, size = Size(radius * 2, radius * 2), style = Stroke(25f))
                                  drawArc(Sky, sweepCivil + sweepFinishing + sweepElec, sweepPlumb, false, size = Size(radius * 2, radius * 2), style = Stroke(25f))
                              }
                          }

                          Spacer(modifier = Modifier.height(16.dp))

                          // List items and indicators
                          val breakdownItems = listOf(
                              Quadruple("🏗️ Civil Concrete Works", item.civilCost, item.civilPct, Primary),
                              Quadruple("🎨 Fine Painting & Finishing", item.finishingCost, item.finishingPct, Gold),
                              Quadruple("⚡ Power Cables & Electrical", item.electricalCost, item.electricalPct, Emerald),
                              Quadruple("🚿 Plumb pipes & Sanitary", item.plumbingCost, item.plumbingPct, Sky)
                          )

                          breakdownItems.forEach { (label, value, pct, color) ->
                              Row(
                                  modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                  horizontalArrangement = Arrangement.SpaceBetween,
                                  verticalAlignment = Alignment.CenterVertically
                              ) {
                                  Row(verticalAlignment = Alignment.CenterVertically) {
                                      Box(modifier = Modifier.size(10.dp).background(color, CircleShape))
                                      Spacer(modifier = Modifier.width(8.dp))
                                      Text(label, fontSize = 13.sp, color = DarkInk3)
                                  }
                                  Column(horizontalAlignment = Alignment.End) {
                                      Text("₹${formatCurrency(value)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkInk, fontFamily = FontFamily.Monospace)
                                      Text("${pct.toInt()}% Distribution", fontSize = 10.sp, color = Slate)
                                  }
                              }
                          }
                          
                          Spacer(modifier = Modifier.height(18.dp))

                          AppButton(
                              label = "GET DETAILED CONTRACTOR QUOTES",
                              onTap = {
                                  // Navigates and submits
                              },
                              type = ButtonType.PRIMARY,
                              modifier = Modifier.fillMaxWidth()
                          )
                      }
                  }
              }

              Spacer(modifier = Modifier.height(40.dp))
         }
    }
}

data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

private fun formatCurrency(amt: Double): String {
    val integerPart = amt.toInt()
    return if (integerPart > 100000) {
        val lakhs = integerPart / 100000f
        String.format("%.2f Lakhs", lakhs)
    } else {
        val thousand = integerPart / 1000f
        String.format("%.1f K", thousand)
    }
}

@Composable
fun BookingsScreenView(
    viewModel: AppViewModel
) {
    val rawBookings by viewModel.bookings.collectAsStateWithLifecycle()
    val user by viewModel.loggedInUser.collectAsStateWithLifecycle()

    val filteredBookings = rawBookings.filter { it.customerId == user?.uid }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "My Scheduled Bookings",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = DarkInk
        )
        Text(
            text = "Track active construction milestones and dispatch statuses live.",
            fontSize = 12.sp,
            color = Slate,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (filteredBookings.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📋", fontSize = 54.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No bookings registered yet.", fontWeight = FontWeight.Bold, color = DarkInk2)
                    Text("Book specialized services starting from Home home.", fontSize = 12.sp, color = Slate)
                }
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(filteredBookings) { booking ->
                    BookingItemExpandable(booking = booking, viewModel = viewModel)
                }
            }
        }
    }
}

// Nested expandable tracking milestone timeline 
@Composable
fun BookingItemExpandable(
    booking: Booking,
    viewModel: AppViewModel
) {
    var isExpanded by remember { mutableStateOf(false) }
    val milestones = listOf("Booking Confirmed", "Pro Assigned", "Work Started", "Quality Inspected", "Work Completed")

    Card(
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, if (isExpanded) Primary else Fog),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { isExpanded = !isExpanded }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = booking.service,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = DarkInk
                    )
                    Text("Booking ID: ${booking.bookingId}", fontSize = 11.sp, color = Primary, fontFamily = FontFamily.Monospace)
                }
                BookingStatusChip(status = booking.status)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(14.dp), tint = Slate)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a", java.util.Locale.getDefault()).format(booking.dateTime),
                    fontSize = 12.sp,
                    color = Slate
                )
            }
            
            Text(
                text = "🏠 Address: ${booking.location}",
                fontSize = 12.sp,
                color = Slate,
                modifier = Modifier.padding(top = 4.dp)
            )

            // Animated Timeline
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier.padding(top = 16.dp).fillMaxWidth()
                ) {
                    Text(
                        text = "Real-Time Tracking Timeline",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = DarkInk
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    milestones.forEachIndexed { index, mName ->
                        val isDone = index <= booking.currentMilestoneIndex
                        val isCurrent = index == booking.currentMilestoneIndex

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .background(
                                            color = if (isDone) Emerald else Fog,
                                            shape = CircleShape
                                        )
                                        .border(
                                            border = if (isCurrent) BorderStroke(2.dp, Primary) else BorderStroke(0.dp, Color.Transparent),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isDone) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = White, modifier = Modifier.size(10.dp))
                                    }
                                }
                                if (index < milestones.size - 1) {
                                    Box(
                                        modifier = Modifier
                                            .width(2.dp)
                                            .height(24.dp)
                                            .background(if (index < booking.currentMilestoneIndex) Emerald else Fog)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = mName,
                                    fontSize = 13.sp,
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isDone) DarkInk else Mist
                                )
                                Text(
                                    text = if (isDone) "Milestone Cleared" else "Pending milestone",
                                    fontSize = 11.sp,
                                    color = Slate
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Divider(color = Fog)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Estimated", fontSize = 11.sp, color = Slate)
                            Text("₹${booking.estimatedAmount}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkInk)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Prepaid Milestone Advance", fontSize = 11.sp, color = Slate)
                            Text("₹${booking.advancePaid}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Emerald)
                        }
                    }

                    if (booking.status.lowercase() == "completed") {
                        Spacer(modifier = Modifier.height(16.dp))
                        RatingPanel(
                            booking = booking,
                            onSendReview = { rating, comment ->
                                viewModel.submitReview(
                                    reviewId = "rev_" + System.currentTimeMillis(),
                                    customerId = booking.customerId,
                                    customerName = booking.customerName,
                                    vendorId = booking.vendorId,
                                    bookingId = booking.bookingId,
                                    rating = rating.toDouble(),
                                    comment = comment
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RatingPanel(
    booking: Booking,
    onSendReview: (Int, String) -> Unit
) {
    var rating by remember { mutableStateOf(5) }
    var comment by remember { mutableStateOf("") }
    var reviewSubmitted by remember { mutableStateOf(false) }

    if (reviewSubmitted) {
        Box(
            modifier = Modifier.fillMaxWidth().background(PrimaryPale, RoundedCornerShape(8.dp)).padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Thank you! Review registered.", color = Primary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Fog, RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            Text("Review ${booking.vendorName}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DarkInk)
            Spacer(modifier = Modifier.height(8.dp))
            
            Row {
                for (s in 1..5) {
                    IconButton(onClick = { rating = s }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (s <= rating) Gold else Mist,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
            
            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                placeholder = { Text("Quality of masonry concrete masonry finishing?") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary)
            )
            Spacer(modifier = Modifier.height(8.dp))
            AppButton(
                label = "Submit Honest Review",
                onTap = {
                    onSendReview(rating, comment)
                    reviewSubmitted = true
                },
                modifier = Modifier.align(Alignment.End).height(36.dp)
            )
        }
    }
}

@Composable
fun ProfileScreenView(
    viewModel: AppViewModel,
    onNavigateToEstimator: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val user by viewModel.loggedInUser.collectAsStateWithLifecycle()
    val city by viewModel.selectedCity.collectAsStateWithLifecycle()
    val bookings by viewModel.bookings.collectAsStateWithLifecycle()

    var enablePushAlerts by remember { mutableStateOf(true) }
    var enableSMSMilestones by remember { mutableStateOf(true) }
    var enableEmailSummary by remember { mutableStateOf(false) }

    var showSupportDialog by remember { mutableStateOf(false) }
    var expandedFAQIndex by remember { mutableStateOf(-1) }
    
    val customerBookings = bookings.filter { it.customerId == user?.uid }
    val totalBookingsCount = customerBookings.size
    val activeProjectsCount = customerBookings.count { it.status.lowercase() == "accepted" || it.status.lowercase() == "in_progress" }
    val totalSpentAmount = customerBookings.sumOf { it.estimatedAmount }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
         Text(
             text = "My Profile & Settings",
             fontSize = 24.sp,
             fontWeight = FontWeight.Bold,
             color = DarkInk
         )
         Text(
             text = "Manage your service preferences, active bookings ledger, and rewards.",
             fontSize = 12.sp,
             color = Slate,
             modifier = Modifier.padding(bottom = 16.dp)
         )

         // U-08 Profile card with avatar/credentials
         Card(
             colors = CardDefaults.cardColors(containerColor = White),
             shape = RoundedCornerShape(16.dp),
             border = BorderStroke(1.dp, Fog),
             modifier = Modifier.fillMaxWidth()
         ) {
             Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                 Box(
                     modifier = Modifier
                         .size(72.dp)
                         .clip(CircleShape)
                         .background(PrimaryPale),
                     contentAlignment = Alignment.Center
                 ) {
                     Text(
                         text = (user?.name?.take(2)?.uppercase()) ?: "AS",
                         fontWeight = FontWeight.Bold,
                         fontSize = 24.sp,
                         color = Primary
                     )
                 }
                 Spacer(modifier = Modifier.width(16.dp))
                 Column {
                     Text(
                         text = user?.name ?: "Abhiyantri Customer",
                         fontWeight = FontWeight.Black,
                         fontSize = 18.sp,
                         color = DarkInk
                     )
                     Text(
                         text = user?.phone ?: "+91 98710 24985",
                         fontSize = 12.sp,
                         color = Slate
                     )
                     Spacer(modifier = Modifier.height(2.dp))
                     Row(verticalAlignment = Alignment.CenterVertically) {
                         Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(11.dp), tint = Primary)
                         Spacer(modifier = Modifier.width(4.dp))
                         Text(text = "$city, Delhi NCR Hub", fontSize = 11.sp, color = Slate)
                     }
                     Text(
                         text = "Member since June 2024",
                         fontSize = 10.sp,
                         color = Slate,
                         modifier = Modifier.padding(top = 4.dp)
                     )
                 }
             }
         }

         Spacer(modifier = Modifier.height(16.dp))

         // U-08 Stats Row
         Row(
             modifier = Modifier.fillMaxWidth(),
             horizontalArrangement = Arrangement.spacedBy(8.dp)
         ) {
             Card(
                 colors = CardDefaults.cardColors(containerColor = White),
                 border = BorderStroke(1.dp, Fog),
                 modifier = Modifier.weight(1f)
             ) {
                 Column(
                     modifier = Modifier.padding(12.dp),
                     horizontalAlignment = Alignment.CenterHorizontally
                 ) {
                     Text("Total Bookings", fontSize = 10.sp, color = Slate)
                     Text("$totalBookingsCount", fontWeight = FontWeight.Black, fontSize = 20.sp, color = DarkInk)
                 }
             }

             Card(
                 colors = CardDefaults.cardColors(containerColor = White),
                 border = BorderStroke(1.dp, Fog),
                 modifier = Modifier.weight(1f)
             ) {
                 Column(
                     modifier = Modifier.padding(12.dp),
                     horizontalAlignment = Alignment.CenterHorizontally
                 ) {
                     Text("Active Projects", fontSize = 10.sp, color = Slate)
                     Text("$activeProjectsCount", fontWeight = FontWeight.Black, fontSize = 20.sp, color = Primary)
                 }
             }

             Card(
                 colors = CardDefaults.cardColors(containerColor = White),
                 border = BorderStroke(1.dp, Fog),
                 modifier = Modifier.weight(1.2f)
             ) {
                 Column(
                     modifier = Modifier.padding(12.dp),
                     horizontalAlignment = Alignment.CenterHorizontally
                 ) {
                     Text("Total Spent", fontSize = 10.sp, color = Slate)
                     Text("₹${String.format("%,.0f", totalSpentAmount)}", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Emerald, modifier = Modifier.padding(top = 4.dp))
                 }
             }
         }

         // U-08 Referral Section "Refer & earn ₹200 per friend" + image
         Spacer(modifier = Modifier.height(24.dp))
         Card(
             shape = RoundedCornerShape(16.dp),
             border = BorderStroke(1.2.dp, Color.White),
             modifier = Modifier
                 .fillMaxWidth()
                 .height(180.dp),
             elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
         ) {
             Box(modifier = Modifier.fillMaxSize()) {
                 Image(
                     painter = painterResource(id = com.example.R.drawable.img_referral_banner),
                     contentDescription = "Referral Program Reward Poster",
                     modifier = Modifier.fillMaxSize(),
                     contentScale = ContentScale.Crop
                 )
                 Box(
                     modifier = Modifier
                         .fillMaxSize()
                         .background(
                             Brush.verticalGradient(
                                 colors = listOf(Color.Transparent, DarkInk.copy(alpha = 0.9f))
                              )
                          )
                  )
                  Column(
                      modifier = Modifier
                          .align(Alignment.BottomStart)
                          .padding(16.dp)
                  ) {
                      Box(
                          modifier = Modifier
                              .background(Primary, RoundedCornerShape(4.dp))
                              .padding(horizontal = 6.dp, vertical = 2.dp)
                      ) {
                          Text("REFERRAL SPECIAL", color = White, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                      }
                      Spacer(modifier = Modifier.height(4.dp))
                      Text("Refer & Earn ₹200 Per Friend", color = White, fontSize = 16.sp, fontWeight = FontWeight.Black)
                      Text("Spread the engineering escrow revolution. Get ₹200 in wallet on friend's first milestone check", color = Mist, fontSize = 11.sp)
                  }
             }
         }

         Spacer(modifier = Modifier.height(24.dp))
         Text("Account Settings & Booking Vault", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkInk)
         Spacer(modifier = Modifier.height(10.dp))

         val itemsActions = listOf(
             Triple("🚧 Construction Cost Calculator", "Open live materials load estimator") { onNavigateToEstimator() },
             Triple("📍 Saved Addresses", "Edit delivery pincodes") {},
             Triple("💳 Saved Payment Methods", "Razorpay / UPI default configuration") {},
             Triple("📁 Regulatory Blueprints Drafts", "Access pending municipal drawings") {},
             Triple("🛡️ Quality Escape Escalations", "Raise a claim for pending inspection support") {}
         )

         itemsActions.forEach { (label, desc, block) ->
             Card(
                 colors = CardDefaults.cardColors(containerColor = White),
                 shape = RoundedCornerShape(8.dp),
                 border = BorderStroke(1.dp, Fog),
                 modifier = Modifier
                     .fillMaxWidth()
                     .padding(vertical = 4.dp)
                     .clickable { block() }
             ) {
                 Row(
                     modifier = Modifier.padding(14.dp),
                     horizontalArrangement = Arrangement.SpaceBetween,
                     verticalAlignment = Alignment.CenterVertically
                 ) {
                     Column(modifier = Modifier.weight(1f)) {
                         Text(label, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkInk)
                         Text(desc, fontSize = 11.sp, color = Slate)
                     }
                     Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Slate)
                 }
             }
         }

         // Shortlisted / favorite pros
         Spacer(modifier = Modifier.height(24.dp))
         Text("Saved & Shortlisted Contractors 💖", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkInk)
         Text("Quickly connect with your preferred vetted experts", fontSize = 11.sp, color = Slate, modifier = Modifier.padding(bottom = 8.dp))

         val shortlistedPros = listOf(
             Pair("Sharma Structural Contracts", "Building Construction & RCC Specialist"),
             Pair("Gautam Plumbing NCR", "Licensed Sanitary & Pipe Grid Installation")
         )

         shortlistedPros.forEach { (name, trade) ->
             Card(
                 colors = CardDefaults.cardColors(containerColor = White),
                 shape = RoundedCornerShape(10.dp),
                 border = BorderStroke(1.dp, Fog),
                 modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
             ) {
                 Column(modifier = Modifier.padding(12.dp)) {
                      Row(
                          modifier = Modifier.fillMaxWidth(),
                          horizontalArrangement = Arrangement.SpaceBetween,
                          verticalAlignment = Alignment.CenterVertically
                      ) {
                          Column {
                              Text(name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkInk)
                              Text(trade, fontSize = 11.sp, color = Slate)
                          }
                          Box(
                              modifier = Modifier
                                  .background(PrimaryPale, RoundedCornerShape(100.dp))
                                  .padding(horizontal = 8.dp, vertical = 4.dp)
                          ) {
                              Row(verticalAlignment = Alignment.CenterVertically) {
                                  Icon(Icons.Default.Star, contentDescription = null, tint = Gold, modifier = Modifier.size(12.dp))
                                  Spacer(modifier = Modifier.width(4.dp))
                                  Text("4.9", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Primary)
                              }
                          }
                      }
                      Spacer(modifier = Modifier.height(8.dp))
                      Row(
                          modifier = Modifier.fillMaxWidth(),
                          horizontalArrangement = Arrangement.End
                      ) {
                          TextButton(onClick = { /* Call action */ }, contentPadding = PaddingValues(0.dp)) {
                              Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(12.dp), tint = Primary)
                              Spacer(modifier = Modifier.width(4.dp))
                              Text("Call Now", fontSize = 11.sp, color = Primary)
                          }
                      }
                 }
             }
         }

         // Notification Preferences (Phase 10)
         Spacer(modifier = Modifier.height(24.dp))
         Text("Notification Settings", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkInk)
         Spacer(modifier = Modifier.height(10.dp))

         Card(
             colors = CardDefaults.cardColors(containerColor = White),
             shape = RoundedCornerShape(10.dp),
             border = BorderStroke(1.dp, Fog),
             modifier = Modifier.fillMaxWidth()
         ) {
             Column(modifier = Modifier.padding(14.dp)) {
                 Row(
                     modifier = Modifier.fillMaxWidth(),
                     horizontalArrangement = Arrangement.SpaceBetween,
                     verticalAlignment = Alignment.CenterVertically
                 ) {
                     Column(modifier = Modifier.weight(1f)) {
                         Text("Milestone Push Alerts", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DarkInk)
                         Text("Live structural phase updates & escrow verification", fontSize = 11.sp, color = Slate)
                     }
                     Switch(checked = enablePushAlerts, onCheckedChange = { enablePushAlerts = it }, colors = SwitchDefaults.colors(checkedThumbColor = Primary, checkedTrackColor = PrimaryPale))
                 }

                 Divider(color = Fog, modifier = Modifier.padding(vertical = 10.dp))

                 Row(
                     modifier = Modifier.fillMaxWidth(),
                     horizontalArrangement = Arrangement.SpaceBetween,
                     verticalAlignment = Alignment.CenterVertically
                 ) {
                     Column(modifier = Modifier.weight(1f)) {
                         Text("Escrow Quote proposals via SMS", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DarkInk)
                         Text("Instant SMS messages upon vendor bid receipts", fontSize = 11.sp, color = Slate)
                     }
                     Switch(checked = enableSMSMilestones, onCheckedChange = { enableSMSMilestones = it }, colors = SwitchDefaults.colors(checkedThumbColor = Primary, checkedTrackColor = PrimaryPale))
                 }

                 Divider(color = Fog, modifier = Modifier.padding(vertical = 10.dp))

                 Row(
                     modifier = Modifier.fillMaxWidth(),
                     horizontalArrangement = Arrangement.SpaceBetween,
                     verticalAlignment = Alignment.CenterVertically
                 ) {
                     Column(modifier = Modifier.weight(1f)) {
                         Text("Weekly Materials Price Index", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DarkInk)
                         Text("Receive weekly market trend price digest reviews", fontSize = 11.sp, color = Slate)
                     }
                     Switch(checked = enableEmailSummary, onCheckedChange = { enableEmailSummary = it }, colors = SwitchDefaults.colors(checkedThumbColor = Primary, checkedTrackColor = PrimaryPale))
                 }
             }
         }

         // Help Center & FAQ (Phase 10)
         Spacer(modifier = Modifier.height(24.dp))
         Text("FAQ & Resolution Center 🛡️", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkInk)
         Spacer(modifier = Modifier.height(10.dp))

         val faqs = listOf(
             Pair("How does the milestone escrow payment work?", "Funds remain safely held in Abhiyantri escrow. When the service provider logs completion, you inspect the work. Once satisfied, tap 'Release Milestone Payment' to pay."),
             Pair("How can I get Silver/Gold badge verified?", "Businesses can log in to the Provider console dashboard, go to the active registration screen, upload GSTIN, COA architect, or municipal contracting licences for escrow vetting."),
             Pair("What if the contractor delays or causes structural damage?", "You can instantly escalate to our dedicated Tech Specialist desk using the 'Quality Escape Escalation' link above, which halts escrow payout immediately.")
         )

         faqs.forEachIndexed { idx, (q, a) ->
             val isOpen = expandedFAQIndex == idx
             Card(
                 colors = CardDefaults.cardColors(containerColor = White),
                 shape = RoundedCornerShape(8.dp),
                 border = BorderStroke(1.dp, if (isOpen) Primary else Fog),
                 modifier = Modifier
                     .fillMaxWidth()
                     .padding(vertical = 4.dp)
                     .clickable { expandedFAQIndex = if (isOpen) -1 else idx }
             ) {
                 Column(modifier = Modifier.padding(12.dp)) {
                     Row(
                         modifier = Modifier.fillMaxWidth(),
                         horizontalArrangement = Arrangement.SpaceBetween,
                         verticalAlignment = Alignment.CenterVertically
                     ) {
                         Text(q, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (isOpen) Primary else DarkInk, modifier = Modifier.weight(1f))
                         Icon(
                             imageVector = if (isOpen) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                             contentDescription = null,
                             tint = if (isOpen) Primary else Slate,
                             modifier = Modifier.size(18.dp)
                         )
                     }
                     AnimatedVisibility(visible = isOpen) {
                         Column(modifier = Modifier.padding(top = 8.dp)) {
                             Divider(color = Fog, modifier = Modifier.padding(bottom = 8.dp))
                             Text(a, fontSize = 11.sp, color = DarkInk3, lineHeight = 15.sp)
                         }
                     }
                 }
             }
         }

         Spacer(modifier = Modifier.height(16.dp))

         AppButton(
             label = "💬 LIVE CHAT WITH SUPPORT TECHS",
             onTap = { showSupportDialog = true },
             type = ButtonType.PRIMARY,
             backgroundColor = Emerald,
             modifier = Modifier.fillMaxWidth()
         )

         Spacer(modifier = Modifier.height(32.dp))

         AppButton(
             label = "LOG OUT SESSIONS",
             onTap = onLogoutClick,
             type = ButtonType.PRIMARY,
             backgroundColor = Color.Red,
             modifier = Modifier.fillMaxWidth()
         )

         if (showSupportDialog) {
             LiveSupportChatDialog(onDismiss = { showSupportDialog = false })
         }
    }
}

@Composable
fun LiveSupportChatDialog(onDismiss: () -> Unit) {
    var chatMessageInput by remember { mutableStateOf("") }
    val supportHistory = remember {
        mutableStateListOf(
            Pair("support", "Welcome to the Abhiyantri Support Desk! 🧑‍🚒\nI am Er. Amit Kumar, your assigned Quality Control officer. Tell me if there is any issue with milestone inspection, blueprint clearance, or materials dispatch delays NCR.")
        )
    }
    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(36.dp).background(Emerald.copy(0.15f), CircleShape), contentAlignment = Alignment.Center) {
                    Text("🧑‍🚒", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Abhiyantri Support Live Desk", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Er. Amit Kumar (Active Civil Specialist)", fontSize = 10.sp, color = Emerald)
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth().height(320.dp)) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState)
                        .background(Paper, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    supportHistory.forEach { (sender, text) ->
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
                                        color = if (isUser) Emerald else White,
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
                                    .widthIn(max = 210.dp)
                            ) {
                                Text(
                                    text = text,
                                    color = if (isUser) White else DarkInk2,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = chatMessageInput,
                        onValueChange = { chatMessageInput = it },
                        placeholder = { Text("Describe issue details...", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        maxLines = 1,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Emerald)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        onClick = {
                            if (chatMessageInput.isNotBlank()) {
                                supportHistory.add(Pair("user", chatMessageInput))
                                val text = chatMessageInput.lowercase()
                                chatMessageInput = ""
                                val reply = when {
                                    text.contains("payment") || text.contains("escrow") || text.contains("money") -> "Escrow releases are subject to customer site approval. If a vendor didn't complete work, select 'Quality Escape Escalation' to freeze the payout."
                                    text.contains("blueprint") || text.contains("license") || text.contains("authority") -> "Municipal approvals require submitting site designs & local soil certificates. Our licensed structural consultants will initiate your municipal drawings file within 24 hours."
                                    else -> "Understood. I will flag this immediately to the site manager assigned to your Noida cluster postcode. We'll secure resolution shortly."
                                }
                                supportHistory.add(Pair("support", reply))
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Emerald)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, tint = White, modifier = Modifier.size(14.dp))
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close Chat", color = Slate, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = White,
        shape = RoundedCornerShape(12.dp)
    )
}
