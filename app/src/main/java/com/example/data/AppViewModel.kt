package com.example.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    val dao = db.dao()
    val repository = AppRepository(dao)

    init {
        // Automatically seed database on launch
        viewModelScope.launch {
            SeedData.seedDatabase(dao)
        }
    }

    // --- Active Session ---
    val loggedInUser: StateFlow<User?> = repository.loggedInUser
    val selectedCity: StateFlow<String> = repository.selectedCity

    fun selectCity(city: String) {
        repository.updateSelectedCity(city)
    }

    // --- Services & Categories ---
    val categories: StateFlow<List<Category>> = repository.allCategories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Vendors ---
    val vendors: StateFlow<List<Vendor>> = repository.allVendors
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Leads ---
    val leads: StateFlow<List<Lead>> = repository.allLeads
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Bookings ---
    val bookings: StateFlow<List<Booking>> = repository.allBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Customers ---
    val customers: StateFlow<List<User>> = repository.getAllCustomersFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Operations ---
    fun login(email: String, password: String, onSuccess: (User) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val success = repository.login(email, password)
            if (success) {
                val user = repository.loggedInUser.value
                if (user != null) {
                    onSuccess(user)
                    return@launch
                }
            }
            onError("Invalid credentials. Try admin@abhiyantrisetu.com / Admin@2024# OR user@test.com / User@2024")
        }
    }

    fun register(name: String, phone: String, email: String, city: String, role: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.register(name, phone, email, city, role)
            onSuccess()
        }
    }

    fun logout(onComplete: () -> Unit) {
        repository.logout()
        onComplete()
    }

    // --- Booking Management ---
    fun createBooking(
        customerId: String,
        customerName: String,
        vendorId: String,
        vendorName: String,
        service: String,
        category: String,
        dateTime: Long,
        location: String,
        requirements: String,
        images: String,
        estimatedAmt: Double,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            val bookingId = "AS" + (2245000 + (1000..9999).random())
            val booking = Booking(
                bookingId = bookingId,
                customerId = customerId,
                customerName = customerName,
                vendorId = vendorId,
                vendorName = vendorName,
                service = service,
                category = category,
                dateTime = dateTime,
                location = location,
                requirements = requirements,
                images = images,
                estimatedAmount = estimatedAmt,
                advancePaid = estimatedAmt * 0.3, // 30% advance
                paymentMethod = "UPI / Razorpay"
            )
            repository.saveBooking(booking)
            onSuccess(bookingId)
        }
    }

    fun updateBookingStatus(bookingId: String, status: String) {
        viewModelScope.launch {
            repository.updateBookingStatus(bookingId, status)
        }
    }

    fun updateBookingMilestone(bookingId: String, index: Int) {
        viewModelScope.launch {
            repository.updateBookingMilestone(bookingId, index)
        }
    }

    fun deleteBooking(id: String) {
        viewModelScope.launch {
            repository.deleteBooking(id)
        }
    }

    // --- Lead Management ---
    fun createLead(
        customerId: String,
        customerName: String,
        phone: String,
        email: String,
        service: String,
        category: String,
        location: String,
        budget: String,
        requirements: String,
        images: String,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            val leadId = "RQ" + (2104000 + (1000..9999).random())
            val lead = Lead(
                leadId = leadId,
                customerId = customerId,
                customerName = customerName,
                phone = phone,
                email = email,
                service = service,
                category = category,
                location = location,
                budget = budget,
                requirements = requirements,
                images = images,
                status = "new"
            )
            repository.createLead(lead)
            onSuccess(leadId)
        }
    }

    fun acceptLead(leadId: String, vendorId: String) {
        viewModelScope.launch {
            repository.updateLeadStatus(leadId, "contacted")
            repository.updateLeadAssignedVendor(leadId, vendorId)
            
            // Increment vendor's total leads count 
            val vendor = repository.getVendorById(vendorId)
            if (vendor != null) {
                repository.saveVendor(vendor.copy(totalLeads = vendor.totalLeads + 1))
            }
        }
    }

    fun declineLead(leadId: String) {
        viewModelScope.launch {
            repository.updateLeadStatus(leadId, "lost")
        }
    }

    fun updateLeadStatus(leadId: String, status: String) {
        viewModelScope.launch {
            repository.updateLeadStatus(leadId, status)
        }
    }

    fun deleteLead(id: String) {
        viewModelScope.launch {
            repository.deleteLead(id)
        }
    }

    // --- Admin Vendor CRUD ---
    fun addVendor(
        businessName: String,
        ownerName: String,
        phone: String,
        email: String,
        category: String,
        subCategories: String,
        city: String,
        experience: Int,
        description: String,
        plan: String,
        verifiedLevel: String
    ) {
        viewModelScope.launch {
            val id = "vendor_" + System.currentTimeMillis()
            val vendor = Vendor(
                id = id,
                businessName = businessName,
                ownerName = ownerName,
                phone = phone,
                email = email,
                category = category,
                subCategories = subCategories,
                city = city,
                experience = experience,
                description = description,
                logoUrl = "default_vendor",
                rating = 4.5,
                totalReviews = 0,
                totalProjects = 0,
                isVerified = verifiedLevel != "none",
                verificationLevel = verifiedLevel,
                subscriptionPlan = plan,
                subscriptionExpiry = System.currentTimeMillis() + 31536000000L
            )
            repository.saveVendor(vendor)
        }
    }

    fun updateVendorVerification(vendorId: String, level: String, isVerified: Boolean) {
        viewModelScope.launch {
            repository.updateVendorVerification(vendorId, level, isVerified)
        }
    }

    fun updateVendorActive(vendorId: String, isActive: Boolean) {
        viewModelScope.launch {
            repository.updateVendorActive(vendorId, isActive)
        }
    }

    fun deleteVendor(id: String) {
        viewModelScope.launch {
            repository.deleteVendor(id)
        }
    }

    // --- Cost Estimator State & logic ---
    private val _estimatorResult = MutableStateFlow<EstimatorResult?>(null)
    val estimatorResult: StateFlow<EstimatorResult?> = _estimatorResult.asStateFlow()

    fun calculateCost(
        projectType: String,
        plotAreaDp: Double,
        floors: Int,
        quality: String,
        includeInterior: Boolean,
        includeWaterproof: Boolean,
        includeElectrical: Boolean,
        includePlumbing: Boolean
    ) {
        val baseRate = when (quality) {
            "Economy" -> 1200.0
            "Standard" -> 1600.0
            "Premium" -> 2200.0
            "Luxury" -> 3200.0
            else -> 1800.0
        }

        var totalRate = baseRate * plotAreaDp * floors

        if (projectType == "Renovation") {
            totalRate *= 0.45 // renovations cost around 45% of building scratch
        } else if (projectType == "Extension") {
            totalRate *= 0.70
        }

        // Apply specs
        var civilPercent = 45.0
        var finishingPercent = 25.0
        var electricalPercent = 15.0
        var plumbingPercent = 15.0

        if (includeInterior) {
            totalRate += (400.0 * plotAreaDp * floors)
            finishingPercent += 10.0
            civilPercent -= 10.0
        }
        if (includeWaterproof) {
            totalRate += (60.0 * plotAreaDp * floors)
        }
        if (includeElectrical) {
            totalRate += (100.0 * plotAreaDp * floors)
            electricalPercent += 5.0
            civilPercent -= 5.0
        }
        if (includePlumbing) {
            totalRate += (100.0 * plotAreaDp * floors)
            plumbingPercent += 5.0
            civilPercent -= 5.0
        }

        val minCost = totalRate * 0.95
        val maxCost = totalRate * 1.08

        _estimatorResult.value = EstimatorResult(
            minCost = minCost,
            maxCost = maxCost,
            civilCost = totalRate * (civilPercent / 100.0),
            finishingCost = totalRate * (finishingPercent / 100.0),
            electricalCost = totalRate * (electricalPercent / 100.0),
            plumbingCost = totalRate * (plumbingPercent / 100.0),
            civilPct = civilPercent.toFloat(),
            finishingPct = finishingPercent.toFloat(),
            electricalPct = electricalPercent.toFloat(),
            plumbingPct = plumbingPercent.toFloat()
        )
    }

    // --- Reviews ---
    fun submitReview(reviewId: String, customerId: String, customerName: String, vendorId: String, bookingId: String, rating: Double, comment: String) {
        viewModelScope.launch {
            val review = Review(
                reviewId = reviewId,
                customerId = customerId,
                customerName = customerName,
                vendorId = vendorId,
                bookingId = bookingId,
                rating = rating,
                comment = comment
            )
            repository.submitReview(review)
            
            // Recalculate average rating for vendor
            val currentVendor = repository.getVendorById(vendorId)
            if (currentVendor != null) {
                val currentRatingSum = currentVendor.rating * currentVendor.totalReviews
                val newReviewsCount = currentVendor.totalReviews + 1
                val newRating = (currentRatingSum + rating) / newReviewsCount
                repository.saveVendor(
                    currentVendor.copy(
                        rating = String.format("%.1f", newRating).toDouble(),
                        totalReviews = newReviewsCount
                    )
                )
            }
        }
    }
}

data class EstimatorResult(
    val minCost: Double,
    val maxCost: Double,
    val civilCost: Double,
    val finishingCost: Double,
    val electricalCost: Double,
    val plumbingCost: Double,
    val civilPct: Float,
    val finishingPct: Float,
    val electricalPct: Float,
    val plumbingPct: Float
)
