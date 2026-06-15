package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val uid: String,
    val name: String,
    val phone: String,
    val email: String,
    val role: String, // "admin" | "vendor" | "user"
    val city: String,
    val profileImage: String = ""
)

@Entity(tableName = "vendors")
data class Vendor(
    @PrimaryKey val id: String,
    val businessName: String,
    val ownerName: String,
    val phone: String,
    val email: String,
    val category: String,
    val subCategories: String, // Comma-separated list
    val city: String,
    val experience: Int,
    val description: String,
    val logoUrl: String,
    val rating: Double,
    val totalReviews: Int,
    val totalProjects: Int,
    val isVerified: Boolean = true,
    val verificationLevel: String = "standard", // "gold" | "standard"
    val isActive: Boolean = true,
    val subscriptionPlan: String = "free", // "free" | "basic" | "pro" | "premium"
    val subscriptionExpiry: Long = System.currentTimeMillis() + 31536000000L, // 1 year
    val totalLeads: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "leads")
data class Lead(
    @PrimaryKey val leadId: String,
    val customerId: String,
    val customerName: String,
    val phone: String,
    val email: String,
    val service: String,
    val category: String,
    val location: String,
    val budget: String,
    val requirements: String,
    val images: String = "", // Comma-separated URLs or names
    val status: String = "new", // "new" | "contacted" | "quote_sent" | "converted" | "lost"
    val assignedVendorId: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey val bookingId: String,
    val customerId: String,
    val customerName: String,
    val vendorId: String,
    val vendorName: String,
    val service: String,
    val category: String,
    val dateTime: Long,
    val location: String,
    val requirements: String,
    val images: String = "", // Comma-separated
    val estimatedAmount: Double,
    val advancePaid: Double,
    val paymentMethod: String,
    val status: String = "confirmed", // "confirmed" | "in_progress" | "completed" | "cancelled"
    val milestones: String = "Booking Confirmed,Professional Assigned,Work Started,Work Completed,Review",
    val currentMilestoneIndex: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey val id: String,
    val name: String,
    val icon: String,
    val description: String,
    val isActive: Boolean = true,
    val servicesCount: Int = 0,
    val subServices: String // Comma-separated
)

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey val reviewId: String,
    val customerId: String,
    val customerName: String,
    val vendorId: String,
    val bookingId: String,
    val rating: Double,
    val comment: String,
    val createdAt: Long = System.currentTimeMillis()
)
