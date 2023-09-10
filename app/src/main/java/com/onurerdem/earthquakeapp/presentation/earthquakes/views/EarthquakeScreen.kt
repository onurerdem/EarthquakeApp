package com.onurerdem.earthquakeapp.presentation.earthquakes.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.onurerdem.earthquakeapp.presentation.earthquakes.EarthquakesEvent
import com.onurerdem.earthquakeapp.presentation.earthquakes.EarthquakesViewModel

@Composable
fun EarthquakeScreen(
    navController: NavController,
    viewModel: EarthquakesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        Column {
            EarthquakeSearchBar(modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
                hint = "Search etc...",
                onSearch = {
                    viewModel.onEvent(EarthquakesEvent.Search(it))
                }
            )
            
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.earthquakes) {earthquake ->
                    EarthquakeListRow(earthquake = earthquake, onItemClick = {
                        //navController.navigate(Screen.EarthquakeDetailScreen.route + "id")
                    })
                }
            }
        }
    }
}

@Composable
fun EarthquakeSearchBar(
    modifier: Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }

    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier) {
        TextField(value = text,
            onValueChange = {
            text = it
        },
            keyboardActions = KeyboardActions(onDone = {
                onSearch(text)
            }),
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            shape = RoundedCornerShape(40.dp),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, CircleShape)
                .background(Color.Black, CircleShape)
                .padding(4.dp)
                .onFocusChanged {
                    isHintDisplayed = it.isFocused != true && text.isEmpty()
                }
        )

        if (isHintDisplayed) {
            Text(text = hint,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
                )
        }
    }
}