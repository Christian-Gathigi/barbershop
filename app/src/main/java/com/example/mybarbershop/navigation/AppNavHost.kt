package com.example.mybarbershop.navigation

import android.R.id.message
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mybarbershop.models.BarberViewModel
import com.example.mybarbershop.models.MassageBookingViewModel
import com.example.mybarbershop.ui.theme.screens.SplashScreen
import com.example.mybarbershop.ui.theme.screens.barberbooking.AddBarberBookingScreen
import com.example.mybarbershop.ui.theme.screens.dashboard.DashboardScreen
import com.example.mybarbershop.ui.theme.screens.massage.MassageBookingForm
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
        composable(ROUTE_ADD_MASSAGE) {
            val context = LocalContext.current
            val viewModel: MassageBookingViewModel = viewModel()

            // Remember state to track success message
            var successMessage by remember { mutableStateOf<String?>(null) }

            // Display Toast when a success message is set
            successMessage?.let { message ->
                LaunchedEffect(message) {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    successMessage = null // Clear message after showing
                }
            }

            // Show the booking form
            MassageBookingForm(
                viewModel = viewModel,
                onBookingSuccess = {
                    successMessage = message.toString()
                }
            )
        }

        composable(ROUTE_ADD_BARBER_BOOKING) {
            val viewModel: BarberViewModel = BarberViewModel()
            AddBarberBookingScreen(viewModel = viewModel)
        }

        composable(ROUTE_ADD_SALON_BOOKING) { AddSalonBookingScreen() }
    }
}


