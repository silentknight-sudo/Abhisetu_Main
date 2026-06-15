package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    // --- Users ---
    @Query("SELECT * FROM users WHERE uid = :uid")
    fun getUserById(uid: String): Flow<User?>

    @Query("SELECT * FROM users WHERE uid = :uid")
    suspend fun getUser(uid: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE role = 'user'")
    suspend fun getAllCustomers(): List<User>

    @Query("SELECT * FROM users WHERE role = 'user'")
    fun getAllCustomersFlow(): Flow<List<User>>

    // --- Vendors ---
    @Query("SELECT * FROM vendors ORDER BY createdAt DESC")
    fun getAllVendors(): Flow<List<Vendor>>

    @Query("SELECT * FROM vendors WHERE id = :id")
    fun getVendorByIdFlow(id: String): Flow<Vendor?>

    @Query("SELECT * FROM vendors WHERE id = :id")
    suspend fun getVendorById(id: String): Vendor?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVendor(vendor: Vendor)

    @Query("UPDATE vendors SET verificationLevel = :level, isVerified = :isVerified WHERE id = :vendorId")
    suspend fun updateVendorVerification(vendorId: String, level: String, isVerified: Boolean)

    @Query("UPDATE vendors SET isActive = :isActive WHERE id = :vendorId")
    suspend fun updateVendorActive(vendorId: String, isActive: Boolean)

    @Query("DELETE FROM vendors WHERE id = :id")
    suspend fun deleteVendor(id: String)

    // --- Leads ---
    @Query("SELECT * FROM leads ORDER BY createdAt DESC")
    fun getAllLeads(): Flow<List<Lead>>

    @Query("SELECT * FROM leads WHERE assignedVendorId = :vendorId ORDER BY createdAt DESC")
    fun getLeadsForVendor(vendorId: String): Flow<List<Lead>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLead(lead: Lead)

    @Query("UPDATE leads SET status = :status WHERE leadId = :leadId")
    suspend fun updateLeadStatus(leadId: String, status: String)

    @Query("UPDATE leads SET assignedVendorId = :vendorId WHERE leadId = :leadId")
    suspend fun updateLeadAssignedVendor(leadId: String, vendorId: String)

    @Query("DELETE FROM leads WHERE leadId = :id")
    suspend fun deleteLead(id: String)

    // --- Bookings ---
    @Query("SELECT * FROM bookings ORDER BY createdAt DESC")
    fun getAllBookings(): Flow<List<Booking>>

    @Query("SELECT * FROM bookings WHERE customerId = :customerId ORDER BY createdAt DESC")
    fun getBookingsForUser(customerId: String): Flow<List<Booking>>

    @Query("SELECT * FROM bookings WHERE vendorId = :vendorId ORDER BY createdAt DESC")
    fun getBookingsForVendor(vendorId: String): Flow<List<Booking>>

    @Query("SELECT * FROM bookings WHERE bookingId = :bookingId")
    fun getBookingById(bookingId: String): Flow<Booking?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: Booking)

    @Query("UPDATE bookings SET status = :status WHERE bookingId = :bookingId")
    suspend fun updateBookingStatus(bookingId: String, status: String)

    @Query("UPDATE bookings SET currentMilestoneIndex = :index WHERE bookingId = :bookingId")
    suspend fun updateBookingMilestone(bookingId: String, index: Int)

    @Query("DELETE FROM bookings WHERE bookingId = :id")
    suspend fun deleteBooking(id: String)

    // --- Categories ---
    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCategories(categories: List<Category>)

    @Query("UPDATE categories SET isActive = :active WHERE id = :id")
    suspend fun updateCategoryActive(id: String, active: Boolean)

    // --- Reviews ---
    @Query("SELECT * FROM reviews WHERE vendorId = :vendorId ORDER BY createdAt DESC")
    fun getReviewsForVendor(vendorId: String): Flow<List<Review>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: Review)
}
