package com.example.mybarbershop.ui.theme.screens.barberbooking

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mybarbershop.data.BarberAppointmentViewModel
import com.example.mybarbershop.model.BarberAppointmentModel
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewBarberBookingsScreen(
    navController: NavController,
    barberAppointmentViewModel: BarberAppointmentViewModel = viewModel()
) {
    val appointments = barberAppointmentViewModel.appointments.collectAsState()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")


    LaunchedEffect(Unit) {
        barberAppointmentViewModel.fetchAppointments()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "All Barber Bookings",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(appointments.value) { appointment ->
                BookingItem(
                    booking = appointment,
                    navController = navController,
                    viewModel = barberAppointmentViewModel,
                    formatter = formatter
                )
            }
        }
    }
}

@Composable
fun BookingItem(
    booking: BarberAppointmentModel,
    navController: NavController,
    viewModel: BarberAppointmentViewModel,
    formatter: DateTimeFormatter?


    ) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Client: ${booking.clientName}")
            Text("Hairstyle: ${booking.hairstyle.name}")
            Text("Start: ${booking.startTime}")
            Text("End: ${booking.endTime}")

            Row {
                Button(onClick = {
                    viewModel.deleteAppointment(booking.bookingid)
                }) {
                    Text("Remove")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    navController.navigate("update_booking/${booking.bookingid}")
                }) {
                    Text("Update")
                }
            }
        }
    }
}

