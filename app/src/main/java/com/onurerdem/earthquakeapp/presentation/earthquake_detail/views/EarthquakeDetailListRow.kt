package com.onurerdem.earthquakeapp.presentation.earthquake_detail.views

import android.content.Context
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
import com.onurerdem.earthquakeapp.R
import com.onurerdem.earthquakeapp.data.remote.dto.AirportX
import com.onurerdem.earthquakeapp.data.remote.dto.ClosestCityX
import com.onurerdem.earthquakeapp.presentation.UIText
import com.onurerdem.earthquakeapp.presentation.isDarkThemeMode

@Composable
fun EarthquakeDetailListRow(
    closestCity: ClosestCityX? = null,
    airports: AirportX? = null,
    context: Context
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
                    color = if (isDarkThemeMode(context = context)) Color.White else Color.Black,
                    textAlign = TextAlign.Start
                )
            }

            if (closestCity != null) {
                Text(
                    text = closestCity.population.toString(),
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    color = if (isDarkThemeMode(context = context)) Color.White else Color.Black,
                    textAlign = TextAlign.Start
                )
            }

            if (closestCity != null) {
                Text(
                    text = closestCity.distance.toString() + " " + UIText.StringResource(R.string._km).likeString(),
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    color = if (isDarkThemeMode(context = context)) Color.White else Color.Black,
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
                    color = if (isDarkThemeMode(context = context)) Color.White else Color.Black,
                    textAlign = TextAlign.Start
                )
            }

            if (airports != null) {
                Text(
                    text = airports.distance.toString() + " " + UIText.StringResource(R.string._km).likeString(),
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    color = if (isDarkThemeMode(context = context)) Color.White else Color.Black,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}