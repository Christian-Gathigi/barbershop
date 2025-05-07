package com.example.mybarbershop.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mybarbershop.models.BarberViewModel
import com.example.mybarbershop.navigation.ROUTE_ADD_MASSAGE
import com.example.mybarbershop.ui.theme.screens.SplashScreen
import com.example.mybarbershop.ui.theme.screens.barberbooking.AddBarberBookingScreen
import com.example.mybarbershop.ui.theme.screens.dashboard.DashboardScreen
import com.example.mybarbershop.ui.theme.screens.menssection.MensSectionScreen
import com.example.mybarbershop.ui.theme.screens.salonbookings.AddSalonBookingScreen
import com.example.mybarbershop.ui.theme.screens.womenssection.WomensSectionScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = ROUTE_SPLASH) {
        composable(ROUTE_SPLASH) {
            SplashScreen {
                navController.navigate(ROUTE_DASHBOARD) {
                    popUpTo(ROUTE_SPLASH) { inclusive = true }
                }
            }
        }
        composable(ROUTE_DASHBOARD) { DashboardScreen(navController) }
        composable(ROUTE_MENSSECTION) { MensSectionScreen(navController) }
        composable(ROUTE_WOMENSSECTION) { WomensSectionScreen(navController) }
        composable(ROUTE_MASSAGE) { Mas() }
        composable(ROUTE_ADD_BARBER_BOOKING) {
            val viewModel: BarberViewModel = BarberViewModel()
            AddBarberBookingScreen(viewModel = viewModel)
        }

        composable(ROUTE_ADD_SALON_BOOKING) { AddSalonBookingScreen() }
    }
}


