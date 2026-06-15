package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.AppViewModel
import com.example.data.Category
import com.example.data.Vendor
import com.example.ui.components.AppButton
import com.example.ui.components.ButtonType
import com.example.ui.components.StepIndicator
import com.example.ui.theme.*

@Composable
fun BookServiceWizard(
    serviceName: String,
    category: com.example.data.Category,
    viewModel: AppViewModel,
    onSuccessFinished: () -> Unit,
    onCancel: () -> Unit
) {
    var step by remember { mutableStateOf(1) }

    // State parameters
    var selectedLocation by remember { mutableStateOf("Greater Noida, Alpha-2") }
    var selectedSlot by remember { mutableStateOf("10:00 AM – 12:00 PM (Morning)") }
    var fileAttached by remember { mutableStateOf(false) }
    var requirementsDesc by remember { mutableStateOf("") }
    
    // Choose Pro
    val vendors by viewModel.vendors.collectAsStateWithLifecycle()
    val availablePros = vendors.filter { it.category == category.name }
    var selectedProID by remember { mutableStateOf("") }
    val selectedPro = availablePros.find { it.id == selectedProID } ?: (if (availablePros.isNotEmpty()) availablePros[0] else null)

    val pricingEstimate = when (serviceName) {
        "Electrician" -> 400.0
        "Plumber" -> 450.0
        "AC Servicing" -> 599.0
        "Deep Cleaning" -> 1499.0
        "Carpenter" -> 499.0
        else -> 999.0
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .statusBarsPadding()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { if (step > 1 && step < 8) step-- else onCancel() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Booking: $serviceName",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkInk
                    )
                }
                Text(
                    text = "Step $step/8",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Paper)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            
            Column(modifier = Modifier.weight(1f)) {
                StepIndicator(totalSteps = 8, currentStep = step)
                Spacer(modifier = Modifier.height(16.dp))

                when (step) {
                    1 -> {
                        Text("Where do you need the service?", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkInk)
                        Text("Select operational service area in Noida cluster", fontSize = 12.sp, color = Slate)
                        Spacer(modifier = Modifier.height(20.dp))

                        val zones = listOf(
                            "Greater Noida, Alpha-2 Flat 104",
                            "Noida Sector 62, High End Complex",
                            "Ghaziabad, Vaishali Block C",
                            "Delhi, Mayur Vihar Pocket A"
                        )
                        zones.forEach { loc ->
                            val isSel = loc == selectedLocation
                            Card(
                                colors = CardDefaults.cardColors(containerColor = if (isSel) PrimaryPale else White),
                                border = BorderStroke(1.dp, if (isSel) Primary else Fog),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedLocation = loc }
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(selected = isSel, onClick = { selectedLocation = loc }, colors = RadioButtonDefaults.colors(selectedColor = Primary))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(loc, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (isSel) Primary else DarkInk)
                                }
                            }
                        }
                    }

                    2 -> {
                        Text("Choose Date and Preferred Slot", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkInk)
                        Text("Professionals maintain strict punctuality timelines", fontSize = 12.sp, color = Slate)
                        Spacer(modifier = Modifier.height(20.dp))

                        val slots = listOf(
                            "08:00 AM – 10:00 AM (Early Bird)",
                            "10:00 AM – 12:00 PM (Morning)",
                            "01:00 PM – 03:00 PM (Afternoon)",
                            "04:00 PM – 06:00 PM (Evening Slot)"
                        )
                        slots.forEach { slot ->
                            val isSel = slot == selectedSlot
                            Card(
                                colors = CardDefaults.cardColors(containerColor = if (isSel) PrimaryPale else White),
                                border = BorderStroke(1.dp, if (isSel) Primary else Fog),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedSlot = slot }
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Schedule, contentDescription = null, tint = if (isSel) Primary else Slate)
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Text(slot, fontSize = 14.sp, color = if (isSel) Primary else DarkInk, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }

                    3 -> {
                        Text("Do you have reference photos?", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkInk)
                        Text("Upload leaks, electrical boards, or blueprints to expedite assessment.", fontSize = 12.sp, color = Slate)
                        Spacer(modifier = Modifier.height(20.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .background(White, RoundedCornerShape(10.dp))
                                .border(BorderStroke(1.5.dp, Primary.copy(alpha = 0.4f)), RoundedCornerShape(10.dp))
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { fileAttached = !fileAttached },
                            contentAlignment = Alignment.Center
                        ) {
                            if (fileAttached) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Emerald, modifier = Modifier.size(44.dp))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Reference_Photo.jpg attached", color = DarkInk, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text("Click to remove attachment", color = Slate, fontSize = 11.sp)
                                }
                            } else {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Primary, modifier = Modifier.size(44.dp))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Select and Attach Images / PDFs", color = DarkInk, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text("Supports JPEG, PNG, PDF up to 10MB", color = Slate, fontSize = 11.sp)
                                }
                            }
                        }
                    }

                    4 -> {
                        Text("Describe Specific Requirements", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkInk)
                        Text("Mention measurements, material preferences, or priority timelines", fontSize = 12.sp, color = Slate)
                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = requirementsDesc,
                            onValueChange = { requirementsDesc = it },
                            placeholder = { Text("E.g., Need to replace 3 modular board switches in double bedroom. Bring standard copper wiring wire coil...") },
                            modifier = Modifier.fillMaxWidth().height(160.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary)
                        )
                    }

                    5 -> {
                        Text("Select Candidate Professional", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkInk)
                        Text("Certified local experts selected based on rating logs", fontSize = 12.sp, color = Slate)
                        Spacer(modifier = Modifier.height(16.dp))

                        if (availablePros.isEmpty()) {
                            Card(colors = CardDefaults.cardColors(containerColor = PrimaryPale), modifier = Modifier.fillMaxWidth()) {
                                Row(modifier = Modifier.padding(14.dp)) {
                                    Text("No direct contractors registered. We will auto-delegate the order to our vetted top-tier standby technician.", fontSize = 13.sp)
                                }
                            }
                        } else {
                            availablePros.forEach { pro ->
                                val isSel = pro.id == selectedProID
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = if (isSel) PrimaryPale else White),
                                    border = BorderStroke(1.dp, if (isSel) Primary else Fog),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedProID = pro.id }
                                        .padding(vertical = 4.dp)
                                ) {
                                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Box(modifier = Modifier.size(36.dp).background(White, CircleShape), contentAlignment = Alignment.Center) {
                                            Text("🧑", fontSize = 20.sp)
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(pro.businessName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            Row {
                                                Text("★ ${pro.rating}", fontSize = 11.sp, color = Gold, fontWeight = FontWeight.Bold)
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text("${pro.experience}y Exp", fontSize = 11.sp, color = Slate)
                                            }
                                        }
                                        RadioButton(selected = isSel, onClick = { selectedProID = pro.id })
                                    }
                                }
                            }
                        }
                    }

                    6 -> {
                        Text("Review Pricing & Advance Terms", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkInk)
                        Text("Transparent cost estimations following municipal guidelines", fontSize = 12.sp, color = Slate)
                        Spacer(modifier = Modifier.height(20.dp))

                        Card(colors = CardDefaults.cardColors(containerColor = White), border = BorderStroke(1.dp, Fog), modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Base Professional Service Quote", fontSize = 13.sp, color = Slate)
                                    Text("₹$pricingEstimate", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkInk)
                                }
                                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Vetted Inspector Commission (0%)", fontSize = 13.sp, color = Slate)
                                    Text("₹0.00", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkInk)
                                }
                                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("SST / CGST (18%)", fontSize = 13.sp, color = Slate)
                                    val gst = pricingEstimate * 0.18
                                    Text("₹${String.format("%.2f", gst)}", fontSize = 13.sp, color = DarkInk)
                                }
                                Divider(color = Fog, modifier = Modifier.padding(vertical = 8.dp))
                                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Grand Estimated Total", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkInk)
                                    val grand = pricingEstimate * 1.18
                                    Text("₹${String.format("%.2f", grand)}", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Primary, fontFamily = FontFamily.Monospace)
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Box(modifier = Modifier.background(PrimaryPale, RoundedCornerShape(8.dp)).padding(10.dp)) {
                                    Text("🔒 Milestone Rule: Place standard booking by depositing 30% advance escrow (₹${String.format("%.1f", pricingEstimate * 0.3)}). Balance payout directly to pro on completed milestones.", fontSize = 11.sp, color = Primary)
                                }
                            }
                        }
                    }

                    7 -> {
                        Text("Payment Gateway Sandbox", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkInk)
                        Text("Encrypted via Secure Razorpay Integration API", fontSize = 12.sp, color = Slate)
                        Spacer(modifier = Modifier.height(20.dp))

                        Card(colors = CardDefaults.cardColors(containerColor = White), border = BorderStroke(1.dp, Fog), modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Select UPI / Cards Provider", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(12.dp))

                                val gatewayOptions = listOf("GPay UPI / PhonePe Wallet", "Netbanking Over SBI / HDFC", "Debit-Credit Cards Network")
                                gatewayOptions.forEach { opt ->
                                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Payment, contentDescription = null, tint = Primary)
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(opt, fontSize = 13.sp, color = DarkInk3, modifier = Modifier.weight(1f))
                                        RadioButton(selected = opt.contains("GPay"), onClick = {})
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("By clicking below, a simulated UPI payment prompt will occur.", fontSize = 11.sp, color = Slate)
                            }
                        }
                    }

                    8 -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(modifier = Modifier.size(72.dp).background(Emerald.copy(alpha = 0.15f), CircleShape), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Emerald, modifier = Modifier.size(48.dp))
                                }
                                Spacer(modifier = Modifier.height(18.dp))
                                Text("Success! Booking Deposited", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = DarkInk)
                                Text("Your booking has been allocated and recorded safely in database logs.", fontSize = 12.sp, color = Slate, textAlign = TextAlign.Center)
                                
                                Spacer(modifier = Modifier.height(20.dp))
                                Card(colors = CardDefaults.cardColors(containerColor = PrimaryPale)) {
                                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Generated Booking ID", fontSize = 11.sp, color = Slate)
                                        Text("AS2245679", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Primary, fontFamily = FontFamily.Monospace)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Bottom CTA
            if (step < 8) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AppButton(
                        label = "Go Back",
                        onTap = { if (step > 1) step-- else onCancel() },
                        type = ButtonType.OUTLINED,
                        modifier = Modifier.weight(1f)
                    )
                    AppButton(
                        label = if (step == 7) "PAY & CONFIRM" else "CONTINUE",
                        onTap = {
                            if (step == 7) {
                                val user = viewModel.loggedInUser.value
                                val vId = selectedPro?.id ?: "v1"
                                val vName = selectedPro?.businessName ?: "Sharma Construction Co."
                                viewModel.createBooking(
                                    customerId = user?.uid ?: "user_test",
                                    customerName = user?.name ?: "Test User",
                                    vendorId = vId,
                                    vendorName = vName,
                                    service = serviceName,
                                    category = category.name,
                                    dateTime = System.currentTimeMillis() + 86400000 * 2, // 2 days ahead
                                    location = selectedLocation,
                                    requirements = requirementsDesc,
                                    images = if (fileAttached) "photo_attached" else "",
                                    estimatedAmt = pricingEstimate
                                ) {
                                    step = 8
                                }
                            } else {
                                step++
                            }
                        },
                        type = ButtonType.PRIMARY,
                        modifier = Modifier.weight(1.5f)
                    )
                }
            } else {
                AppButton(
                    label = "TRACK BOOKING LIVE",
                    onTap = onSuccessFinished,
                    type = ButtonType.PRIMARY,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun RequestQuoteWizard(
    category: com.example.data.Category,
    viewModel: AppViewModel,
    onSuccessFinished: () -> Unit,
    onCancel: () -> Unit
) {
    var step by remember { mutableStateOf(1) }

    var projectName by remember { mutableStateOf("Duplex Renovation Work") }
    var location by remember { mutableStateOf("Greater Noida, Uttar Pradesh") }
    var budgetRange by remember { mutableStateOf("₹1,00,000 – ₹3,00,000") }
    var descRequirements by remember { mutableStateOf("") }
    var fileAttached by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .statusBarsPadding()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { if (step > 1 && step < 4) step-- else onCancel() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Quote: ${category.name}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkInk
                    )
                }
                Text(
                    text = "Step $step/4",
                    fontSize = 12.sp,
                    color = Primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Paper)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            
            Column(modifier = Modifier.weight(1f)) {
                StepIndicator(totalSteps = 4, currentStep = step)
                Spacer(modifier = Modifier.height(16.dp))

                when (step) {
                    1 -> {
                        Text("Project Summary and Budget Scope", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkInk)
                        Text("Fill brief details to dispatch to verified suppliers", fontSize = 12.sp, color = Slate)
                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = projectName,
                            onValueChange = { projectName = it },
                            label = { Text("Project Name / Title") },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary)
                        )

                        OutlinedTextField(
                            value = location,
                            onValueChange = { location = it },
                            label = { Text("Job Location Site") },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary)
                        )

                        Text("Estimated Budget Window", fontSize = 12.sp, color = Slate)
                        val budgets = listOf("Under ₹50k", "₹50,000 – ₹2,00,000", "₹2,00,000 – ₹5,00,000", "Above ₹5 Lakhs")
                        budgets.forEach { b ->
                            val isSel = budgetRange == b
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { budgetRange = b }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = isSel, onClick = { budgetRange = b })
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(b, fontSize = 13.sp, color = if (isSel) Primary else DarkInk3)
                            }
                        }
                    }

                    2 -> {
                        Text("Define Project Specs & Materials Specs", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkInk)
                        Text("Our technical support engineers will analyze inputs before publishing", fontSize = 12.sp, color = Slate)
                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = descRequirements,
                            onValueChange = { descRequirements = it },
                            placeholder = { Text("Specify layout length, brick quality preference, structural layers specs, etc.") },
                            modifier = Modifier.fillMaxWidth().height(160.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary)
                        )
                    }

                    3 -> {
                        Text("Attach Site Blueprints / Reference Photo", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkInk)
                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .background(White, RoundedCornerShape(8.dp))
                                .border(BorderStroke(1.5.dp, Primary.copy(alpha = 0.3f)), RoundedCornerShape(8.dp))
                                .clickable { fileAttached = !fileAttached },
                            contentAlignment = Alignment.Center
                        ) {
                            if (fileAttached) {
                                Text("✅ Layout_Blueprint.pdf attached (3.4 MB)", fontWeight = FontWeight.Bold, color = Emerald)
                            } else {
                                Text("📁 Click to attach structural file", color = Primary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    4 -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(modifier = Modifier.size(64.dp).background(Emerald.copy(alpha = 0.15f), CircleShape), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Check, tint = Emerald, contentDescription = null, modifier = Modifier.size(44.dp))
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Quote Request Submitted", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = DarkInk)
                                Text("This inquiry has been mapped to our Delhi-NCR registered contractor pool. Expect up to 3 competitive quotations.", fontSize = 12.sp, color = Slate, textAlign = TextAlign.Center)

                                Spacer(modifier = Modifier.height(24.dp))
                                Card(colors = CardDefaults.cardColors(containerColor = PrimaryPale)) {
                                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Generated Inquiry Lead ID", fontSize = 11.sp, color = Slate)
                                        Text("RQ2104725", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Primary, fontFamily = FontFamily.Monospace)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // CTAs
            if (step < 4) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AppButton(
                        label = "Back",
                        onTap = { if (step > 1) step-- else onCancel() },
                        type = ButtonType.OUTLINED,
                        modifier = Modifier.weight(1f)
                    )
                    AppButton(
                        label = if (step == 3) "SUBMIT REQUEST" else "CONTINUE",
                        onTap = {
                            if (step == 3) {
                                val user = viewModel.loggedInUser.value
                                viewModel.createLead(
                                    customerId = user?.uid ?: "user_test",
                                    customerName = user?.name ?: "User Test",
                                    phone = user?.phone ?: "9999988888",
                                    email = user?.email ?: "user@test.com",
                                    service = projectName,
                                    category = category.name,
                                    location = location,
                                    budget = budgetRange,
                                    requirements = descRequirements,
                                    images = if (fileAttached) "attached" else ""
                                ) {
                                    step = 4
                                }
                            } else {
                                step++
                            }
                        },
                        type = ButtonType.PRIMARY,
                        modifier = Modifier.weight(1.5f)
                    )
                }
            } else {
                AppButton(
                    label = "RETURN TO EXPLORE",
                    onTap = onSuccessFinished,
                    type = ButtonType.PRIMARY,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
