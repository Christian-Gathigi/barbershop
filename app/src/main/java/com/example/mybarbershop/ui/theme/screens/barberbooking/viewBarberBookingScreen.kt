package com.example.mybarbershop.ui.theme.screens.barberbooking

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mybarbershop.data.Booking
import com.example.mybarbershop.models.BarberViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewBarberBookingScreen(booking: Booking, viewModel: BarberViewModel) {
    val hairstyle = viewModel.getHairstyleById(booking.hairstyleId)
    val barber = viewModel.getBarberById(booking.barberId)

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Client: ${booking.clientName}", style = MaterialTheme.typography.titleMedium)
        Text("Hairstyle: ${hairstyle?.name}")
        Text("Barber: ${barber?.name}")
        Text("Start Time: ${booking.dateTime}")
        Text("End Time: ${booking.dateTime.plusMinutes(hairstyle?.durationMinutes?.toLong() ?: 0L)}")
    }
}
