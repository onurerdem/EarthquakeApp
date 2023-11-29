package com.onurerdem.earthquakeapp.presentation.earthquakes.views

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.onurerdem.earthquakeapp.R
import com.onurerdem.earthquakeapp.domain.model.Earthquake
import com.onurerdem.earthquakeapp.presentation.SharedPreferencesManager
import com.onurerdem.earthquakeapp.presentation.UIText
import com.onurerdem.earthquakeapp.presentation.formatDateForTurkishLocale
import com.onurerdem.earthquakeapp.presentation.isDarkThemeMode

@Composable
fun EarthquakeListRow(
    earthquake: Earthquake,
    onItemClick: (Earthquake) -> Unit,
    context: Context
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onItemClick(earthquake)
            }
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.align(CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = earthquake.title,
                style = MaterialTheme.typography.subtitle1,
                overflow = TextOverflow.Ellipsis,
                color = if (isDarkThemeMode(context = context)) Color.White else Color.Black,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth(.6f)
                    .horizontalScroll(rememberScrollState())
            )

            Text(
                text = UIText.StringResource(R.string.date)
                    .likeString() + " " + formatDateForTurkishLocale(
                    date = earthquake.date.substring(0, 10),
                    sharedPreferencesManager = SharedPreferencesManager(context = context)
                ) + ", " + UIText.StringResource(R.string._hour)
                    .likeString() + earthquake.date.substring(10, 19),
                style = MaterialTheme.typography.subtitle2,
                overflow = TextOverflow.Ellipsis,
                color = if (isDarkThemeMode(context = context)) Color.White else Color.Black,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth(.6f)
                    .horizontalScroll(rememberScrollState())
            )
        }

        Column(
            modifier = Modifier.align(CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = UIText.StringResource(R.string.magnitude)
                    .likeString() + " " + earthquake.mag.toString(),
                style = MaterialTheme.typography.body1,
                overflow = TextOverflow.Ellipsis,
                color = if (isDarkThemeMode(context = context)) Color.White else Color.Black,
                textAlign = TextAlign.End
            )

            Text(
                text = UIText.StringResource(R.string.depth)
                    .likeString() + " " + earthquake.depth.toString() + " " + UIText.StringResource(R.string._km)
                    .likeString(),
                style = MaterialTheme.typography.body2,
                overflow = TextOverflow.Ellipsis,
                color = if (isDarkThemeMode(context = context)) Color.White else Color.Black,
                textAlign = TextAlign.End
            )
        }
    }
}