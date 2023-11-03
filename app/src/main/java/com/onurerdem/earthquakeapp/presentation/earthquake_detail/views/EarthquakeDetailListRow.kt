package com.onurerdem.earthquakeapp.presentation.earthquake_detail.views

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.onurerdem.earthquakeapp.data.remote.dto.AirportX
import com.onurerdem.earthquakeapp.data.remote.dto.ClosestCityX

@Composable
fun EarthquakeDetailListRow(
    closestCity: ClosestCityX? = null,
    airports: AirportX? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.align(Alignment.CenterVertically), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (closestCity != null) {
                Text(
                    text = closestCity.name,
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    textAlign = TextAlign.Start
                )
            }

            if (closestCity != null) {
                Text(
                    text = closestCity.population.toString(),
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    textAlign = TextAlign.Start
                )
            }

            if (closestCity != null) {
                Text(
                    text = closestCity.distance.toString() + " km",
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    textAlign = TextAlign.Start
                )
            }
        }

        Column(
            modifier = Modifier.align(Alignment.CenterVertically), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (airports != null) {
                Text(
                    text = airports.name,
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    textAlign = TextAlign.Start
                )
            }

            if (airports != null) {
                Text(
                    text = airports.distance.toString() + " km",
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}