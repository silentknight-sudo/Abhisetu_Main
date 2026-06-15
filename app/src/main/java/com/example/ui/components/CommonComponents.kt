package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

enum class ButtonType { PRIMARY, OUTLINED, GHOST }
enum class VerifiedLevel { GOLD, STANDARD, NONE }

@Composable
fun AppButton(
    label: String,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.PRIMARY,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    backgroundColor: Color = Primary,
    contentColor: Color = White
) {
    val shape = RoundedCornerShape(10.dp)
    
    when (type) {
        ButtonType.PRIMARY -> {
            Button(
                onClick = onTap,
                colors = ButtonDefaults.buttonColors(
                    containerColor = backgroundColor,
                    contentColor = contentColor
                ),
                shape = shape,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                modifier = modifier.heightIn(min = 48.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = contentColor,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        icon?.let {
                            Icon(it, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            text = label,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
            }
        }
        ButtonType.OUTLINED -> {
            OutlinedButton(
                onClick = onTap,
                border = BorderStroke(1.5.dp, backgroundColor),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = backgroundColor
                ),
                shape = shape,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                modifier = modifier.heightIn(min = 48.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = backgroundColor,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        icon?.let {
                            Icon(it, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            text = label,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }
            }
        }
        ButtonType.GHOST -> {
            TextButton(
                onClick = onTap,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = backgroundColor
                ),
                shape = shape,
                modifier = modifier.heightIn(min = 48.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    icon?.let {
                        Icon(it, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = label,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }
        }
    }
}

@Composable
fun RatingBar(
    rating: Double,
    reviewCount: Int = 0,
    modifier: Modifier = Modifier,
    starSize: Int = 14
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        val filledStars = rating.toInt()
        val hasHalf = (rating - filledStars) >= 0.5
        
        for (i in 1..5) {
            val starIcon = when {
                i <= filledStars -> Icons.Default.Star
                i == filledStars + 1 && hasHalf -> Icons.Default.StarHalf
                else -> Icons.Default.StarOutline
            }
            Icon(
                imageVector = starIcon,
                contentDescription = null,
                tint = Gold,
                modifier = Modifier.size(starSize.dp)
            )
        }
        if (reviewCount > 0) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "($reviewCount reviews)",
                fontSize = (starSize - 2).sp,
                color = Slate,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}

@Composable
fun QuickServiceCard(
    name: String,
    startingPrice: String,
    duration: String,
    rating: Double,
    iconChar: String = "⚡",
    onBook: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Fog),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .width(160.dp)
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(PrimaryPale, CircleShape)
                    .align(Alignment.Start),
                contentAlignment = Alignment.Center
            ) {
                Text(iconChar, fontSize = 22.sp)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = DarkInk,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Text(
                text = "Starting from $startingPrice",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Primary,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            
            Text(
                text = duration,
                fontSize = 11.sp,
                color = Slate
            )
            
            Spacer(modifier = Modifier.height(6.dp))
            
            RatingBar(rating = rating, starSize = 12)
            
            Spacer(modifier = Modifier.height(12.dp))
            
            AppButton(
                label = "Book Now",
                onTap = onBook,
                type = ButtonType.PRIMARY,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
            )
        }
    }
}

@Composable
fun VerifiedBadge(
    level: VerifiedLevel,
    modifier: Modifier = Modifier
) {
    if (level == VerifiedLevel.NONE) return
    
    val bgColor = if (level == VerifiedLevel.GOLD) Gold.copy(alpha = 0.15f) else Emerald.copy(alpha = 0.15f)
    val textColor = if (level == VerifiedLevel.GOLD) Gold else Emerald
    val textLabel = if (level == VerifiedLevel.GOLD) "GOLD VERIFIED" else "VERIFIED"
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(bgColor, RoundedCornerShape(100.dp))
            .border(BorderStroke(1.dp, textColor.copy(alpha = 0.3f)), RoundedCornerShape(100.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Verified,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = textLabel,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
fun VendorCard(
    vendorName: String,
    category: String,
    verifiedLevel: VerifiedLevel,
    rating: Double,
    reviewCount: Int,
    experience: Int,
    projectCount: Int,
    location: String,
    onViewProfile: () -> Unit,
    onRequestQuote: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Fog),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(PrimaryPale, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (vendorName.isNotEmpty()) vendorName.first().toString() else "V",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = vendorName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = DarkInk
                        )
                        Text(
                            text = category,
                            fontSize = 12.sp,
                            color = Slate
                        )
                    }
                }
                
                VerifiedBadge(level = verifiedLevel)
            }
            
            Spacer(modifier = Modifier.height(10.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Rating", fontSize = 11.sp, color = Mist)
                    RatingBar(rating = rating, reviewCount = reviewCount, starSize = 12)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Experience", fontSize = 11.sp, color = Mist)
                    Text("$experience+ Years", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkInk)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Projects Done", fontSize = 11.sp, color = Mist)
                    Text("$projectCount+", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkInk)
                }
            }
            
            Spacer(modifier = Modifier.height(6.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "📍 $location",
                    fontSize = 12.sp,
                    color = Slate
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppButton(
                    label = "View Profile",
                    onTap = onViewProfile,
                    type = ButtonType.OUTLINED,
                    modifier = Modifier.weight(1f)
                )
                AppButton(
                    label = "Request Quote",
                    onTap = onRequestQuote,
                    type = ButtonType.PRIMARY,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun CategoryCard(
    icon: String,
    name: String,
    servicesCount: Int,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Fog),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onTap() }
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(PrimaryPale, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 26.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = DarkInk,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp,
                modifier = Modifier.height(34.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "$servicesCount Services",
                fontSize = 11.sp,
                color = Slate
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = DarkInk2
            )
            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Slate,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
        if (actionLabel != null && onAction != null) {
            Text(
                text = "$actionLabel →",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Primary,
                modifier = Modifier
                    .clickable { onAction() }
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun BookingStatusChip(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status.lowercase()) {
        "confirmed" -> Pair(Sky.copy(alpha = 0.15f), Sky)
        "in_progress" -> Pair(PrimaryPale, Primary)
        "completed" -> Pair(Emerald.copy(alpha = 0.15f), Emerald)
        "cancelled" -> Pair(Color.Red.copy(alpha = 0.1f), Color.Red)
        else -> Pair(Slate.copy(alpha = 0.1f), Slate)
    }

    Box(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(100.dp))
            .border(BorderStroke(1.dp, textColor.copy(alpha = 0.3f)), RoundedCornerShape(100.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.replace("_", " ").uppercase(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
fun AdminStatCard(
    title: String,
    value: String,
    change: String,
    changePositive: Boolean,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Fog),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = Slate,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkInk,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = if (changePositive) Emerald else Color.Red,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = change,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (changePositive) Emerald else Color.Red
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "vs last month",
                        fontSize = 10.sp,
                        color = Mist
                    )
                }
            }
            
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(PrimaryPale, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun StepIndicator(
    totalSteps: Int,
    currentStep: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..totalSteps) {
            val isActive = i <= currentStep
            val isCurrent = i == currentStep
            
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        color = if (isActive) Primary else Fog,
                        shape = CircleShape
                    )
                    .border(
                        border = if (isCurrent) BorderStroke(2.dp, DarkInk) else BorderStroke(0.dp, Color.Transparent),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = i.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isActive) White else Slate
                )
            }
            
            if (i < totalSteps) {
                Box(
                    modifier = Modifier
                        .width(16.dp)
                        .height(2.dp)
                        .background(if (i < currentStep) Primary else Fog)
                )
            }
        }
    }
}

@Composable
fun AbhiyantriLogoIcon(
    modifier: Modifier = Modifier,
    isDarkBg: Boolean = false
) {
    Image(
        painter = painterResource(id = com.example.R.drawable.img_logo),
        contentDescription = "Abhiyantri Setu Logo",
        modifier = modifier
    )
}

@Composable
fun AbhiyantriSetuLogo(
    modifier: Modifier = Modifier,
    isDarkBg: Boolean = false,
    showTagline: Boolean = true
) {
    Image(
        painter = painterResource(id = com.example.R.drawable.img_logo),
        contentDescription = "Abhiyantri Setu Logo",
        modifier = modifier
    )
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White.copy(alpha = 0.65f),
    borderColor: Color = Color.White.copy(alpha = 0.45f),
    shape: RoundedCornerShape = RoundedCornerShape(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = shape,
        border = BorderStroke(1.2.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            content()
        }
    }
}

@Composable
fun GlassmorphicBackgroundColumn(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFFFAF8), // Soft orange tint
                        Color(0xFFEAF5FF), // Soft sky blue tint
                        Color(0xFFF7F2FF)  // Soft light violet tint
                    )
                )
            )
    ) {
        // Aesthetic glowing radial shapes behind on top & right
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color(0xFFFDE4D3).copy(alpha = 0.45f),
                radius = size.width * 0.55f,
                center = Offset(size.width * 0.1f, size.height * 0.15f)
            )
            drawCircle(
                color = Color(0xFFDCEBFF).copy(alpha = 0.45f),
                radius = size.width * 0.6f,
                center = Offset(size.width * 0.9f, size.height * 0.6f)
            )
        }
        Column(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}

