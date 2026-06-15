package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppRepository(private val dao: AppDao) {

    // Simulated Active Session State
    private val _loggedInUser = MutableStateFlow<User?>(null)
    val loggedInUser: StateFlow<User?> = _loggedInUser.asStateFlow()

    private val _selectedCity = MutableStateFlow("Greater Noida")
    val selectedCity: StateFlow<String> = _selectedCity.asStateFlow()

    fun updateSelectedCity(city: String) {
        _selectedCity.value = city
    }

    // --- Authentication ---
    suspend fun login(email: String, password: String): Boolean {
        // Match specific test profiles
        val normalizedEmail = email.trim().lowercase()
        val user = when {
            normalizedEmail == "admin@abhiyantrisetu.com" && password == "Admin@2024#" -> {
                dao.getUser("admin_test") ?: User(
                    uid = "admin_test",
                    name = "Central Administrator",
                    phone = "+91 99999 00000",
                    email = "admin@abhiyantrisetu.com",
                    role = "admin",
                    city = "Greater Noida"
                )
            }
            normalizedEmail == "vendor@test.com" && password == "Vendor@2024" -> {
                dao.getUser("vendor_test") ?: User(
                    uid = "vendor_test",
                    name = "Test Vendor Owner",
                    phone = "+91 99999 11111",
                    email = "vendor@test.com",
                    role = "vendor",
                    city = "Greater Noida"
                )
            }
            normalizedEmail == "user@test.com" && password == "User@2024" -> {
                dao.getUser("user_test") ?: User(
                    uid = "user_test",
                    name = "Test User",
                    phone = "+91 99999 88888",
                    email = "user@test.com",
                    role = "user",
                    city = "Greater Noida"
                )
            }
            else -> {
                // Check in Room general matching or deny
                null
            }
        }

        if (user != null) {
            dao.insertUser(user)
            _loggedInUser.value = user
            return true
        }
        return false
    }

    suspend fun register(name: String, phone: String, email: String, city: String, role: String): Boolean {
        val uid = "user_" + System.currentTimeMillis()
        val user = User(
            uid = uid,
            name = name,
            phone = phone,
            email = email,
            role = role,
            city = city
        )
        dao.insertUser(user)
        _loggedInUser.value = user

        if (role == "vendor") {
            // Also seed as a vendor
            val newVendor = Vendor(
                id = uid,
                businessName = "$name Enterprises",
                ownerName = name,
                phone = phone,
                email = email,
                category = "Home Maintenance",
                subCategories = "Door Hinges & Handle Fix,Lock & Key Set Install",
                city = city,
                experience = 1,
                description = "Newly registered engineering and handyman team.",
                logoUrl = "default_logo",
                rating = 4.0,
                totalReviews = 0,
                totalProjects = 0,
                isVerified = false,
                verificationLevel = "none",
                subscriptionPlan = "free",
                totalLeads = 0
            )
            dao.insertVendor(newVendor)
        }
        return true
    }

    fun logout() {
        _loggedInUser.value = null
    }

    // --- Users/Customers ---
    suspend fun getAllCustomers(): List<User> = dao.getAllCustomers()
    fun getAllCustomersFlow(): Flow<List<User>> = dao.getAllCustomersFlow()

    // --- Categories ---
    val allCategories: Flow<List<Category>> = dao.getAllCategories()
    suspend fun updateCategoryActive(id: String, active: Boolean) = dao.updateCategoryActive(id, active)
    suspend fun addCategory(category: Category) = dao.insertCategory(category)

    // --- Vendors ---
    val allVendors: Flow<List<Vendor>> = dao.getAllVendors()
    fun getVendorByIdFlow(id: String): Flow<Vendor?> = dao.getVendorByIdFlow(id)
    suspend fun getVendorById(id: String): Vendor? = dao.getVendorById(id)
    suspend fun saveVendor(vendor: Vendor) = dao.insertVendor(vendor)
    suspend fun updateVendorVerification(vendorId: String, level: String, isVerified: Boolean) = 
        dao.updateVendorVerification(vendorId, level, isVerified)
    suspend fun updateVendorActive(vendorId: String, isActive: Boolean) = dao.updateVendorActive(vendorId, isActive)
    suspend fun deleteVendor(id: String) = dao.deleteVendor(id)

    // --- Leads ---
    val allLeads: Flow<List<Lead>> = dao.getAllLeads()
    fun getLeadsForVendor(vendorId: String): Flow<List<Lead>> = dao.getLeadsForVendor(vendorId)
    suspend fun createLead(lead: Lead) = dao.insertLead(lead)
    suspend fun updateLeadStatus(leadId: String, status: String) = dao.updateLeadStatus(leadId, status)
    suspend fun updateLeadAssignedVendor(leadId: String, vendorId: String) = dao.updateLeadAssignedVendor(leadId, vendorId)
    suspend fun deleteLead(id: String) = dao.deleteLead(id)

    // --- Bookings ---
    val allBookings: Flow<List<Booking>> = dao.getAllBookings()
    fun getBookingsForUser(userId: String): Flow<List<Booking>> = dao.getBookingsForUser(userId)
    fun getBookingsForVendor(vendorId: String): Flow<List<Booking>> = dao.getBookingsForVendor(vendorId)
    fun getBookingById(bookingId: String): Flow<Booking?> = dao.getBookingById(bookingId)
    suspend fun saveBooking(booking: Booking) = dao.insertBooking(booking)
    suspend fun updateBookingStatus(bookingId: String, status: String) = dao.updateBookingStatus(bookingId, status)
    suspend fun updateBookingMilestone(bookingId: String, index: Int) = dao.updateBookingMilestone(bookingId, index)
    suspend fun deleteBooking(id: String) = dao.deleteBooking(id)

    // --- Reviews ---
    fun getReviewsForVendor(vendorId: String): Flow<List<Review>> = dao.getReviewsForVendor(vendorId)
    suspend fun submitReview(review: Review) = dao.insertReview(review)
}
