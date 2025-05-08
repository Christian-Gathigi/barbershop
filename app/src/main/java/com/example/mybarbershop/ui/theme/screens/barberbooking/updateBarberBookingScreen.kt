package com.example.mybarbershop.ui.theme.screens.barberbooking

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mybarbershop.data.Booking
import com.example.mybarbershop.models.BarberViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UpdateBarberBookingScreen(
    booking: Booking,
    viewModel: BarberViewModel,
    onUpdated: () -> Unit
) {
    val name = remember { mutableStateOf(booking.clientName) }
    val selectedHairstyle = remember { mutableStateOf(viewModel.getHairstyleById(booking.hairstyleId)) }
    val selectedBarber = remember { mutableStateOf(viewModel.getBarberById(booking.barberId)) }
    val selectedDateTime = remember { mutableStateOf(booking.dateTime) }

    val availableBarbers by remember(selectedDateTime.value, selectedHairstyle.value) {
        derivedStateOf {
            selectedHairstyle.value?.let {
                viewModel.getAvailableBarbers(selectedDateTime.value, it)
            } ?: emptyList()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(value = name.value, onValueChange = { name.value = it }, label = { Text("Client Name") })

        Spacer(Modifier.height(8.dp))

        DropdownSelector(
            label = "Hairstyle",
            options = viewModel.getHairstyles(),
            selectedOption = selectedHairstyle.value,
            onOptionSelected = { selectedHairstyle.value = it }
        )

        Spacer(Modifier.height(8.dp))

        DropdownSelector(
            label = "Barber",
            options = availableBarbers,
            selectedOption = selectedBarber.value,
            onOptionSelected = { selectedBarber.value = it }
        )

        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            val updatedBooking = booking.copy(
                clientName = name.value,
                dateTime = selectedDateTime.value,
                hairstyleId = selectedHairstyle.value?.id ?: booking.hairstyleId,
                barberId = selectedBarber.value?.id ?: booking.barberId
            )
            val success = viewModel.updateBooking(updatedBooking)
            if (success) onUpdated()
        }) {
            Text("Update Booking")
        }
    }
}


