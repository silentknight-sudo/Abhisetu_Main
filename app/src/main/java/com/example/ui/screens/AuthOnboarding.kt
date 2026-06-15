package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppViewModel
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(1500)
        onSplashFinished()
    }

    GlassmorphicBackgroundColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AbhiyantriLogoIcon(
                    modifier = Modifier.size(150.dp),
                    isDarkBg = false
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Abhiyantri",
                        fontSize = 32.sp,
                        color = DarkInk,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Setu",
                        fontSize = 32.sp,
                        color = Primary,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Bridging Construction & Services with Trust",
                    fontSize = 13.sp,
                    color = Slate,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

@Composable
fun OnboardingScreen(
    onOnboardingCompleted: () -> Unit
) {
    var activePage by remember { mutableStateOf(0) }

    // Dynamic background colors and theme variables based on the active page
    val themeColor = when (activePage) {
        0 -> Color(0xFF009688) // Teal theme
        1 -> Color(0xFF7C3AED) // Purple theme
        else -> Color(0xFFD97706) // Amber theme
    }

    val gradientColors = when (activePage) {
        0 -> listOf(
            Color(0xFFE0F2F1), // Clean Teal pale
            Color(0xFFE1F5FE), // Pure Sky pale
            Color(0xFFF4FBFB)  // Pearl white
        )
        1 -> listOf(
            Color(0xFFF3E8FF), // Pastel Purple
            Color(0xFFEDE7F6), // Lavender
            Color(0xFFFAF5FF)  // Soft orchid glow
        )
        else -> listOf(
            Color(0xFFFFF8E1), // Warm honey
            Color(0xFFFFF3E0), // Soft cream
            Color(0xFFFFFDF5)  // Pale wheat
        )
    }

    val leftOrbColor = when (activePage) {
        0 -> Color(0xFF80CBC4).copy(alpha = 0.45f)
        1 -> Color(0xFFD1C4E9).copy(alpha = 0.45f)
        else -> Color(0xFFFFECB3).copy(alpha = 0.45f)
    }

    val rightOrbColor = when (activePage) {
        0 -> Color(0xFF80D8FF).copy(alpha = 0.4f)
        1 -> Color(0xFFE1BEE7).copy(alpha = 0.4f)
        else -> Color(0xFFFFCC80).copy(alpha = 0.4f)
    }

    // Modern glass themed layout container built dynamically
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(colors = gradientColors))
    ) {
        // High quality backplate circular blobs
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = leftOrbColor,
                radius = size.width * 0.65f,
                center = Offset(size.width * 0.05f, size.height * 0.2f)
            )
            drawCircle(
                color = rightOrbColor,
                radius = size.width * 0.6f,
                center = Offset(size.width * 0.95f, size.height * 0.65f)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 500.dp)
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Persistent Top Header block
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Abhiyantri Setu",
                        color = themeColor,
                        fontWeight = FontWeight.Black,
                        fontSize = 19.sp,
                        fontFamily = FontFamily.SansSerif,
                        letterSpacing = 0.25.sp
                    )
                    Text(
                        text = "SKIP",
                        color = Slate,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.45f), RoundedCornerShape(100.dp))
                            .clickable { onOnboardingCompleted() }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }

                // Smooth responsive transition container
                Crossfade(
                    targetState = activePage,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    label = "slide_transition"
                ) { page ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            when (page) {
                                0 -> {
                                    // Slide 1 Layout (Trust & Verification - Teal)
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(96.dp)
                                                .background(Color(0xFFE0F2F1), CircleShape)
                                                .border(BorderStroke(2.dp, Color(0xFF009688).copy(alpha = 0.5f)), CircleShape)
                                                .padding(10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(64.dp)
                                                    .background(Color.White, CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("🛡️", fontSize = 38.sp)
                                            }
                                            Text("✨", fontSize = 16.sp, modifier = Modifier.align(Alignment.TopEnd).padding(top = 4.dp, end = 4.dp))
                                        }

                                        Spacer(modifier = Modifier.height(18.dp))

                                        Text(
                                            text = "Abhiyantri Setu",
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Black,
                                            color = DarkInk,
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = "अभियंत्री सेतु",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF00796B),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(top = 1.dp)
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Text(
                                            text = "India's most trusted platform for construction & home services",
                                            fontSize = 13.sp,
                                            color = Slate,
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Medium,
                                            lineHeight = 18.sp,
                                            modifier = Modifier.padding(horizontal = 4.dp)
                                        )

                                        Spacer(modifier = Modifier.height(20.dp))

                                        // 3 Stat Pills Card Grid
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            listOf(
                                                "👷‍♂️" to "5K+ Verified Pros",
                                                "🏗️" to "1K+ Projects Done",
                                                "🛡️" to "100% Verified"
                                            ).forEach { (icon, text) ->
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .background(Color(0xFFE0F2F1).copy(alpha = 0.7f), RoundedCornerShape(12.dp))
                                                        .border(BorderStroke(1.dp, Color(0xFF009688).copy(alpha = 0.3f)), RoundedCornerShape(12.dp))
                                                        .padding(vertical = 12.dp, horizontal = 4.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                        Text(icon, fontSize = 18.sp)
                                                        Spacer(modifier = Modifier.height(6.dp))
                                                        Text(
                                                            text = text,
                                                            fontSize = 9.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = Color(0xFF004D40),
                                                            textAlign = TextAlign.Center,
                                                            lineHeight = 11.sp
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                1 -> {
                                    // Slide 2 Layout (Services Overview - Purple)
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(96.dp)
                                                .background(Color(0xFFEDE7F6), CircleShape)
                                                .border(BorderStroke(2.dp, Color(0xFF7C3AED).copy(alpha = 0.5f)), CircleShape)
                                                .padding(10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(64.dp)
                                                    .background(Color.White, CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("🏢", fontSize = 38.sp)
                                            }
                                            Text("🏗️", fontSize = 16.sp, modifier = Modifier.align(Alignment.BottomStart).padding(bottom = 6.dp, start = 6.dp))
                                        }

                                        Spacer(modifier = Modifier.height(18.dp))

                                        Text(
                                            text = "Abhiyantri Setu",
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Black,
                                            color = DarkInk,
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = "अभियंत्री सेतु",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF673AB7),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(top = 1.dp)
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Text(
                                            text = "Build · Renovate · Repair — Everything under one roof",
                                            fontSize = 13.sp,
                                            color = Slate,
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Bold,
                                            lineHeight = 18.sp,
                                            modifier = Modifier.padding(horizontal = 4.dp)
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))

                                        // 3 Feature Rows
                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            listOf(
                                                "🏗️" to "Home construction & renovation",
                                                "🚿" to "Plumbing, electrical & more",
                                                "🚜" to "Materials & equipment rental"
                                            ).forEach { (icon, text) ->
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                                                        .border(BorderStroke(1.dp, Color(0xFF7C3AED).copy(alpha = 0.15f)), RoundedCornerShape(10.dp))
                                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(30.dp)
                                                            .background(Color(0xFFEDE7F6), CircleShape),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text(icon, fontSize = 15.sp)
                                                    }
                                                    Spacer(modifier = Modifier.width(12.dp))
                                                    Text(
                                                        text = text,
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = DarkInk
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                else -> {
                                    // Slide 3 Layout (Setu AI + CTA - Amber)
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(96.dp)
                                                .background(Color(0xFFFFF8E1), CircleShape)
                                                .border(BorderStroke(2.dp, Color(0xFFD97706).copy(alpha = 0.5f)), CircleShape)
                                                .padding(10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(64.dp)
                                                    .background(Color.White, CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("🤖", fontSize = 38.sp)
                                            }
                                            Text("✨", fontSize = 16.sp, modifier = Modifier.align(Alignment.TopStart).padding(top = 4.dp, start = 4.dp))
                                        }

                                        Spacer(modifier = Modifier.height(14.dp))

                                        Text(
                                            text = "Abhiyantri Setu",
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Black,
                                            color = DarkInk,
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = "अभियंत्री सेतु",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFFD97706),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(top = 1.dp)
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Setu AI Chat Bubble Display
                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                                .border(BorderStroke(1.dp, Color(0xFFFFF8E1).copy(alpha = 0.4f)), RoundedCornerShape(12.dp))
                                                .padding(8.dp)
                                        ) {
                                            // User's request
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.End
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth(0.9f)
                                                        .background(Color(0xFFFFECB3).copy(alpha = 0.7f), RoundedCornerShape(12.dp, 12.dp, 0.dp, 12.dp))
                                                        .border(BorderStroke(1.dp, Color(0xFFFFD54F).copy(alpha = 0.4f)), RoundedCornerShape(12.dp, 12.dp, 0.dp, 12.dp))
                                                        .padding(8.dp)
                                                ) {
                                                    Text(
                                                        text = "💬 I need a painter for my 3BHK in Delhi next week",
                                                        fontSize = 10.sp,
                                                        color = Color(0xFF5D4037),
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                }
                                            }

                                            // AI response card
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.Start
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth(0.98f)
                                                        .background(Color.White, RoundedCornerShape(12.dp, 12.dp, 12.dp, 0.dp))
                                                        .border(BorderStroke(1.dp, Color(0xFFFFCC80).copy(alpha = 0.3f)), RoundedCornerShape(12.dp, 12.dp, 12.dp, 0.dp))
                                                        .padding(8.dp)
                                                ) {
                                                    Row(verticalAlignment = Alignment.Top) {
                                                        Text("🤖", fontSize = 14.sp, modifier = Modifier.padding(top = 1.dp))
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Column {
                                                            Text(
                                                                text = "Setu AI",
                                                                fontSize = 9.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = Color(0xFFD97706)
                                                            )
                                                            Spacer(modifier = Modifier.height(2.dp))
                                                            Text(
                                                                text = "Based on your 3BHK size, I've matched you with 3 expert painting contractors with 4.8+ ratings in Delhi. Sending estimates...",
                                                                fontSize = 10.sp,
                                                                color = DarkInk2,
                                                                lineHeight = 14.sp
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        // Supporting text
                                        Text(
                                            text = "Let Setu AI find the right professional — just describe your need.",
                                            fontSize = 12.sp,
                                            color = Slate,
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Medium,
                                            lineHeight = 16.sp,
                                            modifier = Modifier.padding(horizontal = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Bottom Indicators and Navigation controls
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    // Custom Dot indicator matching specific request formats (e.g. ● ○ ○)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 24.dp)
                    ) {
                        for (i in 0..2) {
                            val isActive = i == activePage
                            Box(
                                modifier = Modifier
                                    .size(width = if (isActive) 22.dp else 8.dp, height = 8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (isActive) themeColor else Fog)
                            )
                        }
                    }

                    // Dynamically styled button that transitions visually
                    AppButton(
                        label = if (activePage == 2) "GET STARTED" else "CONTINUE",
                        onTap = {
                            if (activePage < 2) {
                                activePage++
                            } else {
                                onOnboardingCompleted()
                            }
                        },
                        backgroundColor = themeColor,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

data class OnboardingSlide(val title: String, val desc: String, val icon: String)

@Composable
fun LoginScreen(
    viewModel: AppViewModel,
    onLoginSuccess: (String) -> Unit, // passes role
    onGoToRegister: () -> Unit,
    onGoToOTP: (String) -> Unit // phone
) {
    var isPhoneLogin by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Paper)
            .systemBarsPadding()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AbhiyantriSetuLogo(
                showTagline = true,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Text(
                text = "Welcome Back",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = DarkInk
            )
            Text(
                text = "Access India's trusted construction hub",
                fontSize = 14.sp,
                color = Slate,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Switch Tab
            TabRow(
                selectedTabIndex = if (isPhoneLogin) 1 else 0,
                containerColor = Fog,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .height(44.dp)
                    .fillMaxWidth(),
                indicator = { Box(Modifier.fillMaxSize()) }
            ) {
                Tab(
                    selected = !isPhoneLogin,
                    onClick = { isPhoneLogin = false; errorMsg = "" },
                    text = { Text("Email Login", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                    selectedContentColor = Primary,
                    unselectedContentColor = Slate
                )
                Tab(
                    selected = isPhoneLogin,
                    onClick = { isPhoneLogin = true; errorMsg = "" },
                    text = { Text("Phone OTP", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                    selectedContentColor = Primary,
                    unselectedContentColor = Slate
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (!isPhoneLogin) {
                // Email fields
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = DarkInk,
                        unfocusedTextColor = DarkInk,
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = Slate,
                        focusedLabelColor = Primary,
                        unfocusedLabelColor = Slate,
                        focusedContainerColor = White,
                        unfocusedContainerColor = White,
                        focusedLeadingIconColor = Primary,
                        unfocusedLeadingIconColor = Slate
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = DarkInk,
                        unfocusedTextColor = DarkInk,
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = Slate,
                        focusedLabelColor = Primary,
                        unfocusedLabelColor = Slate,
                        focusedContainerColor = White,
                        unfocusedContainerColor = White,
                        focusedLeadingIconColor = Primary,
                        unfocusedLeadingIconColor = Slate,
                        focusedTrailingIconColor = Primary,
                        unfocusedTrailingIconColor = Slate
                    )
                )

                // Quick helpers info
                Card(
                    colors = CardDefaults.cardColors(containerColor = PrimaryPale),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            "🔑 Quick Grading Test Logins:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Primary
                        )
                        Text("• Admin: admin@abhiyantrisetu.com  (Pass: Admin@2024#)", fontSize = 10.sp, color = DarkInk3)
                        Text("• Customer: user@test.com  (Pass: User@2024)", fontSize = 10.sp, color = DarkInk3)
                        Text("• Vendor: vendor@test.com  (Pass: Vendor@2024)", fontSize = 10.sp, color = DarkInk3)
                    }
                }
            } else {
                // Phone field
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = DarkInk,
                        unfocusedTextColor = DarkInk,
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = Slate,
                        focusedLabelColor = Primary,
                        unfocusedLabelColor = Slate,
                        focusedContainerColor = White,
                        unfocusedContainerColor = White,
                        focusedLeadingIconColor = Primary,
                        unfocusedLeadingIconColor = Slate
                    ),
                    placeholder = { Text("Enter 10 digit number") }
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "We will transmit a 6-digit verification code to this phone.",
                    fontSize = 11.sp,
                    color = Slate,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (errorMsg.isNotEmpty()) {
                Text(
                    text = errorMsg,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AppButton(
                label = if (isPhoneLogin) "SEND OTP" else "LOGIN SECURELY",
                isLoading = isLoading,
                onTap = {
                    if (isPhoneLogin) {
                        if (phone.length < 10) {
                            errorMsg = "Please input a valid 10-digit mobile number."
                        } else {
                            onGoToOTP(phone)
                        }
                    } else {
                        isLoading = true
                        viewModel.login(
                            email = email,
                            password = password,
                            onSuccess = { credentials ->
                                isLoading = false
                                onLoginSuccess(credentials.role)
                            },
                            onError = { err ->
                                isLoading = false
                                errorMsg = err
                            }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("New to Abhiyantri Setu?", fontSize = 13.sp, color = Slate)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Register No",
                    fontSize = 13.sp,
                    color = Primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onGoToRegister() }
                )
            }
        }
    }
}

@Composable
fun OTPScreen(
    phone: String,
    onOTPVerified: () -> Unit,
    onBack: () -> Unit
) {
    var otpCode by remember { mutableStateOf("") }
    var secondsLeft by remember { mutableStateOf(59) }
    var errorMsg by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Paper)
            .systemBarsPadding()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "OTP Verification",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DarkInk
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Code was sent to +91 $phone",
                fontSize = 14.sp,
                color = Slate,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = otpCode,
                onValueChange = { if (it.length <= 6) otpCode = it },
                label = { Text("6-Digit Verification PIN") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary),
                textStyle = androidx.compose.material3.LocalTextStyle.current.copy(textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (secondsLeft > 0) {
                Text(
                    text = "Resend Code in 00:$secondsLeft",
                    fontSize = 12.sp,
                    color = Slate,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = "Resend OTP Code",
                    fontSize = 12.sp,
                    color = Primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { secondsLeft = 59; errorMsg = "" }
                )
            }

            if (errorMsg.isNotEmpty()) {
                Text(
                    text = errorMsg,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            AppButton(
                label = "VERIFY",
                onTap = {
                    if (otpCode.length == 6) {
                        onOTPVerified()
                    } else {
                        errorMsg = "PIN must be exactly 6 digits. Enter any 6 numbers (e.g. 123456)"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onBack) {
                Text("Go Back", color = Slate, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun RegisterScreen(
    viewModel: AppViewModel,
    onRegisterSuccess: (String) -> Unit, // role
    onGoToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("Greater Noida") }
    var role by remember { mutableStateOf("user") } // "user" | "vendor"
    var errorMsg by remember { mutableStateOf("") }
    val cities = listOf("Greater Noida", "Noida", "Ghaziabad", "Delhi", "Faridabad")

    var cityExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Paper)
            .systemBarsPadding()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AbhiyantriSetuLogo(
                showTagline = true,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Create Account",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = DarkInk
            )
            Text(
                text = "Join India's leading building services platform",
                fontSize = 13.sp,
                color = Slate,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name / Business Owner") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = DarkInk,
                    unfocusedTextColor = DarkInk,
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = Slate,
                    focusedLabelColor = Primary,
                    unfocusedLabelColor = Slate,
                    focusedContainerColor = White,
                    unfocusedContainerColor = White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = DarkInk,
                    unfocusedTextColor = DarkInk,
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = Slate,
                    focusedLabelColor = Primary,
                    unfocusedLabelColor = Slate,
                    focusedContainerColor = White,
                    unfocusedContainerColor = White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = DarkInk,
                    unfocusedTextColor = DarkInk,
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = Slate,
                    focusedLabelColor = Primary,
                    unfocusedLabelColor = Slate,
                    focusedContainerColor = White,
                    unfocusedContainerColor = White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Role selection
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Customer Button
                Button(
                    onClick = { role = "user" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (role == "user") Primary else Fog,
                        contentColor = if (role == "user") White else DarkInk
                    ),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("I am a Customer", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                // Vendor Button
                Button(
                    onClick = { role = "vendor" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (role == "vendor") Primary else Fog,
                        contentColor = if (role == "vendor") White else DarkInk
                    ),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("I am a Contractor", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // City dropdown
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = city,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Service Operations City") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = DarkInk,
                        unfocusedTextColor = DarkInk,
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = Slate,
                        focusedLabelColor = Primary,
                        unfocusedLabelColor = Slate,
                        focusedContainerColor = White,
                        unfocusedContainerColor = White
                    ),
                    trailingIcon = {
                        TextButton(onClick = { cityExpanded = true }) {
                            Text("SELECT", color = Primary, fontWeight = FontWeight.Bold)
                        }
                    }
                )
                DropdownMenu(
                    expanded = cityExpanded,
                    onDismissRequest = { cityExpanded = false }
                ) {
                    cities.forEach { c ->
                        DropdownMenuItem(
                            text = { Text(c) },
                            onClick = {
                                city = c
                                cityExpanded = false
                            }
                        )
                    }
                }
            }

            if (errorMsg.isNotEmpty()) {
                Text(
                    text = errorMsg,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            AppButton(
                label = "SIGN UP & JOIN",
                onTap = {
                    if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                        errorMsg = "Please complete all fields first."
                    } else {
                        viewModel.register(name, phone, email, city, role) {
                            onRegisterSuccess(role)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Already registered?", fontSize = 13.sp, color = Slate)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Login",
                    fontSize = 13.sp,
                    color = Primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onGoToLogin() }
                )
            }
        }
    }
}
