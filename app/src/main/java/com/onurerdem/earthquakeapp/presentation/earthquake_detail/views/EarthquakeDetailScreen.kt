package com.onurerdem.earthquakeapp.presentation.earthquake_detail.views

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.onurerdem.earthquakeapp.R
import com.onurerdem.earthquakeapp.presentation.SharedPreferencesManager
import com.onurerdem.earthquakeapp.presentation.UIText
import com.onurerdem.earthquakeapp.presentation.earthquake_detail.EarthquakeDetailViewModel
import com.onurerdem.earthquakeapp.presentation.formatDateForTurkishLocale
import com.onurerdem.earthquakeapp.presentation.isDarkThemeMode
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun EarthquakeDetailScreen(
    earthquakeDetailViewModel: EarthquakeDetailViewModel = hiltViewModel(),
    context: Context
) {
    val state = earthquakeDetailViewModel.state.value

    val cameraPositionState = rememberCameraPositionState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDarkThemeMode(context = context)) Color.DarkGray else Color.White),
        contentAlignment = Alignment.Center
    ) {
        state.earthquake?.let {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = UIText.StringResource(R.string.location_point_of_the_earthquake)
                        .likeString(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp),
                    color = if (isDarkThemeMode(context = context)) Color.White else Color.Black,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )

                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                        .padding(horizontal = 8.dp),
                    cameraPositionState = cameraPositionState
                ) {
                    state.earthquake.geojson.coordinates.let {
                        MarkerInfoWindow(
                            state = rememberMarkerState(position = LatLng(it.get(1), it.get(0)))
                        )

                        val scope = rememberCoroutineScope()
                        MapEffect(key1 = it) { map ->
                            map.setOnMapLoadedCallback {
                                scope.launch {
                                    cameraPositionState.animate(
                                        update = CameraUpdateFactory.newLatLngZoom(
                                            LatLng(it.get(1), it.get(0)),
                                            8f
                                        ),
                                    )
                                }
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = UIText.StringResource(R.string.details_of_the_earthquake)
                            .likeString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp),
                        color = if (isDarkThemeMode(context = context)) Color.White else Color.Black,
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = it.title,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp),
                        color = if (isDarkThemeMode(context = context)) Color.White else Color.Black
                    )

                    Text(
                        text = UIText.StringResource(R.string.date)
                            .likeString() + " " + formatDateForTurkishLocale(
                            date = it.date.substring(0, 10),
                            sharedPreferencesManager = SharedPreferencesManager(context = context)
                        ) + ", " + UIText.StringResource(R.string._hour)
                            .likeString() + " " + it.date.substring(10, 19),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp),
                        color = if (isDarkThemeMode(context = context)) Color.White else Color.Black
                    )

                    Text(
                        text = UIText.StringResource(R.string.earthquake_information_provider)
                            .likeString() + " " + it.provider.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        },
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp),
                        color = if (isDarkThemeMode(context = context)) Color.White else Color.Black
                    )

                    Text(
                        text = UIText.StringResource(R.string.earthquake_magnitude)
                            .likeString() + " " + it.mag.toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp),
                        color = if (isDarkThemeMode(context = context)) Color.White else Color.Black
                    )

                    Text(
                        text = UIText.StringResource(R.string.earthquake_magnitude)
                            .likeString() + " " + it.depth.toString() + " " + UIText.StringResource(R.string._km)
                            .likeString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp),
                        color = if (isDarkThemeMode(context = context)) Color.White else Color.Black
                    )

                    Text(
                        text = UIText.StringResource(R.string.population_of_the_city)
                            .likeString() + " " + it.population.toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp),
                        color = if (isDarkThemeMode(context = context)) Color.White else Color.Black
                    )

                    Column {
                        Text(
                            text = UIText.StringResource(R.string.cities_close_to_the_earthquake).likeString(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp),
                            color = if (isDarkThemeMode(context = context)) Color.White else Color.Black,
                            style = MaterialTheme.typography.subtitle1,
                            fontWeight = FontWeight.Bold
                        )

                        LazyRow(modifier = Modifier.fillMaxWidth()) {
                            items(state.earthquake.closestCities) { closestCities ->
                                EarthquakeDetailListRow(
                                    closestCity = closestCities,
                                    context = context
                                )
                            }
                        }

                        Text(
                            text = UIText.StringResource(R.string.airports_close_to_the_earthquake).likeString(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp),
                            color = if (isDarkThemeMode(context = context)) Color.White else Color.Black,
                            style = MaterialTheme.typography.subtitle1,
                            fontWeight = FontWeight.Bold
                        )

                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            items(state.earthquake.airports) { airports ->
                                EarthquakeDetailListRow(airports = airports, context = context)
                            }
                        }
                    }
                }
            }
        }

        if (state.error.isNotBlank()) {
            Text(
                text = state.error,
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
                    .align(Alignment.Center)
            )
        }

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}