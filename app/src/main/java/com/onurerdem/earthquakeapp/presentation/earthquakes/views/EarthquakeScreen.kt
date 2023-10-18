package com.onurerdem.earthquakeapp.presentation.earthquakes.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.onurerdem.earthquakeapp.presentation.Screen
import com.onurerdem.earthquakeapp.presentation.earthquakes.EarthquakesEvent
import com.onurerdem.earthquakeapp.presentation.earthquakes.EarthquakesViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EarthquakeScreen(
    navController: NavController,
    viewModel: EarthquakesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val pullRefreshState = rememberPullRefreshState(isRefreshing, { viewModel.onEvent(EarthquakesEvent.Search("")) })

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .pullRefresh(pullRefreshState)) {
        Column {
            EarthquakeSearchBar(modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
                hint = "Şehir vb. ara...",
                onSearch = {
                    viewModel.onEvent(EarthquakesEvent.Search(it))
                }
            )
            
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.earthquakes) {earthquake ->
                    EarthquakeListRow(earthquake = earthquake, onItemClick = {
                        navController.navigate(Screen.EarthquakeDetailScreen.route + "/${earthquake.earthquake_id}")
                    })
                }
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
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

    val focusManager = LocalFocusManager.current

    var prevLength by remember { mutableStateOf(0) }

    Box(modifier = modifier) {
        TextField(value = text,
            onValueChange = {
                text = it
                if((prevLength == 3 && it.length < 3) || it.length > 2) {
                    onSearch(text)
                }
                prevLength = it.length
            },
            keyboardActions = KeyboardActions(onDone = {
                onSearch(text)
                focusManager.clearFocus()
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
                    isHintDisplayed = (!it.isFocused) && text.isEmpty()
                },
            leadingIcon = {
                Icon(
                    modifier = Modifier.clickable {
                        onSearch(text)
                        focusManager.clearFocus()
                    },
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search icon."
                )
            },
            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable {
                        text = ""
                        onSearch(text)
                        focusManager.clearFocus()
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close icon."
                )
            }
        )

        if (isHintDisplayed) {
            Text(text = hint,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 55.dp, vertical = 20.dp)
                )
        }

    }
}