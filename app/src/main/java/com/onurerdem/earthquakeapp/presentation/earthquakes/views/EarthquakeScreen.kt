package com.onurerdem.earthquakeapp.presentation.earthquakes.views

import android.Manifest
import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.work.BackoffPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.onurerdem.earthquakeapp.R
import com.onurerdem.earthquakeapp.domain.model.NavigationItem
import com.onurerdem.earthquakeapp.presentation.AlertDialogExample
import com.onurerdem.earthquakeapp.presentation.AppToolbar
import com.onurerdem.earthquakeapp.presentation.NavigationDrawerBody
import com.onurerdem.earthquakeapp.presentation.NavigationDrawerHeader
import com.onurerdem.earthquakeapp.presentation.Screen
import com.onurerdem.earthquakeapp.presentation.SharedPreferencesManager
import com.onurerdem.earthquakeapp.presentation.UIText
import com.onurerdem.earthquakeapp.presentation.allowNotification
import com.onurerdem.earthquakeapp.presentation.earthquakes.EarthquakesEvent
import com.onurerdem.earthquakeapp.presentation.earthquakes.EarthquakesViewModel
import com.onurerdem.earthquakeapp.presentation.isDarkThemeMode
import com.onurerdem.earthquakeapp.presentation.isTurkish
import com.onurerdem.earthquakeapp.presentation.saveAllowNotification
import com.onurerdem.earthquakeapp.presentation.saveIsTurkish
import com.onurerdem.earthquakeapp.presentation.saveThemeMode
import kotlinx.coroutines.launch
import com.onurerdem.earthquakeapp.presentation.NotificationWorker
import java.time.Duration
import java.util.Locale
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
@Composable
fun EarthquakeScreen(
    navController: NavController,
    viewModel: EarthquakesViewModel = hiltViewModel(),
    context: Context,
    sharedPreferencesManager: SharedPreferencesManager
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

    var darkTheme by remember { mutableStateOf(isDarkThemeMode(context = context)) }

    var isDMYorN by remember { mutableStateOf(sharedPreferencesManager.getDMYorN()) }

    var isTurkish by remember { mutableStateOf(isTurkish(context = context)) }

    var allowNotification by remember { mutableStateOf(allowNotification(context = context)) }

    val postNotificationPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
        } else {
            rememberPermissionState(permission = "android.permission.POST_NOTIFICATIONS")
        }

    val lifecycleOwner = LocalLifecycleOwner.current

    val workManager = WorkManager.getInstance(context)

    val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
        repeatInterval = 1,
        repeatIntervalTimeUnit = TimeUnit.DAYS,
        flexTimeInterval = 5,
        flexTimeIntervalUnit = TimeUnit.MINUTES
    ).setBackoffCriteria(
        backoffPolicy = BackoffPolicy.LINEAR,
        duration = Duration.ofSeconds(15)
    ).build()

    fun workManager() {
        if (postNotificationPermission.status.isGranted) {
            workManager.enqueueUniquePeriodicWork(
                "NotificationWork",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
            workManager.getWorkInfosForUniqueWorkLiveData("NotificationWork")
                .observe(lifecycleOwner) {
                    it.forEach { workInfo ->
                        Log.d("EarthquakeScreen", "${workInfo.state}")
                    }
                }
        } else {
            workManager.cancelUniqueWork("NotificationWork")
        }
    }

    LaunchedEffect(key1 = true) {
        if (!postNotificationPermission.status.isGranted) {
            postNotificationPermission.launchPermissionRequest()
        }
    }

    when {
        openExitAlertDialog.value -> {
            AlertDialogExample(
                onDismissRequest = { openExitAlertDialog.value = false },
                onConfirmation = {
                    openExitAlertDialog.value = false
                    activity?.finish()
                },
                dialogTitle = UIText.StringResource(R.string.exit).likeString(),
                dialogText = UIText.StringResource(R.string.are_you_sure_you_want_to_quit)
                    .likeString(),
                icon = Icons.Default.ExitToApp,
                iconContentColor = Color.Red,
                confirmButtonText = UIText.StringResource(R.string.yes).likeString(),
                dismissButtonText = UIText.StringResource(R.string.no).likeString(),
                dismissButtonColor = Color.Red,
                confirmButtonIcon = null,
                dismissButtonIcon = null,
                context = context
            )
        }

        openLogoutAlertDialog.value -> {
            AlertDialogExample(
                onDismissRequest = { openLogoutAlertDialog.value = false },
                onConfirmation = {
                    openLogoutAlertDialog.value = false
                    viewModel.logout(navController)
                },
                dialogTitle = UIText.StringResource(R.string.exit).likeString(),
                dialogText = UIText.StringResource(R.string.are_you_sure_you_want_to_log_out)
                    .likeString(),
                icon = Icons.Default.Logout,
                iconContentColor = Color.Red,
                confirmButtonText = UIText.StringResource(R.string.yes).likeString(),
                dismissButtonText = UIText.StringResource(R.string.no).likeString(),
                dismissButtonColor = Color.Red,
                confirmButtonIcon = null,
                dismissButtonIcon = null,
                context = context
            )
        }

        openLanguageAlertDialog.value -> {
            AlertDialogExample(
                onDismissRequest = {
                    openLanguageAlertDialog.value = false
                    if (!isTurkish(context = context)) {
                        isTurkish = !isTurkish
                        saveIsTurkish(
                            isTurkish = !isTurkish(context = context),
                            context = context
                        )
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        context.getSystemService(LocaleManager::class.java)
                            .applicationLocales = LocaleList.forLanguageTags("tr")
                    } else {
                        AppCompatDelegate.setApplicationLocales(
                            LocaleListCompat.forLanguageTags("tr")
                        )
                    }

                    if (isTurkish) {
                        Locale.setDefault(Locale("tr"))
                    }
                    val config = Configuration()
                    config.setLocale(Locale.getDefault())
                    @Suppress("DEPRECATION")
                    context.resources.updateConfiguration(config, context.resources.displayMetrics)

                    activity?.recreate()
                },
                onConfirmation = {
                    openLanguageAlertDialog.value = false
                    if (isTurkish(context = context)) {
                        isTurkish = !isTurkish
                        saveIsTurkish(
                            isTurkish = !isTurkish(context = context),
                            context = context
                        )
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        context.getSystemService(LocaleManager::class.java)
                            .applicationLocales = LocaleList.forLanguageTags("en")
                    } else {
                        AppCompatDelegate.setApplicationLocales(
                            LocaleListCompat.forLanguageTags("en")
                        )
                    }

                    if (!isTurkish) {
                        Locale.setDefault(Locale("en"))
                    }
                    val config = Configuration()
                    config.setLocale(Locale.getDefault())
                    @Suppress("DEPRECATION")
                    context.resources.updateConfiguration(config, context.resources.displayMetrics)

                    activity?.recreate()
                },
                dialogTitle = UIText.StringResource(R.string.language).likeString(),
                dialogText = UIText.StringResource(R.string.you_can_choose_the_language_you_want_to_use)
                    .likeString(),
                icon = Icons.Default.Language,
                iconContentColor = if (darkTheme) Color.White else Color.Black,
                confirmButtonText = UIText.StringResource(R.string.english).likeString(),
                dismissButtonText = UIText.StringResource(R.string.turkce).likeString(),
                dismissButtonColor = Color.Blue,
                confirmButtonIcon = null,
                dismissButtonIcon = null,
                context = context
            )
        }

        openNotificationAlertDialog.value -> {
            AlertDialogExample(
                onDismissRequest = {
                    openNotificationAlertDialog.value = false
                    if (allowNotification(context = context)) {
                        allowNotification = !allowNotification
                        saveAllowNotification(
                            allowNotification = !allowNotification(context = context),
                            context = context
                        )
                    }

                    workManager.cancelUniqueWork("NotificationWork")
                },
                onConfirmation = {
                    openNotificationAlertDialog.value = false
                    if (!allowNotification(context = context)) {
                        allowNotification = !allowNotification
                        saveAllowNotification(
                            allowNotification = !allowNotification(context = context),
                            context = context
                        )
                    }

                    workManager()
                },
                dialogTitle = UIText.StringResource(R.string.notification).likeString(),
                dialogText = UIText.StringResource(R.string.do_you_want_to_receive_notifications)
                    .likeString(),
                icon = Icons.Default.Notifications,
                iconContentColor = if (darkTheme) Color.White else Color.Black,
                confirmButtonText = UIText.StringResource(R.string.yes).likeString(),
                dismissButtonText = UIText.StringResource(R.string.no).likeString(),
                dismissButtonColor = Color.Red,
                confirmButtonIcon = null,
                dismissButtonIcon = null,
                context = context
            )
        }

        openThemeAlertDialog.value -> {
            AlertDialogExample(
                onDismissRequest = {
                    openThemeAlertDialog.value = false
                    if (isDarkThemeMode(context = context)) {
                        darkTheme = !darkTheme
                        saveThemeMode(
                            isDarkMode = !isDarkThemeMode(context = context),
                            context = context
                        )
                    }
                },
                onConfirmation = {
                    openThemeAlertDialog.value = false
                    if (!isDarkThemeMode(context = context)) {
                        darkTheme = !darkTheme
                        saveThemeMode(
                            isDarkMode = !isDarkThemeMode(context = context),
                            context = context
                        )
                    }
                },
                dialogTitle = UIText.StringResource(R.string.application_format).likeString(),
                dialogText = UIText.StringResource(R.string.you_can_choose_the_application_format_you_want_to_use)
                    .likeString(),
                icon = Icons.Default.Lightbulb,
                iconContentColor = if (darkTheme) Color.White else Color.Black,
                confirmButtonText = UIText.StringResource(R.string.dark).likeString(),
                dismissButtonText = UIText.StringResource(R.string.light).likeString(),
                dismissButtonColor = Color.Blue,
                confirmButtonIcon = Icons.Default.DarkMode,
                dismissButtonIcon = Icons.Default.LightMode,
                context = context
            )
        }

        openDateFormatAlertDialog.value -> {
            AlertDialogExample(
                onDismissRequest = {
                    openDateFormatAlertDialog.value = false
                    if (sharedPreferencesManager.getDateFormat() == "dd.MM.yyyy" || sharedPreferencesManager.getDateFormat() == null) {
                        isDMYorN = !isDMYorN
                        sharedPreferencesManager.saveDMYorN(isDMYorN = !sharedPreferencesManager.getDMYorN())
                        sharedPreferencesManager.saveDateFormat("MM.dd.yyyy")
                    }
                },
                onConfirmation = {
                    openDateFormatAlertDialog.value = false
                    if (sharedPreferencesManager.getDateFormat() == "MM.dd.yyyy") {
                        isDMYorN = !isDMYorN
                        sharedPreferencesManager.saveDMYorN(isDMYorN = !sharedPreferencesManager.getDMYorN())
                        sharedPreferencesManager.saveDateFormat("dd.MM.yyyy")
                    }
                },
                dialogTitle = UIText.StringResource(R.string.date_format).likeString(),
                dialogText = UIText.StringResource(R.string.you_can_choose_the_date_format_you_want_to_use)
                    .likeString(),
                icon = Icons.Default.CalendarMonth,
                iconContentColor = if (darkTheme) Color.White else Color.Black,
                confirmButtonText = UIText.StringResource(R.string.day_month_year).likeString(),
                dismissButtonText = UIText.StringResource(R.string.month_day_year).likeString(),
                dismissButtonColor = Color.Blue,
                confirmButtonIcon = null,
                dismissButtonIcon = null,
                context = context
            )
        }
    }

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    viewModel.getUserData()

    val navigationItemsList = listOf<NavigationItem>(
        NavigationItem(
            title = UIText.StringResource(R.string.language).likeString(),
            icon = Icons.Default.Language,
            description = UIText.StringResource(R.string.language_setting).likeString(),
            itemId = "languageSetting"
        ),
        NavigationItem(
            title = UIText.StringResource(R.string.notification).likeString(),
            icon = Icons.Default.Notifications,
            description = UIText.StringResource(R.string.notification_setting).likeString(),
            itemId = "notificationSetting"
        ),
        NavigationItem(
            title = UIText.StringResource(R.string.application_format).likeString(),
            icon = Icons.Default.Lightbulb,
            description = UIText.StringResource(R.string.application_sormat_setting).likeString(),
            itemId = "themeSetting"
        ),
        NavigationItem(
            title = UIText.StringResource(R.string.date_format).likeString(),
            icon = Icons.Default.CalendarMonth,
            description = UIText.StringResource(R.string.date_format_setting).likeString(),
            itemId = "dateFormatSetting"
        )
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppToolbar(
                toolbarTitle = UIText.StringResource(R.string.earthquakes).likeString(),
                logoutButtonClicked = {
                    openLogoutAlertDialog.value = !openLogoutAlertDialog.value
                },
                navigationIconClicked = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                },
                darkTheme = darkTheme
            )
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            NavigationDrawerHeader(viewModel.emailId.value, darkTheme = darkTheme)
            NavigationDrawerBody(
                navigationDrawerItems = navigationItemsList,
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
                },
                darkTheme = darkTheme,
                onThemeUpdated = {
                    darkTheme = !darkTheme
                    saveThemeMode(
                        isDarkMode = !isDarkThemeMode(context = context),
                        context = context
                    )
                },
                onDateFormatUpdated = {
                    if (sharedPreferencesManager.getDateFormat() == "dd.MM.yyyy" || sharedPreferencesManager.getDateFormat() == null) {
                        isDMYorN = !isDMYorN
                        sharedPreferencesManager.saveDMYorN(isDMYorN = !sharedPreferencesManager.getDMYorN())
                        sharedPreferencesManager.saveDateFormat("MM.dd.yyyy")
                    } else {
                        isDMYorN = !isDMYorN
                        sharedPreferencesManager.saveDMYorN(isDMYorN = !sharedPreferencesManager.getDMYorN())
                        sharedPreferencesManager.saveDateFormat("dd.MM.yyyy")
                    }
                },
                onLanguageUpdated = {
                    isTurkish = !isTurkish
                    saveIsTurkish(
                        isTurkish = !isTurkish(context = context),
                        context = context
                    )
                    if (isTurkish == true) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            context.getSystemService(LocaleManager::class.java)
                                .applicationLocales = LocaleList.forLanguageTags("tr")
                        } else {
                            AppCompatDelegate.setApplicationLocales(
                                LocaleListCompat.forLanguageTags("tr")
                            )
                        }

                        Locale.setDefault(Locale("tr"))
                        val config = Configuration()
                        config.setLocale(Locale.getDefault())
                        @Suppress("DEPRECATION")
                        context.resources.updateConfiguration(config, context.resources.displayMetrics)

                        activity?.recreate()
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            context.getSystemService(LocaleManager::class.java)
                                .applicationLocales = LocaleList.forLanguageTags("en")
                        } else {
                            AppCompatDelegate.setApplicationLocales(
                                LocaleListCompat.forLanguageTags("en")
                            )
                        }

                        Locale.setDefault(Locale("en"))
                        val config = Configuration()
                        config.setLocale(Locale.getDefault())
                        @Suppress("DEPRECATION")
                        context.resources.updateConfiguration(config, context.resources.displayMetrics)

                        activity?.recreate()
                    }
                },
                onAllowNotificationUpdated = {
                    allowNotification = !allowNotification
                    saveAllowNotification(
                        allowNotification = !allowNotification(context = context),
                        context = context
                    )

                    if (allowNotification == true) {
                        workManager()
                    } else {
                        workManager.cancelUniqueWork("NotificationWork")
                    }
                },
                isDMYorN = isDMYorN,
                isTurkish = isTurkish,
                allowNotification = allowNotification
            )
        }

    ) { paddingValues ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(if (darkTheme) Color.DarkGray else Color.White)
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (darkTheme) Color.DarkGray else Color.White)
                    .pullRefresh(pullRefreshState)
            ) {
                Column {
                    EarthquakeSearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        hint = UIText.StringResource(R.string.city_etc_search).likeString(),
                        onSearch = {
                            viewModel.onEvent(EarthquakesEvent.Search(it))
                        },
                        darktheme = darkTheme
                    )

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.earthquakes) { earthquake ->
                            EarthquakeListRow(
                                earthquake = earthquake,
                                onItemClick = {
                                    navController.navigate(Screen.EarthquakeDetailScreen.route + "/${earthquake.earthquake_id}")
                                },
                                context = context
                            )
                        }
                    }
                }

                if (state.error.isNotBlank()) {
                    androidx.compose.material.Text(
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
    onSearch: (String) -> Unit = {},
    darktheme: Boolean
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
            textStyle = TextStyle(color = if (darktheme) Color.White else Color.Black),
            shape = RoundedCornerShape(40.dp),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = if (darktheme) Color.DarkGray else Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, CircleShape)
                .background(if (darktheme) Color.White else Color.Black, CircleShape)
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
                    contentDescription = UIText.StringResource(R.string.search_icon).likeString(),
                    tint = if (darktheme) Color.LightGray else Color.Gray
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
                    contentDescription = UIText.StringResource(R.string.close_icon).likeString(),
                    tint = if (darktheme) Color.LightGray else Color.Gray
                )
            }
        )

        if (isHintDisplayed) {
            Text(
                text = hint,
                color = if (darktheme) Color.Gray else Color.LightGray,
                modifier = Modifier.padding(horizontal = 55.dp, vertical = 20.dp)
            )
        }

    }
}