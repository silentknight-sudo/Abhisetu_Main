package com.example.data

import android.content.Context
import kotlinx.coroutines.flow.first

object SeedData {
    val CATEGORIES = listOf(
        Category(
            id = "home-construction",
            name = "Home Construction & Renovation",
            icon = "🏗️",
            description = "From planning to perfect finish, we build your dreams with quality and trust.",
            servicesCount = 12,
            subServices = "New House Construction,Home Renovation,House Extension,Structural Repair,Waterproofing,Tile & Flooring Work,Boundary Wall Construction,RCC Work,Demolition,Plastering,Brickwork,Excavation"
        ),
        Category(
            id = "interior-finishing",
            name = "Interior & Finishing",
            icon = "🎨",
            description = "Modern and beautiful designs with luxurious finishes for your liveable spaces.",
            servicesCount = 12,
            subServices = "Modular Kitchen,False Ceiling,Painting & Wallpaper,Wardrobe Design,Wooden Flooring,Tiling & Marbling,Electrical Fittings,Plumbing Fittings,Carpentry Work,Glass & Glazing,Deep Cleaning,PVC Paneling"
        ),
        Category(
            id = "electrical",
            name = "Electrical Services",
            icon = "⚡",
            description = "Book certified electrical professionals for installations, repairs, and support.",
            servicesCount = 12,
            subServices = "Full Home Wiring,Fan Repair & Install,Switch & Socket Fix,Geyser Installation,Inverter Sales & Repair,Smart Home Setup,Chandelier Hanging,DB Dressing,AC Wiring,Grounding & Earthing,CCTV Connection,Solar Panels Installation"
        ),
        Category(
            id = "plumbing",
            name = "Plumbing Services",
            icon = "🚿",
            description = "Leakages, installations, and repairs conducted by professional plumbers.",
            servicesCount = 12,
            subServices = "Pipe Leak Repair,Tap & Mixer Fix,Flush Valve Repair,Water Tank Cleaning,Sink & Basin Install,Drain Blockage Clear,Water Pump Install,Gutter Repair,Sprinkler Repair,Water Softener Setup,Basin Install,Shower Fittings"
        ),
        Category(
            id = "ac-hvac",
            name = "AC & HVAC Services",
            icon = "❄️",
            description = "Expert AC servicing, gas charging, repair, and professional installations.",
            servicesCount = 10,
            subServices = "AC Dry & Wet Service,Gas Charging,AC Installation,Compressor Replacement,Remote Control Repair,Copper Piping,Anti-Noise Vibration Fix,Water Leakage Fix,Duct Cleaning,Blower Fan Repair"
        ),
        Category(
            id = "home-maintenance",
            name = "Home Maintenance",
            icon = "🔧",
            description = "Everyday handymen and repair services for your home and office.",
            servicesCount = 10,
            subServices = "Door Hinges & Handle Fix,Window Sliding Repair,Lock & Key Set Install,Wall Plaster Patching,Sofa & Carpet Cleaning,Floor Polishing,Chimney Hood Services,Water Purifier (RO) service,Pest Control,Tile Grouting & Filling"
        ),
        Category(
            id = "safety-security",
            name = "Safety & Security",
            icon = "🛡️",
            description = "CCTV installations, burglar alarms, and advanced home security configurations.",
            servicesCount = 8,
            subServices = "CCTV Camera Setup,Smart Digital Lock,Electronic Safe Install,Video Doorbell Set,Smoke Alarms Installation,Fire Extinguisher Fitting,Laser Beam Fence,Passcode Access Control"
        ),
        Category(
            id = "engineering-consultancy",
            name = "Engineering Consultancy",
            icon = "📐",
            description = "Professional structural designs, blueprints, vaastu compliance, and estimations.",
            servicesCount = 10,
            subServices = "Blueprints & Floor Plans,Soil Testing,Structural Stability Check,Material Quality Estimation,On-Site Supervision,Green Building Consult,Renovation Assessment,Fire Safety Audit,Vaastu Consultation,3D Walkthrough Render"
        ),
        Category(
            id = "material-marketplace",
            name = "Material Marketplace",
            icon = "🧱",
            description = "High quality cement, bricks, aggregates, and steel bars directly from suppliers.",
            servicesCount = 10,
            subServices = "OPC/PPC Cement Bags,Red Clay Bricks,Premium River Sand,TMT Thermo Steel Bars,Wall Paint Buckets,Ceramic & Vitrified Tiles,PVC Sanitary Pipes,Copper Wires Grid,Teak Timber Logs,Float Glass Sheets"
        ),
        Category(
            id = "machinery-rental",
            name = "Machinery Rental",
            icon = "🚜",
            description = "Rent backhoes, concrete mixers, scaffolding, and compactors on daily rates.",
            servicesCount = 8,
            subServices = "JCB Backhoe Loader,Hydraulic Tower Crane,Diesel Concrete Mixer,Scaffolding Metal Sets,Vibratory Soil Compactor,Silent Generator Set,Heavy Motor Forklift,Asphalt Paver Machine"
        ),
        Category(
            id = "facility-management",
            name = "Facility Management",
            icon = "🏢",
            description = "Comprehensive building and society maintenance contracts.",
            servicesCount = 8,
            subServices = "Corporate Office Cleaning,24/7 Society Security,Municipal Waste Management,Commercial Pest Control,Horticulture & Landscaping,Automated Parking Setup,Rooftop Solar Upkeep,Elevator Maintenance"
        ),
        Category(
            id = "amc-services",
            name = "AMC Services",
            icon = "📋",
            description = "Annual Maintenance Contracts at special prices for ultimate peace of mind.",
            servicesCount = 6,
            subServices = "AC Annual AMC,Water RO Service AMC,Kitchen Chimney AMC,Society CCTV AMC,Elevator Lift AMC,Generator Set AMC"
        )
    )

    val VENDORS = listOf(
        Vendor(
            id = "v1",
            businessName = "Sharma Construction Co.",
            ownerName = "Rajesh Sharma",
            phone = "9876543210",
            email = "sharma.const@gmail.com",
            category = "Home Construction & Renovation",
            subCategories = "New House Construction,Home Renovation,House Extension,Structural Repair",
            city = "Greater Noida",
            experience = 15,
            description = "Premium home constructors with over 15 years of solid structural work in the NCR region. We deliver robust civil foundations and state-of-the-art multi-story brick and RCC frameworks with lifetime durability warranties.",
            logoUrl = "sharma_logo",
            rating = 4.8,
            totalReviews = 326,
            totalProjects = 250,
            isVerified = true,
            verificationLevel = "gold",
            subscriptionPlan = "premium",
            subscriptionExpiry = System.currentTimeMillis() + 31536000000L,
            totalLeads = 120
        ),
        Vendor(
            id = "v2",
            businessName = "ABC Interiors & Furnishings",
            ownerName = "Anita Chawla",
            phone = "9812345678",
            email = "info@abcinteriors.com",
            category = "Interior & Finishing",
            subCategories = "Modular Kitchen,False Ceiling,Painting & Wallpaper,Wardrobe Design",
            city = "Noida",
            experience = 10,
            description = "Award-winning interior design agency specializing in modular premium kitchens, customized false ceilings, false lighting, and storage Wardrobe architecture. Transforming NCR flats into luxury boutique spaces.",
            logoUrl = "abc_logo",
            rating = 4.7,
            totalReviews = 180,
            totalProjects = 135,
            isVerified = true,
            verificationLevel = "gold",
            subscriptionPlan = "pro",
            subscriptionExpiry = System.currentTimeMillis() + 15552000000L,
            totalLeads = 85
        ),
        Vendor(
            id = "v3",
            businessName = "Om Electrical Services",
            ownerName = "Om Prakash Yadav",
            phone = "9988776655",
            email = "yadav.om@yahoo.com",
            category = "Electrical Services",
            subCategories = "Full Home Wiring,Fan Repair & Install,Switch & Socket Fix,Geyser Installation",
            city = "Ghaziabad",
            experience = 8,
            description = "Fast, reliable local electrician squad assisting residential housing projects. Emergency fuse fix, premium wiring layouts, and appliance fittings with safety clearance standards.",
            logoUrl = "om_logo",
            rating = 4.6,
            totalReviews = 500,
            totalProjects = 480,
            isVerified = true,
            verificationLevel = "standard",
            subscriptionPlan = "basic",
            subscriptionExpiry = System.currentTimeMillis() + 8645000000L,
            totalLeads = 240
        ),
        Vendor(
            id = "v4",
            businessName = "BuildTech Solutions",
            ownerName = "Sameer Saxena",
            phone = "9012345678",
            email = "contact@buildtech.in",
            category = "Engineering Consultancy",
            subCategories = "Blueprints & Floor Plans,Structural Stability Check,Material Quality Estimation",
            city = "Greater Noida",
            experience = 12,
            description = "Certified professional structural engineers creating blueprint layouts, vaastu-approved 3D CAD renders, and material costing metrics for high-rises and duplex villas.",
            logoUrl = "buildtech_logo",
            rating = 4.5,
            totalReviews = 96,
            totalProjects = 110,
            isVerified = true,
            verificationLevel = "standard",
            subscriptionPlan = "pro",
            subscriptionExpiry = System.currentTimeMillis() + 12000000000L,
            totalLeads = 34
        ),
        Vendor(
            id = "v5",
            businessName = "PerfectBuild Co.",
            ownerName = "Gagan Preet",
            phone = "9201234567",
            email = "gagan@perfectbuild.co",
            category = "Home Construction & Renovation",
            subCategories = "Tile & Flooring Work,Waterproofing,RCC Work,Boundary Wall",
            city = "Noida",
            experience = 6,
            description = "Modern construction sub-contractors delivering excellent tile and flooring work, roof waterproofing services, and concrete casting for luxury complexes.",
            logoUrl = "perfect_logo",
            rating = 4.6,
            totalReviews = 74,
            totalProjects = 88,
            isVerified = true,
            verificationLevel = "standard",
            subscriptionPlan = "basic",
            subscriptionExpiry = System.currentTimeMillis() + 4500000000L,
            totalLeads = 40
        )
    )

    val LEADS = listOf(
        Lead(
            leadId = "RQ2104721",
            customerId = "user_test",
            customerName = "Vikash Sharma",
            phone = "9911223344",
            email = "vikash@gmail.com",
            service = "Home Renovation",
            category = "Home Construction & Renovation",
            location = "Greater Noida, UP",
            budget = "₹2,00,000 – ₹5,00,000",
            requirements = "Complete 3BHK flat interior repaint, wall crack waterproofing repairs, and brand new floor tile installation in dual balconies.",
            status = "new",
            assignedVendorId = "v1"
        ),
        Lead(
            leadId = "RQ2104722",
            customerId = "c1",
            customerName = "Anita Kumari",
            phone = "9988112233",
            email = "anita@gmail.com",
            service = "Modular Kitchen",
            category = "Interior & Finishing",
            location = "Noida, UP",
            budget = "₹1,50,000 – ₹2,50,000",
            requirements = "L-shaped acrylic modern modular kitchen with soft-close tandem drawers, chimney hook, and premium granite countertop replacement.",
            status = "contacted",
            assignedVendorId = "v2"
        ),
        Lead(
            leadId = "RQ2104723",
            customerId = "c2",
            customerName = "Kamlesh Prasad",
            phone = "8877554433",
            email = "kamlesh@test.com",
            service = "Full Home Wiring",
            category = "Electrical Services",
            location = "Ghaziabad, UP",
            budget = "₹50,000 – ₹90,000",
            requirements = "Full building wire replacement with fireproof copper wires. Installation of 3 main DB box panels.",
            status = "quote_sent",
            assignedVendorId = "v3"
        ),
        Lead(
            leadId = "RQ2104724",
            customerId = "c3",
            customerName = "Manish Garg",
            phone = "7766554433",
            email = "manish@test.com",
            service = "New House Construction",
            category = "Home Construction & Renovation",
            location = "Greater Noida, UP",
            budget = "₹25,000 – ₹50,000",
            requirements = "Floor blueprint sketches and civil foundation load estimation for a brand new 2-story independent duplex on 1500 sq.ft plot.",
            status = "converted",
            assignedVendorId = "v4"
        ),
        Lead(
            leadId = "RQ2104725",
            customerId = "user_test",
            customerName = "Sanjay Dutt",
            phone = "9112233445",
            email = "sanjay@gmail.com",
            service = "Waterproofing Solution",
            category = "Home Construction & Renovation",
            location = "Delhi",
            budget = "₹30,000 – ₹60,000",
            requirements = "Basement layout waterproofing of 1200 sq.ft to avoid continuous damp patches in monsoons.",
            status = "lost",
            assignedVendorId = "v5"
        )
    )

    val BOOKINGS = listOf(
        Booking(
            bookingId = "AS2245679",
            customerId = "user_test",
            customerName = "User Test",
            vendorId = "v1",
            vendorName = "Sharma Construction Co.",
            service = "Home Renovation",
            category = "Home Construction & Renovation",
            dateTime = System.currentTimeMillis() + 86400000L * 2, // 2 days in future
            location = "Greater Noida Area, Flat 402, Alpha-2",
            requirements = "Slab modification and balcony plastering with waterproofing compound.",
            estimatedAmount = 35000.0,
            advancePaid = 10000.0,
            paymentMethod = "UPI / Razorpay",
            status = "confirmed",
            currentMilestoneIndex = 0
        ),
        Booking(
            bookingId = "AS2245680",
            customerId = "user_test",
            customerName = "User Test",
            vendorId = "v2",
            vendorName = "ABC Interiors & Furnishings",
            service = "Modular Kitchen",
            category = "Interior & Finishing",
            dateTime = System.currentTimeMillis() - 86400000L, // yesterday
            location = "Greater Noida TechZone 4, Elite Apartments",
            requirements = "Installation of upper cabinets and soft close hinge tuning.",
            estimatedAmount = 180000.0,
            advancePaid = 50000.0,
            paymentMethod = "Net Banking",
            status = "in_progress",
            currentMilestoneIndex = 2 // Work Started
        ),
        Booking(
            bookingId = "AS2245681",
            customerId = "user_test",
            customerName = "User Test",
            vendorId = "v3",
            vendorName = "Om Electrical Services",
            service = "Full Home Wiring",
            category = "Electrical Services",
            dateTime = System.currentTimeMillis() - 86400000L * 5, // 5 days ago
            location = "Noida Sector 62, High End Villas",
            requirements = "Complete living room light fitting and LED panel wiring installations.",
            estimatedAmount = 12000.0,
            advancePaid = 3000.0,
            paymentMethod = "Cards",
            status = "completed",
            currentMilestoneIndex = 4 // Work Completed
        )
    )

    suspend fun seedDatabase(dao: AppDao) {
        val count = dao.getAllCategories().first().size
        if (count == 0) {
            dao.insertAllCategories(CATEGORIES)
            for (vendor in VENDORS) {
                dao.insertVendor(vendor)
            }
            for (lead in LEADS) {
                dao.insertLead(lead)
            }
            for (booking in BOOKINGS) {
                dao.insertBooking(booking)
            }
            // Seed a test user 
            dao.insertUser(
                User(
                    uid = "user_test",
                    name = "Test User",
                    phone = "+91 99999 88888",
                    email = "user@test.com",
                    role = "user",
                    city = "Greater Noida"
                )
            )
            // Seed a test vendor
            dao.insertUser(
                User(
                    uid = "vendor_test",
                    name = "Test Vendor Owner",
                    phone = "+91 99999 11111",
                    email = "vendor@test.com",
                    role = "vendor",
                    city = "Greater Noida"
                )
            )
            val newVendorForTest = Vendor(
                id = "vendor_test",
                businessName = "Test Professional Services",
                ownerName = "Test Vendor Owner",
                phone = "9999911111",
                email = "vendor@test.com",
                category = "Home Maintenance",
                subCategories = "Door Hinges & Handle Fix,Window Sliding Repair,Lock & Key Set Install",
                city = "Greater Noida",
                experience = 5,
                description = "Experienced repair team ready to deploy immediate assistance. Handymen, locksmiths, and tile fixer crew available 24/7.",
                logoUrl = "test_logo",
                rating = 4.7,
                totalReviews = 120,
                totalProjects = 110,
                isVerified = true,
                verificationLevel = "gold",
                subscriptionPlan = "pro",
                subscriptionExpiry = System.currentTimeMillis() + 86400000L * 180,
                totalLeads = 12
            )
            dao.insertVendor(newVendorForTest)

            // Seed admin
            dao.insertUser(
                User(
                    uid = "admin_test",
                    name = "Central Administrator",
                    phone = "+91 99999 00000",
                    email = "admin@abhiyantrisetu.com",
                    role = "admin",
                    city = "Greater Noida"
                )
            )
        }
    }
}
