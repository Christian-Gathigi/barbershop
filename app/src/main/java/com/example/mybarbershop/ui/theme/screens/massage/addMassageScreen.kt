package com.example.mybarbershop.ui.theme.screens.massage

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mybarbershop.model.Massage
import com.example.mybarbershop.data.MassageBookingViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeParseException

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MassageBookingForm(viewModel: MassageBookingViewModel, onBookingSuccess: Function<Unit>) {
    val context = LocalContext.current

    val name = remember { mutableStateOf(TextFieldValue("")) }
    val selectedMassage = remember { mutableStateOf<Massage?>(null) }
    val timeInput = remember { mutableStateOf(TextFieldValue("")) }

    val parsedTime = try {
        LocalTime.parse(timeInput.value.text)
    } catch (e: DateTimeParseException) {
        null
    }

    val selectedDateTime = parsedTime?.let {
        LocalDateTime.of(LocalDate.now(), it)
    }

    Column(Modifier.padding(16.dp)) {
        Text("Book a Massage", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Your Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = timeInput.value,
            onValueChange = { timeInput.value = it },
            label = { Text("Preferred Time (HH:mm)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        var massageDropdownExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = massageDropdownExpanded,
            onExpandedChange = { massageDropdownExpanded = !massageDropdownExpanded }
        ) {
            OutlinedTextField(
                value = selectedMassage.value?.name ?: "Select Massage",
                onValueChange = {},
                readOnly = true,
                label = { Text("Massage Type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = massageDropdownExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = massageDropdownExpanded,
                onDismissRequest = { massageDropdownExpanded = false }
            ) {
                viewModel.massageTypes.forEach { massage ->
                    DropdownMenuItem(
                        text = { Text(massage.name) },
                        onClick = {
                            selectedMassage.value = massage
                            massageDropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        val massage = selectedMassage.value
        if (massage != null && selectedDateTime != null) {
            val endTime = selectedDateTime.plusMinutes(massage.durationMinutes.toLong())
            Text("Ends at: ${endTime.toLocalTime()}")
            Text("Cost: \$${massage.cost}")
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                val massageType = selectedMassage.value
                val time = selectedDateTime
                if (name.value.text.isNotBlank() && massageType != null && time != null) {
                    val success = viewModel.createMassageBooking(
                        name = name.value.text,
                        time = time,
                        massage = massageType
                    )
                    if (success) {
                        Toast.makeText(
                            context,
                            "Booked ${massageType.name} at ${time.toLocalTime()}",
                            Toast.LENGTH_LONG
                        ).show()
                        name.value = TextFieldValue("")
                        timeInput.value = TextFieldValue("")
                        selectedMassage.value = null
                    } else {
                        Toast.makeText(context, "Time slot unavailable", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Fill all fields correctly", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Book Now")
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun MassageBookingFormPreview() {
    MassageBookingForm(
        viewModel = MassageBookingViewModel(),
        onBookingSuccess = { /* No-op for preview */ }
    )
}

