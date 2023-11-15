package com.onurerdem.earthquakeapp.presentation.earthquakes.views

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.onurerdem.earthquakeapp.presentation.AlertDialogExample
import com.onurerdem.earthquakeapp.presentation.AppToolbar
import com.onurerdem.earthquakeapp.presentation.NavigationDrawerBody
import com.onurerdem.earthquakeapp.presentation.NavigationDrawerHeader
import com.onurerdem.earthquakeapp.presentation.Screen
import com.onurerdem.earthquakeapp.presentation.earthquakes.EarthquakesEvent
import com.onurerdem.earthquakeapp.presentation.earthquakes.EarthquakesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EarthquakeScreen(
    navController: NavController,
    viewModel: EarthquakesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val pullRefreshState =
        rememberPullRefreshState(isRefreshing, { viewModel.onEvent(EarthquakesEvent.Search("")) })

    val openExitAlertDialog = remember { mutableStateOf(false) }
    val openLogoutAlertDialog = remember { mutableStateOf(false) }
    val openLanguageAlertDialog = remember { mutableStateOf(false) }
    val openNotificationAlertDialog = remember { mutableStateOf(false) }
    val openThemeAlertDialog = remember { mutableStateOf(false) }
    val openDateFormatAlertDialog = remember { mutableStateOf(false) }

    val activity = (LocalContext.current as? Activity)

    when {
        openExitAlertDialog.value -> {
            AlertDialogExample(
                onDismissRequest = { openExitAlertDialog.value = false },
                onConfirmation = {
                    openExitAlertDialog.value = false
                    activity?.finish()
                },
                dialogTitle = "Çıkış",
                dialogText = "Çıkmak istediğinize emin misiniz?",
                icon = Icons.Default.ExitToApp,
                iconContentColor = Color.Red,
                confirmButtonText = "Evet",
                dismissButtonText = "Hayır",
                dismissButtonColor = Color.Red,
                condirmButtonIcon = null,
                dismissButtonIcon = null
            )
        }

        openLogoutAlertDialog.value -> {
            AlertDialogExample(
                onDismissRequest = { openLogoutAlertDialog.value = false },
                onConfirmation = {
                    openLogoutAlertDialog.value = false
                    viewModel.logout(navController)
                },
                dialogTitle = "Çıkış",
                dialogText = "Oturumunuzu kapatmak istediğinize emin misiniz?",
                icon = Icons.Default.Logout,
                iconContentColor = Color.Red,
                confirmButtonText = "Evet",
                dismissButtonText = "Hayır",
                dismissButtonColor = Color.Red,
                condirmButtonIcon = null,
                dismissButtonIcon = null
            )
        }

        openLanguageAlertDialog.value -> {
            AlertDialogExample(
                onDismissRequest = { openLanguageAlertDialog.value = false },
                onConfirmation = {
                    openLanguageAlertDialog.value = false
                },
                dialogTitle = "Dil",
                dialogText = "Kullanmak istediğiniz dili seçebilirsiniz.",
                icon = Icons.Default.Language,
                iconContentColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                confirmButtonText = "English",
                dismissButtonText = "Türkçe",
                dismissButtonColor = Color.Blue,
                condirmButtonIcon = null,
                dismissButtonIcon = null
            )
        }

        openNotificationAlertDialog.value -> {
            AlertDialogExample(
                onDismissRequest = { openNotificationAlertDialog.value = false },
                onConfirmation = {
                    openNotificationAlertDialog.value = false
                },
                dialogTitle = "Bildirim",
                dialogText = "Bildirim almak istiyor musunuz?",
                icon = Icons.Default.Notifications,
                iconContentColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                confirmButtonText = "Evet",
                dismissButtonText = "Hayır",
                dismissButtonColor = Color.Red,
                condirmButtonIcon = null,
                dismissButtonIcon = null
            )
        }

        openThemeAlertDialog.value -> {
            AlertDialogExample(
                onDismissRequest = { openThemeAlertDialog.value = false },
                onConfirmation = {
                    openThemeAlertDialog.value = false
                },
                dialogTitle = "Uygulama Biçimi",
                dialogText = "Kullanmak istediğiniz uygulama biçimini seçebilirsiniz.",
                icon = Icons.Default.Lightbulb,
                iconContentColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                confirmButtonText = "Karanlık",
                dismissButtonText = "Aydınlık",
                dismissButtonColor = Color.Blue,
                condirmButtonIcon = Icons.Default.DarkMode,
                dismissButtonIcon = Icons.Default.LightMode
            )
        }

        openDateFormatAlertDialog.value -> {
            AlertDialogExample(
                onDismissRequest = { openDateFormatAlertDialog.value = false },
                onConfirmation = {
                    openDateFormatAlertDialog.value = false
                },
                dialogTitle = "Tarih Biçimi",
                dialogText = "Kullanmak istediğiniz tarih biçimini seçebilirsiniz.",
                icon = Icons.Default.CalendarMonth,
                iconContentColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                confirmButtonText = "Gün-Ay-Yıl",
                dismissButtonText = "Ay-Gün-Yıl",
                dismissButtonColor = Color.Blue,
                condirmButtonIcon = null,
                dismissButtonIcon = null
            )
        }
    }

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    viewModel.getUserData()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppToolbar(toolbarTitle = "Depremler",
                logoutButtonClicked = {
                    openLogoutAlertDialog.value = !openLogoutAlertDialog.value
                },
                navigationIconClicked = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            )
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            NavigationDrawerHeader(viewModel.emailId.value)
            NavigationDrawerBody(
                navigationDrawerItems = viewModel.navigationItemsList,
                onNavigationItemClicked = {
                    Log.d("ComingHere", "inside_NavigationItemClicked")
                    Log.d("ComingHere", "${it.itemId} ${it.title}")
                    when {
                        it.itemId == "languageSetting" -> {
                            openLanguageAlertDialog.value = !openLanguageAlertDialog.value
                        }

                        it.itemId == "notificationSetting" -> {
                            openNotificationAlertDialog.value = !openNotificationAlertDialog.value
                        }

                        it.itemId == "themeSetting" -> {
                            openThemeAlertDialog.value = !openThemeAlertDialog.value
                        }

                        it.itemId == "dateFormatSetting" -> {
                            openDateFormatAlertDialog.value = !openDateFormatAlertDialog.value
                        }
                    }
                }
            )
        }

    ) { paddingValues ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isSystemInDarkTheme()) Color.DarkGray else Color.White)
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (isSystemInDarkTheme()) Color.DarkGray else Color.White)
                    .pullRefresh(pullRefreshState)
            ) {
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
                        items(state.earthquakes) { earthquake ->
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
    }

    BackHandler {
        openExitAlertDialog.value = !openExitAlertDialog.value
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
                if ((prevLength == 3 && it.length < 3) || it.length > 2) {
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
            textStyle = TextStyle(color = if (isSystemInDarkTheme()) Color.White else Color.Black),
            shape = RoundedCornerShape(40.dp),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, CircleShape)
                .background(if (isSystemInDarkTheme()) Color.White else Color.Black, CircleShape)
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
                    contentDescription = "Search icon.",
                    tint = if (isSystemInDarkTheme()) Color.LightGray else Color.Gray
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
                    contentDescription = "Close icon.",
                    tint = if (isSystemInDarkTheme()) Color.LightGray else Color.Gray
                )
            }
        )

        if (isHintDisplayed) {
            Text(
                text = hint,
                color = if (isSystemInDarkTheme()) Color.Gray else Color.LightGray,
                modifier = Modifier.padding(horizontal = 55.dp, vertical = 20.dp)
            )
        }

    }
}