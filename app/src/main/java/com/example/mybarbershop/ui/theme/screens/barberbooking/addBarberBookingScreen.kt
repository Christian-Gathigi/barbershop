package com.example.mybarbershop.ui.theme.screens.barberbooking

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mybarbershop.data.BarberAppointmentModel.Barber
import com.example.mybarbershop.data.BarberAppointmentModel.Booking
import com.example.mybarbershop.data.BarberAppointmentModel.Hairstyle
import com.example.mybarbershop.models.BarberAppointmentViewModel
import com.example.mybarbershop.ui.theme.screens.menssection.MensSectionScreen
import java.time.LocalDateTime

@Composable
fun AddBarberBooking(viewModel: BarberAppointmentViewModel) {
    val name = remember { mutableStateOf("") }
    val selectedHairstyle = remember { mutableStateOf<Hairstyle?>(null) }
    val selectedBarber = remember { mutableStateOf<Barber?>(null) }
    val selectedDateTime = remember { mutableStateOf(LocalDateTime.now().plusMinutes(1)) }

    val availableBarbers = selectedHairstyle.value?.let {
        viewModel.getAvailableBarbers(selectedDateTime.value, it)
    } ?: emptyList()

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(value = name.value, onValueChange = { name.value = it }, label = { Text("Your Name") })

        Spacer(Modifier.height(8.dp))
        DropdownMenu("Select Hairstyle", viewModel.hairstyles.map { it.name }) {
            selectedHairstyle.value = viewModel.hairstyles[it]
        }

        Spacer(Modifier.height(8.dp))
        DropdownMenu("Select Barber", availableBarbers.map { it.name }) {
            selectedBarber.value = availableBarbers[it]
        }

        Spacer(Modifier.height(8.dp))
        // Custom DateTime Picker goes here...

        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            val hairstyle = selectedHairstyle.value
            val barber = selectedBarber.value
            if (name.value.isNotBlank() && hairstyle != null && barber != null) {
                val booking = Booking(
                    clientName = name.value,
                    dateTime = selectedDateTime.value,
                    barberId = barber.id,
                    hairstyleId = hairstyle.id
                )
                val success = viewModel.createBooking(booking)
                if (!success) {
                    Toast.makeText(LocalContext.current, "Barber not available!", Toast.LENGTH_SHORT).show()
                }
            }
        }) {
            Text("Book Now")
        }

        selectedHairstyle.value?.let {
            val endTime = selectedDateTime.value.plusMinutes(it.durationMinutes.toLong())
            Text("Ends at: ${endTime.toLocalTime()}", Modifier.padding(top = 8.dp))
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddBarberBookingScreenPreview(){
    AddBarberBooking( rememberNavController() )
}



