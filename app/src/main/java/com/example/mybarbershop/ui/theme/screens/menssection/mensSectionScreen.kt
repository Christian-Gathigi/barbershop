package com.example.mybarbershop.ui.theme.screens.menssection

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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




@Composable
    fun MensSectionScreen(navController: NavHostController) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Men's Section")
            Spacer(modifier = Modifier.height(16.dp))




            Spacer(modifier = Modifier.height(8.dp))

            HairStyleCard(
                title = "Afrotaper",
                imageRes = R.drawable.afrotaper,
                onClick = { navController.navigate(ROUTE_ADD_BARBER_BOOKING) }
            )
            HairStyleCard(
                title = "mid taper",
                imageRes = R.drawable.midtaper,
                onClick = { navController.navigate(ROUTE_ADD_BARBER_BOOKING) }
            )

            HairStyleCard(
                title = "buzzcut",
                imageRes = R.drawable.buzzcut,
                onClick = { navController.navigate(ROUTE_ADD_BARBER_BOOKING) }
            )
            HairStyleCard(
                title = "afro mohawk",
                imageRes = R.drawable.afromohawk,
                onClick = { navController.navigate(ROUTE_ADD_BARBER_BOOKING) }
            )
            HairStyleCard(
                title = " braids",
                imageRes = R.drawable.braids,
                onClick = { navController.navigate(ROUTE_ADD_BARBER_BOOKING) }
            )
        }
    }

    @Composable
    fun HairStyleCard(title: String, imageRes: Int, onClick: () -> Unit) {
        Card(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    title,
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MenssectionScreenPreview(){
    MensSectionScreen(rememberNavController())
}