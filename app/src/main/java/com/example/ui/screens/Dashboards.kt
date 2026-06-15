package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.animation.core.animateDpAsState
import com.example.R
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.AppViewModel
import com.example.data.Category
import com.example.data.Booking
import com.example.data.Vendor
import com.example.data.Lead
import com.example.data.User
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun VendorDashboardShell(
    viewModel: AppViewModel,
    onLogout: () -> Unit
) {
    val user by viewModel.loggedInUser.collectAsStateWithLifecycle()
    val vendors by viewModel.vendors.collectAsStateWithLifecycle()
    val bookings by viewModel.bookings.collectAsStateWithLifecycle()
    val leads by viewModel.leads.collectAsStateWithLifecycle()

    val currentVendor = vendors.find { it.id == user?.uid } ?: (if (vendors.isNotEmpty()) vendors[0] else null)

    var activeTab by remember { mutableStateOf("dashboard") } // "dashboard" | "leads" | "bookings" | "subscription"

    if (currentVendor == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Error: Vendor Profile Not Mapped in Database.")
                Spacer(modifier = Modifier.height(12.dp))
                AppButton(label = "Logout Session", onTap = onLogout)
            }
        }
        return
    }

    // Filter vendor leads and bookings
    val vendorLeads = leads.filter { it.assignedVendorId == currentVendor.id || it.status == "new" }
    val vendorBookings = bookings.filter { booking -> booking.vendorId == currentVendor.id }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = White, tonalElevation = 6.dp) {
                val tabs = listOf(
                    Triple("dashboard", "Console", Icons.Default.Dashboard),
                    Triple("leads", "Leads (${vendorLeads.size})", Icons.Default.Inbox),
                    Triple("bookings", "Projects (${vendorBookings.size})", Icons.Default.Assignment),
                    Triple("subscription", "Sub Premium", Icons.Default.Star)
                )
                tabs.forEach { (tab, label, icon) ->
                    NavigationBarItem(
                        selected = activeTab == tab,
                        onClick = { activeTab = tab },
                        icon = { Icon(icon, contentDescription = null) },
                        label = { Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold) },
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Paper)
        ) {
            // Hot Notification Strip / Alert (V-01)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Primary.copy(alpha = 0.08f))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).background(Color.Red, CircleShape))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ALERT: Received a direct 3BHK Quote Request under South Delhi Sector 5",
                        color = Primary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            // Enhanced Header with Avatar and Verification Badges (V-01 / V-02)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(PrimaryPale),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_pro_avatar),
                            contentDescription = "Vendor Headshot",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentVendor.businessName,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                color = DarkInk
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            // Gold checkmark verify logo (V-01 / V-07)
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified Seal",
                                tint = Emerald,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("★ ${currentVendor.rating}", fontSize = 11.sp, color = Gold, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .background(Primary, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "GOLD COMPLIANCE",
                                    fontSize = 8.sp,
                                    color = White,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
                
                IconButton(onClick = onLogout) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "logout", tint = Color.Red.copy(alpha = 0.8f))
                }
            }

            // Vendor Dashboard Horizontal Tabs Bar
            ScrollableTabRow(
                selectedTabIndex = when(activeTab) {
                    "dashboard" -> 0
                    "leads" -> 1
                    "bookings" -> 2
                    "verification" -> 3
                    else -> 0
                },
                containerColor = White,
                contentColor = Primary,
                edgePadding = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(selected = activeTab == "dashboard", onClick = { activeTab = "dashboard" }) {
                    Text("Overview Console", modifier = Modifier.padding(14.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Tab(selected = activeTab == "leads", onClick = { activeTab = "leads" }) {
                    Text("Leads Catalogs (${vendorLeads.size})", modifier = Modifier.padding(14.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Tab(selected = activeTab == "bookings", onClick = { activeTab = "bookings" }) {
                    Text("Milestone Planner (${vendorBookings.size})", modifier = Modifier.padding(14.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Tab(selected = activeTab == "verification", onClick = { activeTab = "verification" }) {
                    Text("Docs & Vetting Approval", modifier = Modifier.padding(14.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                when (activeTab) {
                    "dashboard" -> {
                        // V-01 Greeting
                        Text(
                            text = "Namaste, ${currentVendor.ownerName}! 👋",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = DarkInk
                        )
                        Text(
                            text = "Track incoming quote invitations, regional cement prices & milestones settlement.",
                            fontSize = 11.sp,
                            color = Slate,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // V-01 Stat Counter Grid KPI Cards (Leads, Active, Pending, Total Earnings)
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Box(modifier = Modifier.weight(1f).padding(4.dp)) {
                                Card(colors = CardDefaults.cardColors(containerColor = White), border = BorderStroke(1.dp, Fog)) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text("NEW LEADS", fontSize = 9.sp, color = Slate, fontWeight = FontWeight.Bold)
                                        Text("${vendorLeads.size} Active", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Primary)
                                    }
                                }
                            }
                            Box(modifier = Modifier.weight(1f).padding(4.dp)) {
                                Card(colors = CardDefaults.cardColors(containerColor = White), border = BorderStroke(1.dp, Fog)) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text("PENDING QUOTES", fontSize = 9.sp, color = Slate, fontWeight = FontWeight.Bold)
                                        Text("3 Pending", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Gold)
                                    }
                                }
                            }
                        }

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Box(modifier = Modifier.weight(1f).padding(4.dp)) {
                                Card(colors = CardDefaults.cardColors(containerColor = White), border = BorderStroke(1.dp, Fog)) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text("COMPLETED JOBS", fontSize = 9.sp, color = Slate, fontWeight = FontWeight.Bold)
                                        Text("${currentVendor.totalProjects}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Emerald)
                                    }
                                }
                            }
                            Box(modifier = Modifier.weight(1f).padding(4.dp)) {
                                Card(colors = CardDefaults.cardColors(containerColor = White), border = BorderStroke(1.dp, Fog)) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text("TOTAL EARNINGS", fontSize = 9.sp, color = Slate, fontWeight = FontWeight.Bold)
                                        Text("₹${String.format("%,.0f", currentVendor.totalProjects * 18000.0)}", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Emerald, modifier = Modifier.padding(top = 4.dp))
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // V-01 / V-05 Earnings sparkline trend (Custom canvas gradient draw)
                        Card(
                            colors = CardDefaults.cardColors(containerColor = White),
                            border = BorderStroke(1.dp, Fog),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("Weekly Earnings Sparkline", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkInk)
                                        Text("Ledger settlement tracker in INR", fontSize = 11.sp, color = Slate)
                                    }
                                    Text("▲ +18.5% NCR Avg", color = Emerald, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // Canvas sparkline drawing
                                Canvas(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                ) {
                                    val points = listOf(10f, 25f, 15f, 40f, 30f, 55f, 45f)
                                    val widthStep = size.width / (points.size - 1)
                                    val heightMax = size.height
                                    val path = Path()

                                    points.forEachIndexed { idx, value ->
                                        val x = idx * widthStep
                                        val y = heightMax - (value / 60f * heightMax)
                                        if (idx == 0) {
                                            path.moveTo(x, y)
                                        } else {
                                            path.lineTo(x, y)
                                        }
                                    }
                                    
                                    drawPath(
                                        path = path,
                                        color = Primary,
                                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                                            width = 3.dp.toPx(),
                                            cap = StrokeCap.Round
                                        )
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Mon", fontSize = 10.sp, color = Slate)
                                    Text("Wed", fontSize = 10.sp, color = Slate)
                                    Text("Fri", fontSize = 10.sp, color = Slate)
                                    Text("Sun", fontSize = 10.sp, color = Slate)
                                }
                            }
                        }

                        // V-02 Public Preview Mock view inside console (GST tag, team size, etc.)
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Business Public Bio & Profile Vitals (V-02)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(10.dp))

                        Card(
                            colors = CardDefaults.cardColors(containerColor = White),
                            border = BorderStroke(1.dp, Fog),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Owner Operator", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Slate)
                                    Text(currentVendor.ownerName, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DarkInk)
                                }
                                Divider(color = Fog, modifier = Modifier.padding(vertical = 8.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Registered Area", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Slate)
                                    Text(currentVendor.city, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DarkInk)
                                }
                                Divider(color = Fog, modifier = Modifier.padding(vertical = 8.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("GSTIN Status", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Slate)
                                    Text("07AABCA1295F1Z9 (VETTED)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Emerald)
                                }
                                Divider(color = Fog, modifier = Modifier.padding(vertical = 8.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Execution Team Size", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Slate)
                                    Text("12 Structural Engineers & Masons", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DarkInk)
                                }
                            }
                        }

                        // V-06 Reviews Ledger Breakdown Summary
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Customer Reviews Breakdown & Reply Desk (V-06)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(10.dp))

                        Card(
                            colors = CardDefaults.cardColors(containerColor = White),
                            border = BorderStroke(1.dp, Fog),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("${currentVendor.rating}", fontSize = 32.sp, fontWeight = FontWeight.Black, color = DarkInk)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text("Out of 5 Stars score", fontSize = 11.sp, color = Slate)
                                        Row {
                                            for (i in 1..5) {
                                                Icon(Icons.Default.Star, contentDescription = null, tint = Gold, modifier = Modifier.size(14.dp))
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Divider(color = Fog)
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                Text("Latest Customer Review Review Desk:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DarkInk)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("\"Anil Kumar and team finished concrete column layouts beautifully. Escrow payment was disbursed instantly after clear check.\" - Vijay S., Delhi", fontSize = 11.sp, color = Slate)
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Box(
                                    modifier = Modifier
                                        .background(PrimaryPale, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("Your Response: Thank you Vijay-ji, pleasure working with you. Escrow safeguards protect both channels happily.", fontSize = 10.sp, color = Primary, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    "leads" -> {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Escrow Job Leads Catalog (V-03)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("${vendorLeads.size} Leads Active", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Primary)
                        }
                        Text("Describe your specifications to bid on customer blueprint projects directly.", fontSize = 11.sp, color = Slate, modifier = Modifier.padding(bottom = 12.dp))

                        if (vendorLeads.isEmpty()) {
                            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                                Text("No incoming leads matching register categories.", color = Slate)
                            }
                        } else {
                            vendorLeads.forEach { lead ->
                                val isUrgent = lead.budget.contains("90,000") || lead.service.contains("Repair")
                                
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = White),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, if(isUrgent) Gold else Fog),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text(
                                                text = lead.service,
                                                fontWeight = FontWeight.Black,
                                                fontSize = 15.sp,
                                                color = DarkInk
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .background(if (isUrgent) Gold.copy(alpha = 0.2f) else PrimaryPale, RoundedCornerShape(4.dp))
                                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = if (isUrgent) "🚨 HIGH URGENCY" else "⚡ OPEN BID",
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isUrgent) Color.Red else Primary
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(imageVector = Icons.Default.Person, contentDescription = null, modifier = Modifier.size(11.dp), tint = Slate)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Client Name: ${lead.customerName}", fontSize = 12.sp, color = Slate)
                                        }
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(11.dp), tint = Primary)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("📍 Location: ${lead.location}", fontSize = 12.sp, color = Slate)
                                        }
                                        Text("💰 Escrow Budget: ${lead.budget}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Emerald, modifier = Modifier.padding(vertical = 4.dp))
                                        
                                        Divider(color = Fog, modifier = Modifier.padding(vertical = 8.dp))
                                        Text("📝 Details: ${lead.requirements}", fontSize = 11.sp, color = Slate, lineHeight = 14.sp)
                                        
                                        // Lead asset thumbnail strip simulation (V-03)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Box(modifier = Modifier.size(54.dp).clip(RoundedCornerShape(6.dp)).background(Fog)) {
                                                Image(
                                                    painter = painterResource(id = R.drawable.img_house_construction),
                                                    contentDescription = "Blueprint Asset info",
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentScale = ContentScale.Crop
                                                )
                                            }
                                            Box(modifier = Modifier.size(54.dp).clip(RoundedCornerShape(6.dp)).background(Fog)) {
                                                Image(
                                                    painter = painterResource(id = R.drawable.img_map_preview),
                                                    contentDescription = "Location reference",
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentScale = ContentScale.Crop
                                                )
                                            }
                                        }

                                        if (lead.status == "new") {
                                            Spacer(modifier = Modifier.height(12.dp))
                                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                AppButton(
                                                    label = "Decline",
                                                    onTap = { viewModel.declineLead(lead.leadId) },
                                                    type = ButtonType.OUTLINED,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                AppButton(
                                                    label = "Submit Quote & Bid",
                                                    onTap = { viewModel.acceptLead(lead.leadId, currentVendor.id) },
                                                    type = ButtonType.PRIMARY,
                                                    modifier = Modifier.weight(1.5f)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    "bookings" -> {
                        Text("Active Projects Milestone Planner (V-04)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Keep work diaries, trigger stages progress review & escrow payout requests.", fontSize = 11.sp, color = Slate, modifier = Modifier.padding(bottom = 12.dp))

                        // Visual Calendar Day Highlighter (V-04 Schedule)
                        Card(
                            colors = CardDefaults.cardColors(containerColor = White),
                            border = BorderStroke(1.dp, Fog),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("📅 Active Operations Schedule - June 2026", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Slate)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    val days = listOf("15\nMon", "16\nTue", "17\nWed", "18\nThu", "19\nFri", "20\nSat", "21\nSun")
                                    days.forEachIndexed { i, dayText ->
                                        val isHighlighted = i == 2 || i == 3 // Amber color
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isHighlighted) Gold.copy(alpha = 0.35f) else Color.Transparent)
                                                .border(BorderStroke(1.dp, if (isHighlighted) Gold else Fog), RoundedCornerShape(8.dp))
                                                .padding(horizontal = 8.dp, vertical = 6.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = dayText,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isHighlighted) DarkInk else Slate,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Highlighted amber slots show booked RCC layout casting sessions.", fontSize = 9.sp, color = Gold, fontWeight = FontWeight.Bold)
                            }
                        }

                        if (vendorBookings.isEmpty()) {
                            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                                Text("No active booked structural projects loaded.", color = Slate)
                            }
                        } else {
                            vendorBookings.forEach { booking ->
                                var isExpanded by remember { mutableStateOf(false) }
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = White),
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, if (isExpanded) Primary else Fog),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { isExpanded = !isExpanded }
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Column {
                                                Text(booking.service, fontWeight = FontWeight.Black, fontSize = 14.sp)
                                                Text("Project Booking ID: ${booking.bookingId}", fontSize = 11.sp, color = Primary, fontFamily = FontFamily.Monospace)
                                            }
                                            BookingStatusChip(status = booking.status)
                                        }
                                        
                                        if (isExpanded) {
                                            Spacer(modifier = Modifier.height(12.dp))
                                            Text("Client Name: ${booking.customerName}", fontSize = 12.sp, color = Slate)
                                            Text("📍 Location: ${booking.location}", fontSize = 12.sp, color = Slate)
                                            Text("📝 Specifications: ${booking.requirements}", fontSize = 11.sp, color = Slate)
                                            
                                            // Milestone tracker step changer
                                            Spacer(modifier = Modifier.height(14.dp))
                                            Text("Active Milestone Index: Step ${booking.currentMilestoneIndex + 1}/5", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                            
                                            // Photo Upload Tile Showcase (V-04 / V-07)
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Card(
                                                colors = CardDefaults.cardColors(containerColor = Fog),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(12.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = Primary, modifier = Modifier.size(24.dp))
                                                    Spacer(modifier = Modifier.width(12.dp))
                                                    Column {
                                                        Text("Add Site Milestone Progress Photos", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DarkInk)
                                                        Text("Required to disburse current Escrow step release", fontSize = 10.sp, color = Slate)
                                                    }
                                                }
                                            }

                                            Row(
                                                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                if (booking.currentMilestoneIndex < 4) {
                                                    AppButton(
                                                        label = "Disburse Milestone Progress →",
                                                        onTap = {
                                                            viewModel.updateBookingMilestone(booking.bookingId, booking.currentMilestoneIndex + 1)
                                                            if (booking.currentMilestoneIndex + 1 == 4) {
                                                                viewModel.updateBookingStatus(booking.bookingId, "completed")
                                                            } else if (booking.currentMilestoneIndex + 1 == 2) {
                                                                viewModel.updateBookingStatus(booking.bookingId, "in_progress")
                                                            }
                                                        },
                                                        type = ButtonType.PRIMARY,
                                                        modifier = Modifier.weight(1f)
                                                    )
                                                }
                                                AppButton(
                                                    label = "Cancel Operation",
                                                    onTap = { viewModel.updateBookingStatus(booking.bookingId, "cancelled") },
                                                    type = ButtonType.OUTLINED,
                                                    backgroundColor = Color.Red,
                                                    modifier = Modifier.weight(1f)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    "verification" -> {
                        // V-07 Verification and Documents Upload Console
                        Text("V-07 Compliance Records Verification & Subscriptions", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Required for compliance auditing and accessing priority gold escrows.", fontSize = 11.sp, color = Slate, modifier = Modifier.padding(bottom = 12.dp))

                        // Doc Verification Progress Tracker (Step 2 of 4)
                        Card(
                            colors = CardDefaults.cardColors(containerColor = PrimaryPale),
                            border = BorderStroke(1.2.dp, Primary),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("V-07 Step 2 of 4: Document Verification", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Primary)
                                    Box(
                                        modifier = Modifier
                                            .background(Gold, RoundedCornerShape(4.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("UNDER REVIEW", color = White, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Aadhaar PAN linked! Regulatory trade license document is currently inside municipal escrow compliance queue. Time expected: 24-48 Hours.", fontSize = 11.sp, color = DarkInk3)
                            }
                        }

                        // V-07 Upload Slot Tiles
                        Text("Business Upload Slots", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(10.dp))

                        val docTypes = listOf(
                            Pair("Aadhaar Verification KYC Card", "✅ Accepted & Linked ID"),
                            Pair("Company PAN Registration Card", "✅ Accepted & Linked ID"),
                            Pair("Municipal Trade Licence", "⏳ Under compliance queue"),
                            Pair("GSTIN Registration Document", "➕ Tap to edit/upload PDF scan")
                        )

                        docTypes.forEach { (title, status) ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = White),
                                border = BorderStroke(1.dp, Fog),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DarkInk)
                                        Text(status, fontSize = 10.sp, color = if (status.contains("✅")) Emerald else if (status.contains("Under")) Gold else Slate)
                                    }
                                    Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Primary, modifier = Modifier.size(18.dp))
                                }
                            }
                        }

                        // Divider and Plan Tiers
                        Spacer(modifier = Modifier.height(24.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text("Active Billing Levels & Bids Access Limits", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(10.dp))

                        val tiers = listOf(
                            Triple("FREE LEVEL TIER", "₹0/Month", "Access up to 5 regional jobs leads monthly. No verified standard badge inclusion."),
                            Triple("PRO BUNDLE ESCROW", "₹2,500/Month", "Access up to 35 regional leads. Verified STANDARD badge. Automated lead SMS notifications."),
                            Triple("GOLD PREMIUM SECURITY", "₹6,000/Month", "Unlimited leads catalog. GOLD Verified badge. Dedicated consultant engineering assistance, priority banner listing.")
                        )

                        tiers.forEach { (title, cost, desc) ->
                            val isCurrent = currentVendor.subscriptionPlan.contains("free", ignoreCase = true) && title.contains("FREE") ||
                                            currentVendor.subscriptionPlan.contains("pro", ignoreCase = true) && title.contains("PRO") ||
                                            currentVendor.subscriptionPlan.contains("premium", ignoreCase = true) && title.contains("GOLD")

                            Card(
                                colors = CardDefaults.cardColors(containerColor = if (isCurrent) PrimaryPale else White),
                                border = BorderStroke(1.5.dp, if (isCurrent) Primary else Fog),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                              ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkInk)
                                        Text(cost, fontWeight = FontWeight.Black, color = Primary, fontSize = 14.sp)
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(desc, fontSize = 11.sp, color = Slate)
                                    if (isCurrent) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Box(modifier = Modifier.background(Primary, RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 2.dp)) {
                                            Text("YOUR CURRENT ACTIVE BILLING CONTRACT", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = White)
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

@Composable
fun AdminDashboardShell(
    viewModel: AppViewModel,
    onLogout: () -> Unit
) {
    var activeModule by remember { mutableStateOf("dashboard") } // "dashboard" | "vendors" | "leads" | "bookings" | "categories"
    var isSidebarExpanded by remember { mutableStateOf(false) } // Default collapsed for perfect fit on phone screen
    var selectedChartMetric by remember { mutableStateOf("revenue") } // "revenue" | "leads" | "escrow"

    val vendors by viewModel.vendors.collectAsStateWithLifecycle()
    val leads by viewModel.leads.collectAsStateWithLifecycle()
    val bookings by viewModel.bookings.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val customers by viewModel.customers.collectAsStateWithLifecycle()

    val sidebarWidth by animateDpAsState(targetValue = if (isSidebarExpanded) 220.dp else 64.dp, label = "sidebarWidth")

    Row(modifier = Modifier.fillMaxSize().background(Paper).systemBarsPadding()) {
        
        // Collapsible Responsive Navigation Sidebar Left Panel
        Column(
            modifier = Modifier
                .width(sidebarWidth)
                .fillMaxHeight()
                .background(DarkInk2)
                .padding(vertical = 12.dp, horizontal = if (isSidebarExpanded) 12.dp else 6.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header Row with Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp, top = 8.dp),
                    horizontalArrangement = if (isSidebarExpanded) Arrangement.SpaceBetween else Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isSidebarExpanded) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Primary, RoundedCornerShape(4.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("A", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Abhiyantri Central",
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                color = White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Primary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("A", color = White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }

                    if (isSidebarExpanded) {
                        IconButton(onClick = { isSidebarExpanded = false }) {
                            Icon(Icons.Default.MenuOpen, contentDescription = "Collapse", tint = White)
                        }
                    }
                }

                if (!isSidebarExpanded) {
                    IconButton(
                        onClick = { isSidebarExpanded = true },
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = "Expand", tint = White)
                    }
                }

                val menuItems = listOf(
                    Triple("dashboard", "Overview Metrics", Icons.Default.TrendingUp),
                    Triple("vendors", "Vendors Portal", Icons.Default.Engineering),
                    Triple("leads", "Consumer Leads", Icons.Default.BackupTable),
                    Triple("bookings", "All Bookings", Icons.Default.AssignmentTurnedIn),
                    Triple("categories", "Manage Scopes", Icons.Default.Category)
                )

                menuItems.forEach { (mod, label, icon) ->
                    val isSel = activeModule == mod
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(
                                color = if (isSel) Primary else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { activeModule = mod }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = if (isSidebarExpanded) Arrangement.Start else Arrangement.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isSel) White else Mist,
                            modifier = Modifier.size(20.dp)
                        )
                        if (isSidebarExpanded) {
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                color = if (isSel) White else Mist,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            // Collapsible logout button
            if (isSidebarExpanded) {
                AppButton(
                    label = "ADMIN LOGOUT",
                    onTap = onLogout,
                    type = ButtonType.PRIMARY,
                    backgroundColor = Color.Red,
                    modifier = Modifier.fillMaxWidth().height(42.dp)
                )
            } else {
                IconButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .background(Color.Red.copy(alpha = 0.2f), CircleShape)
                        .border(BorderStroke(1.dp, Color.Red), CircleShape)
                        .size(40.dp)
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "logout", tint = Color.Red)
                }
            }
        }

        // Action workspace Panel Right
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            when (activeModule) {
                "dashboard" -> {
                    // Header title area
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Central Analytics Console", fontWeight = FontWeight.Black, fontSize = 22.sp, color = DarkInk)
                            Text("Delhi-NCR real-time transaction indicators & routing streams", fontSize = 11.sp, color = Slate)
                        }
                        
                        // Collapsed notice
                        if (!isSidebarExpanded) {
                            IconButton(
                                onClick = { isSidebarExpanded = true },
                                modifier = Modifier.background(PrimaryPale, CircleShape).size(36.dp)
                            ) {
                                Icon(Icons.Default.Menu, contentDescription = "Expand Sidebar", tint = Primary, modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    // Advanced AI Header Banner
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .padding(bottom = 16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Image(
                                painter = painterResource(id = R.drawable.img_admin_dashboard_banner),
                                contentDescription = "Admin Banner",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            // Dark gradient overlay
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(DarkInk2.copy(alpha = 0.9f), Color.Transparent)
                                        )
                                    )
                            )
                            Column(
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(Primary, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("ADMIN COMMAND v4.2", color = White, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Setu Operations Portal", color = White, fontSize = 18.sp, fontWeight = FontWeight.Black)
                                Text("Continuous quality audit & escrow settlement tracking in room", color = Mist, fontSize = 11.sp)
                            }
                        }
                    }

                    // Scrollable portion of main overview dashboard to fit perfectly on small screens
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Quick Stats
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Box(modifier = Modifier.weight(1f).padding(2.dp)) {
                                AdminStatCard("ACTIVE ENROLLED VENDORS", vendors.size.toString(), "+15%", true, Icons.Default.People)
                            }
                            Box(modifier = Modifier.weight(1f).padding(2.dp)) {
                                AdminStatCard("MONTHLY ACTIVE LEADS", leads.size.toString(), "+42%", true, Icons.Default.Leaderboard)
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                            Box(modifier = Modifier.weight(1f).padding(2.dp)) {
                                AdminStatCard("BOOKINGS DEPOSITS", bookings.size.toString(), "+18%", true, Icons.Default.Task)
                            }
                            Box(modifier = Modifier.weight(1f).padding(2.dp)) {
                                val revenueAccumulated = bookings.sumOf { it.estimatedAmount }
                                AdminStatCard("TOTAL PLATFORM VOLUME", "₹${formatCurrency(revenueAccumulated)}", "+26%", true, Icons.Default.AccountBalanceWallet)
                            }
                        }

                        // Performance Revenue & analytics section (Highly interactive chart)
                        Card(
                            colors = CardDefaults.cardColors(containerColor = White),
                            border = BorderStroke(1.dp, Fog),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Platform Analytics Engine", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkInk)
                                    // Dropdown or metric selector chips
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        listOf("revenue" to "Revenue", "leads" to "Leads Flow", "escrow" to "Locked Escrow").forEach { (id, label) ->
                                            val isSel = selectedChartMetric == id
                                            Box(
                                                modifier = Modifier
                                                    .background(
                                                        color = if (isSel) Primary else Paper,
                                                        shape = RoundedCornerShape(100.dp)
                                                    )
                                                    .border(BorderStroke(1.dp, if (isSel) Primary else Fog), RoundedCornerShape(100.dp))
                                                    .clickable { selectedChartMetric = id }
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = label,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isSel) White else Slate
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Render the Interactive Canvas Chart
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(140.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        val w = size.width
                                        val h = size.height

                                        // Draw Gridlines
                                        drawLine(
                                            color = Fog,
                                            start = Offset(0f, h * 0.25f),
                                            end = Offset(w, h * 0.25f),
                                            strokeWidth = 1f,
                                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                                        )
                                        drawLine(
                                            color = Fog,
                                            start = Offset(0f, h * 0.5f),
                                            end = Offset(w, h * 0.5f),
                                            strokeWidth = 1f,
                                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                                        )
                                        drawLine(
                                            color = Fog,
                                            start = Offset(0f, h * 0.75f),
                                            end = Offset(w, h * 0.75f),
                                            strokeWidth = 1f,
                                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                                        )

                                        // Select points based on metric
                                        val points = when (selectedChartMetric) {
                                            "revenue" -> listOf(
                                                Offset(w * 0.05f, h * 0.85f),
                                                Offset(w * 0.23f, h * 0.70f),
                                                Offset(w * 0.41f, h * 0.80f),
                                                Offset(w * 0.59f, h * 0.45f),
                                                Offset(w * 0.77f, h * 0.30f),
                                                Offset(w * 0.95f, h * 0.15f)
                                            )
                                            "leads" -> listOf(
                                                Offset(w * 0.05f, h * 0.75f),
                                                Offset(w * 0.23f, h * 0.50f),
                                                Offset(w * 0.41f, h * 0.35f),
                                                Offset(w * 0.59f, h * 0.60f),
                                                Offset(w * 0.77f, h * 0.40f),
                                                Offset(w * 0.95f, h * 0.10f)
                                            )
                                            else -> listOf(
                                                Offset(w * 0.05f, h * 0.90f),
                                                Offset(w * 0.23f, h * 0.75f),
                                                Offset(w * 0.41f, h * 0.50f),
                                                Offset(w * 0.59f, h * 0.52f),
                                                Offset(w * 0.77f, h * 0.35f),
                                                Offset(w * 0.95f, h * 0.25f)
                                            )
                                        }

                                        // Draw gradient fill under curve
                                        val fillPath = Path().apply {
                                            moveTo(points.first().x, h)
                                            points.forEach { lineTo(it.x, it.y) }
                                            lineTo(points.last().x, h)
                                            close()
                                        }
                                        drawPath(
                                            path = fillPath,
                                            brush = Brush.verticalGradient(
                                                colors = listOf(Primary.copy(alpha = 0.25f), Color.Transparent)
                                            )
                                        )

                                        // Draw smooth connector line
                                        for (i in 0 until points.size - 1) {
                                            drawLine(
                                                color = Primary,
                                                start = points[i],
                                                end = points[i + 1],
                                                strokeWidth = 4f,
                                                cap = StrokeCap.Round
                                            )
                                            drawCircle(
                                                color = Primary,
                                                radius = 5f,
                                                center = points[i]
                                            )
                                        }
                                        drawCircle(
                                            color = Primary,
                                            radius = 5f,
                                            center = points.last()
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun").forEach { text ->
                                        Text(text = text, fontSize = 9.sp, color = Slate, fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            }
                        }

                        // Advanced Feature: AI-Assisted Smart Estimator & Lead Auditor
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = White),
                            border = BorderStroke(1.dp, Fog),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .background(PrimaryPale, CircleShape)
                                                .size(24.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("🤖", fontSize = 12.sp)
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Delhi-NCR AI Lead Auditor",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = DarkInk
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .background(Emerald.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("ACTIVE MODEL VERIFIED", color = Emerald, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Our platform auditing machine-learning engine automatically scans requirements and estimates if client budgets align with material index rates in Noida & Ghaziabad.",
                                    fontSize = 11.sp,
                                    color = Slate,
                                    lineHeight = 15.sp
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Show audited leads list with stamps
                                val auditedItems = listOf(
                                    Triple("1000 Sq Ft Villa Masonry", "₹8 Lakhs Budget", false),
                                    Triple("Full Plumbing Pipeline Rehab", "₹45k Budget", true),
                                    Triple("Commercial Floor Epoxying", "₹1.5 Lakhs Budget", true)
                                )

                                auditedItems.forEach { (name, budget, isValid) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .background(Paper, RoundedCornerShape(8.dp))
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(name, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DarkInk)
                                            Text("Pre-estimated budget: $budget", fontSize = 10.sp, color = Slate)
                                        }
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            if (isValid) {
                                                Box(
                                                    modifier = Modifier
                                                        .background(Emerald.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text("🟢 SAFE BUDGET", color = Emerald, fontSize = 8.sp, fontWeight = FontWeight.Black)
                                                }
                                            } else {
                                                Box(
                                                    modifier = Modifier
                                                        .background(Color.Red.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text("⚠️ LOW BUDGET STAMP", color = Color.Red, fontSize = 8.sp, fontWeight = FontWeight.Black)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Advanced Feature: Live Milestone Escrow Board
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = White),
                            border = BorderStroke(1.dp, Fog),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Shield, contentDescription = null, tint = Primary, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Prepaid Escrow Release Settlement Console", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    }
                                    Text("LOCKED CODES", color = Slate, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "To guarantee quality, consumer prepaid funds are stored in milestone escrow. As an Administrator, release installments to contractors when structural checks pass.",
                                    color = Slate,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                val lockedEscrows = bookings.filter { it.status == "in_progress" || it.currentMilestoneIndex < 4 }
                                if (lockedEscrows.isEmpty()) {
                                    Text("No active milestone funds locked in escrow.", color = Slate, fontSize = 11.sp, modifier = Modifier.padding(vertical = 8.dp))
                                } else {
                                    lockedEscrows.take(2).forEach { booking ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                                .border(BorderStroke(1.dp, Fog), RoundedCornerShape(8.dp))
                                                .background(White)
                                                .padding(10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(booking.service, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DarkInk)
                                                Text("Contractor: ${booking.vendorName} | Milestone: ${booking.currentMilestoneIndex + 1}/5", fontSize = 10.sp, color = Slate)
                                                Text("Locked Funds: ₹${booking.advancePaid}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Emerald)
                                            }
                                            
                                            AppButton(
                                                label = "Release Mile ${booking.currentMilestoneIndex + 1}",
                                                onTap = {
                                                    val nextIdx = booking.currentMilestoneIndex + 1
                                                    viewModel.updateBookingMilestone(booking.bookingId, nextIdx)
                                                    if (nextIdx >= 4) {
                                                        viewModel.updateBookingStatus(booking.bookingId, "completed")
                                                    }
                                                },
                                                type = ButtonType.PRIMARY,
                                                modifier = Modifier.height(34.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "vendors" -> {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Registered Contractors Roster", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("${vendors.size} Active Online", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Emerald)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Toggle Expandable Registration form for new Vendor
                    var showAddForm by remember { mutableStateOf(false) }
                    
                    AppButton(
                        label = if (showAddForm) "Collapse Form Panel" else "➕ ADD NEW VENDOR PROFILE INTO ROOM",
                        onTap = { showAddForm = !showAddForm },
                        type = ButtonType.OUTLINED,
                        modifier = Modifier.fillMaxWidth().height(42.dp)
                    )

                    AnimatedVisibility(visible = showAddForm) {
                        AddVendorFormPanel(viewModel = viewModel) { showAddForm = false }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(vendors) { vendor ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = White),
                                border = BorderStroke(1.dp, Fog),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(vendor.businessName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text("Owner Name: ${vendor.ownerName} | ID: ${vendor.id}", fontSize = 11.sp, color = Slate)
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("Category: ${vendor.category}", fontSize = 11.sp, color = Primary, fontWeight = FontWeight.Bold)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("★ ${vendor.rating}", fontSize = 11.sp, color = Gold, fontWeight = FontWeight.Bold)
                                        }
                                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                                            Text("Verified Status: ", fontSize = 11.sp)
                                            val currentLevelLabel = vendor.verificationLevel.uppercase()
                                            Text(
                                                text = currentLevelLabel,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (vendor.isVerified) Emerald else Color.Gray
                                            )
                                        }
                                    }
                                    
                                    Column(horizontalAlignment = Alignment.End) {
                                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            // Upgrade button
                                            IconButton(onClick = {
                                                val nextLvl = if (vendor.verificationLevel == "gold") "none" else "gold"
                                                viewModel.updateVendorVerification(vendor.id, nextLvl, nextLvl != "none")
                                            }) {
                                                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Primary)
                                            }
                                            // Delete button
                                            IconButton(onClick = { viewModel.deleteVendor(vendor.id) }) {
                                                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "leads" -> {
                    Text("Central Consumer Demands Table", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkInk)
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(leads) { lead ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = White),
                                border = BorderStroke(1.dp, Fog),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(lead.service, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text(lead.status.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Primary)
                                    }
                                    Text("Routable Site location: ${lead.location}", fontSize = 12.sp, color = Slate)
                                    Text("Consumer Phone: ${lead.phone} | email: ${lead.email}", fontSize = 11.sp, color = Slate)
                                    Text("Budget Scale: ${lead.budget}", fontSize = 11.sp, color = Emerald, fontWeight = FontWeight.Bold)
                                    
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        listOf("contacted", "quote_sent", "converted", "lost").forEach { st ->
                                            Text(
                                                text = st.uppercase(),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (lead.status == st) Primary else Color.Gray,
                                                modifier = Modifier
                                                    .background(if (lead.status == st) PrimaryPale else Fog, RoundedCornerShape(4.dp))
                                                    .clickable { viewModel.updateLeadStatus(lead.leadId, st) }
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.weight(1f))
                                        IconButton(onClick = { viewModel.deleteLead(lead.leadId) }, modifier = Modifier.size(24.dp)) {
                                            Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "bookings" -> {
                    Text("Consolidated Job Bookings Registers", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkInk)
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(bookings) { booking ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = White),
                                border = BorderStroke(1.dp, Fog),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(booking.service, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        BookingStatusChip(status = booking.status)
                                    }
                                    Text("Assigned Pro Contractor: ${booking.vendorName}", fontSize = 12.sp, color = Slate)
                                    Text("Escrow Ledger: Estimated ₹${booking.estimatedAmount} | Prepaid Advance ₹${booking.advancePaid}", fontSize = 11.sp, color = Slate)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        AppButton(
                                            label = "Force Complete",
                                            onTap = { viewModel.updateBookingStatus(booking.bookingId, "completed") },
                                            type = ButtonType.PRIMARY,
                                            modifier = Modifier.height(30.dp)
                                        )
                                        AppButton(
                                            label = "Revoke / Delete",
                                            onTap = { viewModel.deleteBooking(booking.bookingId) },
                                            type = ButtonType.OUTLINED,
                                            backgroundColor = Color.Red,
                                            modifier = Modifier.height(30.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                "categories" -> {
                    Text("Platforms Operational Categories Management", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkInk)
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(categories) { cat ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = White),
                                border = BorderStroke(1.dp, Fog),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(cat.icon, fontSize = 24.sp)
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(cat.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            Text("${cat.servicesCount} sub-services online", fontSize = 11.sp, color = Slate)
                                        }
                                    }
                                    Switch(checked = true, onCheckedChange = {})
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
fun AddVendorFormPanel(
    viewModel: AppViewModel,
    onFinished: () -> Unit
) {
    var bizName by remember { mutableStateOf("") }
    var ownerName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var categorySelection by remember { mutableStateOf("Home Construction & Renovation") }
    var subCats by remember { mutableStateOf("New House Construction,Home Renovation,House Extension") }
    var exp by remember { mutableStateOf("5") }
    var desc by remember { mutableStateOf("Enterprise engineering builders") }
    var planSelect by remember { mutableStateOf("pro") }
    var verifiedLevelSelect by remember { mutableStateOf("standard") }

    Card(
        colors = CardDefaults.cardColors(containerColor = White),
        border = BorderStroke(1.5.dp, Primary),
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text("Insert Mock Vendor Form", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(value = bizName, onValueChange = { bizName = it }, label = { Text("Biz/Company Legal Name") }, modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp))
            OutlinedTextField(value = ownerName, onValueChange = { ownerName = it }, label = { Text("Proprietor Full Name") }, modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp))
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Contact Mobile Number") }, modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Business Email") }, modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp))

            Text("Prime Category Scope", fontSize = 11.sp)
            val scopeCatChoices = listOf("Home Construction & Renovation", "Interior & Finishing", "Electrical Services", "Plumbing Services")
            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                var menuOpen by remember { mutableStateOf(false) }
                OutlinedButton(onClick = { menuOpen = true }, modifier = Modifier.fillMaxWidth()) {
                    Text(categorySelection)
                }
                DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                    scopeCatChoices.forEach { ch ->
                        DropdownMenuItem(text = { Text(ch) }, onClick = { categorySelection = ch; menuOpen = false })
                    }
                }
            }

            AppButton(
                label = "COMMIT RECORD TO ROOM",
                onTap = {
                    if (bizName.isNotEmpty()) {
                        viewModel.addVendor(
                            businessName = bizName,
                            ownerName = ownerName,
                            phone = phone,
                            email = email,
                            category = categorySelection,
                            subCategories = subCats,
                            city = "Greater Noida",
                            experience = exp.toIntOrNull() ?: 5,
                            description = desc,
                            plan = planSelect,
                            verifiedLevel = verifiedLevelSelect
                        )
                        onFinished()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

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
