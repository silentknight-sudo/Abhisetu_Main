package com.example.ui

import androidx.compose.animation.*
import androidx.compose.runtime.*
import com.example.data.AppViewModel
import com.example.data.Category
import com.example.ui.screens.*

sealed class Screen {
    object Splash : Screen()
    object Onboarding : Screen()
    object Login : Screen()
    data class OTP(val phone: String) : Screen()
    object Register : Screen()
    object CustomerShell : Screen()
    data class CategoryDetail(val categoryId: String) : Screen()
    data class BookService(val serviceName: String, val category: Category) : Screen()
    data class RequestQuote(val category: Category) : Screen()
    object CostEstimator : Screen()
    object VendorDashboard : Screen()
    object AdminDashboard : Screen()
}

@Composable
fun AppNavigationHost(
    viewModel: AppViewModel
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }
    val screenStack = remember { mutableStateListOf<Screen>(Screen.Splash) }

    fun navigateTo(screen: Screen) {
        screenStack.add(screen)
        currentScreen = screen
    }

    fun navigateBack() {
        if (screenStack.size > 1) {
            screenStack.removeAt(screenStack.size - 1)
            currentScreen = screenStack.last()
        }
    }

    fun navigateAndClear(screen: Screen) {
        screenStack.clear()
        screenStack.add(screen)
        currentScreen = screen
    }

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "fade"
    ) { screen ->
        when (screen) {
            is Screen.Splash -> SplashScreen(
                onSplashFinished = {
                    navigateAndClear(Screen.Onboarding)
                }
            )
            is Screen.Onboarding -> OnboardingScreen(
                onOnboardingCompleted = {
                    navigateAndClear(Screen.Login)
                }
            )
            is Screen.Login -> LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = { role ->
                    when (role) {
                        "admin" -> navigateAndClear(Screen.AdminDashboard)
                        "vendor" -> navigateAndClear(Screen.VendorDashboard)
                        else -> navigateAndClear(Screen.CustomerShell)
                    }
                },
                onGoToRegister = {
                    navigateTo(Screen.Register)
                },
                onGoToOTP = { phone ->
                    navigateTo(Screen.OTP(phone))
                }
            )
            is Screen.OTP -> OTPScreen(
                phone = screen.phone,
                onOTPVerified = {
                    navigateAndClear(Screen.CustomerShell)
                },
                onBack = {
                    navigateBack()
                }
            )
            is Screen.Register -> RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = { role ->
                    when (role) {
                        "admin" -> navigateAndClear(Screen.AdminDashboard)
                        "vendor" -> navigateAndClear(Screen.VendorDashboard)
                        else -> navigateAndClear(Screen.CustomerShell)
                    }
                },
                onGoToLogin = {
                    navigateTo(Screen.Login)
                }
            )
            is Screen.CustomerShell -> CustomerAppShell(
                viewModel = viewModel,
                onNavigateToCategory = { catId ->
                    navigateTo(Screen.CategoryDetail(catId))
                },
                onNavigateToBookService = { sName, cat ->
                    navigateTo(Screen.BookService(sName, cat))
                },
                onNavigateToRequestQuote = { cat ->
                    navigateTo(Screen.RequestQuote(cat))
                },
                onNavigateToVendorProfile = {},
                onNavigateToEstimator = {
                    navigateTo(Screen.CostEstimator)
                },
                onLogout = {
                    viewModel.logout {
                        navigateAndClear(Screen.Login)
                    }
                }
            )
            is Screen.CategoryDetail -> CategoryDetailScreenView(
                categoryId = screen.categoryId,
                viewModel = viewModel,
                onBookService = { sName, cat ->
                    navigateTo(Screen.BookService(sName, cat))
                },
                onRequestQuote = { cat ->
                    navigateTo(Screen.RequestQuote(cat))
                },
                onBack = {
                    navigateBack()
                }
            )
            is Screen.BookService -> BookServiceWizard(
                serviceName = screen.serviceName,
                category = screen.category,
                viewModel = viewModel,
                onSuccessFinished = {
                    navigateAndClear(Screen.CustomerShell)
                },
                onCancel = {
                    navigateBack()
                }
            )
            is Screen.RequestQuote -> RequestQuoteWizard(
                category = screen.category,
                viewModel = viewModel,
                onSuccessFinished = {
                    navigateAndClear(Screen.CustomerShell)
                },
                onCancel = {
                    navigateBack()
                }
            )
            is Screen.CostEstimator -> CostEstimatorScreenView(
                viewModel = viewModel,
                onBack = {
                    navigateBack()
                }
            )
            is Screen.VendorDashboard -> VendorDashboardShell(
                viewModel = viewModel,
                onLogout = {
                    viewModel.logout {
                        navigateAndClear(Screen.Login)
                    }
                }
            )
            is Screen.AdminDashboard -> AdminDashboardShell(
                viewModel = viewModel,
                onLogout = {
                    viewModel.logout {
                        navigateAndClear(Screen.Login)
                    }
                }
            )
        }
    }
}
