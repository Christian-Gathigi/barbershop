package com.example.mybarbershop.ui.theme.screens.womenssection

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mybarbershop.R
import com.example.mybarbershop.navigation.ROUTE_ADD_BARBER_BOOKING
import com.example.mybarbershop.navigation.ROUTE_ADD_SALON_BOOKING

@Composable
fun WomensSectionScreen(navController: NavHostController) {
    Column(Modifier.padding(16.dp)) {
        Text("Men's Section")
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { navController.navigate(ROUTE_ADD_BARBER_BOOKING) },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(Modifier.padding(16.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.haircut),
                    contentDescription = "Haircut",
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text("Book a Haircut", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { navController.navigate(ROUTE_ADD_BARBER_BOOKING) },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(Modifier.padding(16.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.beard_trim),
                    contentDescription = "Beard Trim",
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text("Book Beard Trim", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WomensSectionScreenPreview(){
    WomensSectionScreen(rememberNavController())
}


