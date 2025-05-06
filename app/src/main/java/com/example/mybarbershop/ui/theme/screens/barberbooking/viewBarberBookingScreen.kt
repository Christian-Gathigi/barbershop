package com.example.mybarbershop.ui.theme.screens.barberbooking

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mybarbershop.data.BarberAppointmentModel.BarberBooking
import com.example.mybarbershop.models.BarberAppointmentViewModel

@Composable
fun BarberBookingScreen(viewModel: BarberAppointmentViewModel) {
    LazyColumn {
        items(viewModel.bookings) { booking ->
            val barber = viewModel.barbers.find { it.id == booking.barberId }
            val hairstyle = viewModel.hairstyles.find { it.id == booking.hairstyleId }

            Card(modifier = Modifier.padding(8.dp)) {
                Column(Modifier.padding(8.dp)) {
                    Text("Name: ${booking.clientName}")
                    Text("Barber: ${barber?.name ?: "Unknown"}")
                    Text("Hairstyle: ${hairstyle?.name ?: "Unknown"}")
                    Text("Time: ${booking.dateTime}")

                    Row {
                        Button(onClick = {
                            viewModel.deleteBooking(booking.id)
                        }) {
                            Text("Delete")
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = {
                            // TODO: Navigate to update screen or open a dialog
                        }) {
                            Text("Update")
                        }
                    }
                }
            }
        }
    }
}
