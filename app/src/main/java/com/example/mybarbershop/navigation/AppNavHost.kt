package com.example.mybarbershop.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mybarbershop.data.BarberAppointmentViewModel
import com.example.mybarbershop.ui.theme.screens.SplashScreen
import com.example.mybarbershop.ui.theme.screens.barberbooking.AddBarberBookingScreen
import com.example.mybarbershop.ui.theme.screens.barberbooking.UpdateBarberBookingScreen
import com.example.mybarbershop.ui.theme.screens.barberbooking.ViewBarberBookingsScreen
import com.example.mybarbershop.ui.theme.screens.dashboard.DashboardScreen
import com.example.mybarbershop.ui.theme.screens.menssection.MensSectionScreen
import com.example.mybarbershop.ui.theme.screens.salonbookings.AddSalonBookingScreen
import com.example.mybarbershop.ui.theme.screens.womenssection.WomensSectionScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    val barberViewModel: BarberAppointmentViewModel = viewModel()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = ROUTE_SPLASH) {

        composable(ROUTE_SPLASH) {
            SplashScreen {
                navController.navigate(ROUTE_DASHBOARD) {
                    popUpTo(ROUTE_SPLASH) { inclusive = true }
                }
            }
        }

        composable(ROUTE_DASHBOARD) {
            DashboardScreen(navController)
        }

        composable(ROUTE_MENSSECTION) {
            MensSectionScreen(navController)
        }

        composable(ROUTE_WOMENSSECTION) {
            WomensSectionScreen(navController)
        }

        composable(ROUTE_ADD_SALON_BOOKING) {
            AddSalonBookingScreen()
        }

        composable(ROUTE_ADD_BARBER_BOOKING) { AddBarberBookingScreen(navController) }
        composable(ROUTE_VIEW_BARBER_BOOKINGS){ ViewBarberBookingsScreen(navController) }
        composable("$ROUTE_UPDATE_BARBER_BOOKING/{studentId}") {
                passedData -> UpdateBarberBookingScreen(
            navController, passedData.arguments?.getString("studentId")!! )
        }
    }}