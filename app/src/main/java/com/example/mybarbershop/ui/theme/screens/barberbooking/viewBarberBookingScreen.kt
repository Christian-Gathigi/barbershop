package com.example.mybarbershop.ui.theme.screens.barberbooking

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mybarbershop.data.Booking
import com.example.mybarbershop.models.BarberViewModel
import com.example.mybarbershop.navigation.ROUTE_UPDATE_BARBER_BOOKING

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewBarberBookingsScreen(
    navController: NavController,
    viewModel: BarberViewModel,
    onEditBooking: (Int) -> Unit,
) {
    val bookings = viewModel.bookings
    var showConfirmDialog by remember { mutableStateOf(false) }
    var bookingToDelete by remember { mutableStateOf<Booking?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("All Bookings", style = MaterialTheme.typography.headlineSmall)

        if (bookings.isEmpty()) {
            Text("No bookings found", modifier = Modifier.padding(top = 16.dp))
        } else {
            bookings.forEach { booking ->
                val hairstyle = viewModel.getHairstyleById(booking.hairstyleId)

                Card(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Client: ${booking.clientName}")
                        Text("Time: ${booking.dateTime.toLocalTime()}")
                        Text("Hairstyle: ${hairstyle?.name ?: "Unknown"}")
                        Spacer(modifier = Modifier.height(8.dp))

                        Row {
                            Button(
                                onClick = {
                                    navController.navigate("$ROUTE_UPDATE_BARBER_BOOKING/${booking.id}")
                                }
                            ) {
                                Text("Edit")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    viewModel.deleteBooking(booking.id)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}
