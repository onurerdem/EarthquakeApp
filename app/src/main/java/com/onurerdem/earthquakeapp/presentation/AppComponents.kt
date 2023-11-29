package com.onurerdem.earthquakeapp.presentation

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.onurerdem.earthquakeapp.R
import com.onurerdem.earthquakeapp.domain.model.NavigationItem
import com.onurerdem.earthquakeapp.presentation.ui.theme.EarthquakeAppTheme
import com.onurerdem.earthquakeapp.presentation.ui.theme.componentShapes

@Composable
fun NormalTextComponent(value: String, context: Context) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ), color = if (isDarkThemeMode(context = context)) Color.White else Color.Black,
        textAlign = TextAlign.Center
    )
}

@Composable
fun HeadingTextComponent(value: String, context: Context) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        ), color = if (isDarkThemeMode(context = context)) Color.White else Color.Black,
        textAlign = TextAlign.Center
    )
}

@Composable
fun ErrorTextComponent(value: String?) {
    if (value != null) {
        Text(
            text = value,
            modifier = Modifier
                .fillMaxWidth(),
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal
            ), color = Color.Red,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun MyTextFieldComponent(
    labelValue: String, painterResource: Painter,
    onTextChanged: (String) -> Unit,
    errorStatus: Boolean = false,
    screen: Screen,
    context: Context
) {

    val textValue = remember {
        mutableStateOf("")
    }
    val localFocusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(componentShapes.small),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Blue,
            focusedLabelColor = Color.Blue,
            cursorColor = Color.Blue,
            backgroundColor = if (isDarkThemeMode(context = context)) Color.DarkGray else Color.White,
            textColor = if (isDarkThemeMode(context = context)) Color.White else Color.Black
        ),
        keyboardOptions = if (screen == Screen.ForgotPasswordScreen) {
            KeyboardOptions(imeAction = ImeAction.Done)
        } else {
            KeyboardOptions(imeAction = ImeAction.Next)
        },
        keyboardActions = KeyboardActions {
            if (screen == Screen.ForgotPasswordScreen) {
                localFocusManager.clearFocus()
            } else {
                localFocusManager.moveFocus(focusDirection = FocusDirection.Next)
            }
        },
        singleLine = true,
        maxLines = 1,
        value = textValue.value,
        onValueChange = {
            textValue.value = it
            onTextChanged(it)
        },
        leadingIcon = {
            Icon(painter = painterResource, contentDescription = "")
        },
        isError = !errorStatus
    )
}

@Composable
fun PasswordTextFieldComponent(
    labelValue: String, painterResource: Painter,
    onTextSelected: (String) -> Unit,
    errorStatus: Boolean = false,
    context: Context
) {

    val localFocusManager = LocalFocusManager.current
    val password = remember {
        mutableStateOf("")
    }

    val passwordVisible = remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(componentShapes.small),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Blue,
            focusedLabelColor = Color.Blue,
            cursorColor = Color.Blue,
            backgroundColor = if (isDarkThemeMode(context = context)) Color.DarkGray else Color.White,
            textColor = if (isDarkThemeMode(context = context)) Color.White else Color.Black
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()
        },
        maxLines = 1,
        value = password.value,
        onValueChange = {
            password.value = it
            onTextSelected(it)
        },
        leadingIcon = {
            Icon(painter = painterResource, contentDescription = "")
        },
        trailingIcon = {

            val iconImage = if (passwordVisible.value) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }

            val description = if (passwordVisible.value) {
                UIText.StringResource(R.string.hide_password).likeString()
            } else {
                UIText.StringResource(R.string.show_password).likeString()
            }

            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                Icon(imageVector = iconImage, contentDescription = description)
            }

        },
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        isError = !errorStatus
    )
}

@Composable
fun CheckboxComponent(
    onTextSelected: (String) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    context: Context
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(56.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        val checkedState = remember {
            mutableStateOf(false)
        }

        Checkbox(checked = checkedState.value,
            onCheckedChange = {
                checkedState.value = !checkedState.value
                onCheckedChange.invoke(it)
            })

        ClickableTextComponent(onTextSelected, context = context)
    }
}

@Composable
fun ClickableTextComponent(onTextSelected: (String) -> Unit, context: Context) {
    val initialText =
        UIText.StringResource(R.string.by_continuing_you_agree_that).likeString() + " "
    val privacyPolicyText =
        UIText.StringResource(R.string.by_continuing_you_agree_that).likeString()
    val andText = " " + UIText.StringResource(R.string.and).likeString() + " "
    val termsAndConditionsText = UIText.StringResource(R.string.terms_of_use).likeString()

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = if (isDarkThemeMode(context = context)) Color.White else Color.Black)) {
            pushStringAnnotation(tag = initialText, annotation = initialText)
            append(initialText)
        }
        withStyle(style = SpanStyle(color = if (isDarkThemeMode(context = context)) Color.Cyan else Color.Blue)) {
            pushStringAnnotation(tag = privacyPolicyText, annotation = privacyPolicyText)
            append(privacyPolicyText)
        }
        withStyle(style = SpanStyle(color = if (isDarkThemeMode(context = context)) Color.White else Color.Black)) {
            pushStringAnnotation(tag = andText, annotation = andText)
            append(andText)
        }
        withStyle(style = SpanStyle(color = if (isDarkThemeMode(context = context)) Color.Cyan else Color.Blue)) {
            pushStringAnnotation(tag = termsAndConditionsText, annotation = termsAndConditionsText)
            append(termsAndConditionsText)
        }
    }

    ClickableText(text = annotatedString, onClick = { offset ->

        annotatedString.getStringAnnotations(offset, offset)
            .firstOrNull()?.also { span ->
                Log.d("ClickableTextComponent", "{${span.item}}")

                if ((span.item == termsAndConditionsText) || (span.item == privacyPolicyText)) {
                    onTextSelected(span.item)
                }
            }

    })
}

@Composable
fun ButtonComponent(value: String, onButtonClicked: () -> Unit, isEnabled: Boolean = false) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        onClick = {
            onButtonClicked.invoke()
        },
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        shape = RoundedCornerShape(50.dp),
        enabled = isEnabled
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            if (isEnabled == false) Color.Gray else Color.Cyan,
                            if (isEnabled == false) Color.Gray else Color.Blue
                        )
                    ),
                    shape = RoundedCornerShape(50.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

        }

    }
}

@Composable
fun DividerTextComponent(context: Context) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = Color.Gray,
            thickness = 1.dp
        )

        Text(
            modifier = Modifier.padding(8.dp),
            text = UIText.StringResource(R.string.or).likeString(),
            fontSize = 18.sp,
            color = if (isDarkThemeMode(context = context)) Color.White else Color.Black
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = Color.Gray,
            thickness = 1.dp
        )
    }
}


@Composable
fun ClickableLoginTextComponent(
    tryingToLogin: Boolean = true,
    onTextSelected: (String) -> Unit,
    context: Context
) {
    val initialText =
        if (tryingToLogin) UIText.StringResource(R.string.already_have_an_account)
            .likeString() + " " else UIText.StringResource(R.string.dont_have_an_account_yet)
            .likeString() + " "
    val loginText = if (tryingToLogin) UIText.StringResource(R.string.login)
        .likeString() else UIText.StringResource(R.string.register).likeString()

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = if (isDarkThemeMode(context = context)) Color.White else Color.Black)) {
            pushStringAnnotation(tag = initialText, annotation = initialText)
            append(initialText)
        }
        withStyle(style = SpanStyle(color = if (isDarkThemeMode(context = context)) Color.Cyan else Color.Blue)) {
            pushStringAnnotation(tag = loginText, annotation = loginText)
            append(loginText)
        }
    }

    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 21.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center
        ),
        text = annotatedString,
        onClick = { offset ->

            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    Log.d("ClickableTextComponent", "{${span.item}}")

                    if (span.item == loginText) {
                        onTextSelected(span.item)
                    }
                }

        },
    )
}

@Composable
fun ClickableUnderLinedTextComponent(value: String, onClick: () -> Unit) {
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle()) {
            pushStringAnnotation(tag = value, annotation = value)
            append(value)
        }
    }

    ClickableText(
        text = annotatedString,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline
        ),
        onClick = { offset ->

            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    Log.d("ClickableUnderLinedTextComponent", "{${span.item}}")

                    onClick()
                }

        }
    )
}

@Composable
fun AppToolbar(
    toolbarTitle: String, logoutButtonClicked: () -> Unit,
    navigationIconClicked: () -> Unit,
    darkTheme: Boolean
) {
    TopAppBar(
        backgroundColor = if (darkTheme) Color.Black else Color.Blue,
        title = {
            Text(
                text = toolbarTitle, color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                navigationIconClicked.invoke()
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = UIText.StringResource(R.string.menu).likeString(),
                    tint = Color.White
                )
            }
        },
        actions = {
            IconButton(onClick = {
                logoutButtonClicked.invoke()
            }) {
                Icon(
                    imageVector = Icons.Filled.Logout,
                    contentDescription = UIText.StringResource(R.string.log_out).likeString(),
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
fun NavigationDrawerHeader(value: String?, darkTheme: Boolean) {
    Box(
        modifier = Modifier
            .background(
                Brush.horizontalGradient(
                    listOf(
                        if (darkTheme) Color(0xFF1F1F20) else Color.Blue,
                        if (darkTheme) Color(0xFF23212E) else Color.Cyan
                    )
                )
            )
            .fillMaxWidth()
            .fillMaxHeight(0.23f)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.profile_animation))

            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.fillMaxSize(0.7f),
                alignment = Alignment.TopCenter
            )

            Spacer(modifier = Modifier.heightIn(12.dp))

            Row {
                NavigationDrawerText(
                    title = UIText.StringResource(R.string.email_).likeString() + " ",
                    textUnit = 14.sp,
                    color = Color.White,
                    shadowColor = if (darkTheme) Color.Gray else Color.Black,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold
                )
                NavigationDrawerText(
                    title = value ?: UIText.StringResource(R.string.application_drawer)
                        .likeString(),
                    textUnit = 14.sp,
                    color = Color.White,
                    shadowColor = if (darkTheme) Color.Gray else Color.Black,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun NavigationDrawerBody(
    navigationDrawerItems: List<NavigationItem>,
    onNavigationItemClicked: (NavigationItem) -> Unit,
    darkTheme: Boolean = false,
    onThemeUpdated: () -> Unit,
    onDateFormatUpdated: () -> Unit,
    onLanguageUpdated: () -> Unit,
    isDMYorN: Boolean = false,
    isTurkish: Boolean = false
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(if (darkTheme) Color.DarkGray else Color.White)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = UIText.StringResource(R.string.settings).likeString(),
                    tint = if (darkTheme) Color.White else Color.Black
                )

                Spacer(modifier = Modifier.width(16.dp))

                NavigationDrawerText(
                    title = UIText.StringResource(R.string.capital_settings).likeString(),
                    textUnit = 24.sp,
                    color = if (darkTheme) Color.White else Color.Black,
                    shadowColor = if (darkTheme) Color.Gray else Color.LightGray,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        items(navigationDrawerItems) {
            Row {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    NavigationItemRow(item = it, onNavigationItemClicked, darkTheme)

                    when {
                        it.itemId == "themeSetting" -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 16.dp)
                            ) {
                                EarthquakeAppTheme(darkTheme = darkTheme) {
                                    ThemeSwitcher(
                                        darkTheme = darkTheme,
                                        size = 36.dp,
                                        padding = 5.dp,
                                        onClick = onThemeUpdated
                                    )
                                }
                            }
                        }

                        it.itemId == "dateFormatSetting" -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 16.dp)
                            ) {
                                EarthquakeAppTheme(darkTheme = darkTheme) {
                                    DateFormatSwitcher(
                                        size = 36.dp,
                                        padding = 5.dp,
                                        onClick = onDateFormatUpdated,
                                        isDMYorN = isDMYorN
                                    )
                                }
                            }
                        }

                        it.itemId == "languageSetting" -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 16.dp)
                            ) {
                                EarthquakeAppTheme(darkTheme = darkTheme) {
                                    LanguageSwitcher(
                                        size = 36.dp,
                                        padding = 5.dp,
                                        onClick = onLanguageUpdated,
                                        isTurkish = isTurkish
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationItemRow(
    item: NavigationItem,
    onNavigationItemClicked: (NavigationItem) -> Unit,
    darkTheme: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onNavigationItemClicked.invoke(item)
            }
            .padding(all = 16.dp)
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.description,
            tint = if (darkTheme) Color.White else Color.Black
        )

        Spacer(modifier = Modifier.width(16.dp))

        NavigationDrawerText(
            title = item.title,
            textUnit = 18.sp,
            color = if (darkTheme) Color.White else Color.Black,
            shadowColor = if (darkTheme) Color.Gray else Color.LightGray,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun NavigationDrawerText(
    title: String,
    textUnit: TextUnit,
    color: Color,
    shadowColor: Color,
    fontStyle: FontStyle,
    fontWeight: FontWeight
) {
    val shadowOffset = Offset(4f, 6f)

    Text(
        text = title,
        style = TextStyle(
            color = color,
            fontSize = textUnit,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            shadow = Shadow(
                color = shadowColor,
                offset = shadowOffset, 2f
            )
        )
    )
}

@Composable
fun ThemeSwitcher(
    darkTheme: Boolean = false,
    size: Dp = 150.dp,
    iconSize: Dp = size / 3,
    padding: Dp = 10.dp,
    borderWidth: Dp = 1.dp,
    parentShape: Shape = CircleShape,
    toggleShape: Shape = CircleShape,
    animationSpec: AnimationSpec<Dp> = tween(durationMillis = 300),
    onClick: () -> Unit
) {
    val offset by animateDpAsState(
        targetValue = if (darkTheme) 0.dp else size,
        animationSpec = animationSpec
    )

    Box(modifier = Modifier
        .width(size * 2)
        .height(size)
        .clip(shape = parentShape)
        .clickable { onClick() }
        .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .offset(x = offset)
                .padding(all = padding)
                .clip(shape = toggleShape)
                .background(MaterialTheme.colorScheme.primary)
        ) {}
        Row(
            modifier = Modifier
                .border(
                    border = BorderStroke(
                        width = borderWidth,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    shape = parentShape
                )
        ) {
            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    imageVector = Icons.Default.Nightlight,
                    contentDescription = "Theme Icon",
                    tint = if (darkTheme) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.primary
                )
            }
            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    imageVector = Icons.Default.LightMode,
                    contentDescription = "Theme Icon",
                    tint = if (darkTheme) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondaryContainer
                )
            }
        }
    }
}

fun saveThemeMode(isDarkMode: Boolean, context: Context) {
    val sharedPreferences = context.getSharedPreferences("ThemePreferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("isDarkMode", isDarkMode)
    editor.apply()
}

fun isDarkThemeMode(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("ThemePreferences", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("isDarkMode", false)
}

@Composable
fun DateFormatSwitcher(
    size: Dp = 150.dp,
    iconSize: Dp = size / 3,
    padding: Dp = 10.dp,
    borderWidth: Dp = 1.dp,
    parentShape: Shape = CircleShape,
    toggleShape: Shape = CircleShape,
    animationSpec: AnimationSpec<Dp> = tween(durationMillis = 300),
    onClick: () -> Unit,
    isDMYorN: Boolean = false
) {
    val offset by animateDpAsState(
        targetValue = if (isDMYorN) 0.dp else size,
        animationSpec = animationSpec
    )

    Box(modifier = Modifier
        .width(size * 2)
        .height(size)
        .clip(shape = parentShape)
        .clickable { onClick() }
        .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .offset(x = offset)
                .padding(all = padding)
                .clip(shape = toggleShape)
                .background(MaterialTheme.colorScheme.primary)
        ) {}
        Row(
            modifier = Modifier
                .border(
                    border = BorderStroke(
                        width = borderWidth,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    shape = parentShape
                )
        ) {
            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = UIText.StringResource(R.string.theme_icon).likeString(),
                    tint = if (isDMYorN) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.primary
                )
            }
            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = UIText.StringResource(R.string.theme_icon).likeString(),
                    tint = if (isDMYorN) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondaryContainer
                )
            }
        }
    }
}

fun saveIsTurkish(isTurkish: Boolean, context: Context) {
    val sharedPreferences = context.getSharedPreferences("ThemePreferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("isTurkish", isTurkish)
    editor.apply()
}

fun isTurkish(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("ThemePreferences", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("isTurkish", false)
}

@Composable
fun LanguageSwitcher(
    size: Dp = 150.dp,
    iconSize: Dp = size / 3,
    padding: Dp = 10.dp,
    borderWidth: Dp = 1.dp,
    parentShape: Shape = CircleShape,
    toggleShape: Shape = CircleShape,
    animationSpec: AnimationSpec<Dp> = tween(durationMillis = 300),
    onClick: () -> Unit,
    isTurkish: Boolean = false
) {
    val offset by animateDpAsState(
        targetValue = if (isTurkish) 0.dp else size,
        animationSpec = animationSpec
    )

    Box(modifier = Modifier
        .width(size * 2)
        .height(size)
        .clip(shape = parentShape)
        .clickable { onClick() }
        .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .offset(x = offset)
                .padding(all = padding)
                .clip(shape = toggleShape)
                .background(MaterialTheme.colorScheme.primary)
        ) {}
        Row(
            modifier = Modifier
                .border(
                    border = BorderStroke(
                        width = borderWidth,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    shape = parentShape
                )
        ) {
            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = UIText.StringResource(R.string.theme_icon).likeString(),
                    tint = if (isTurkish) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.primary
                )
            }
            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    imageVector = Icons.Default.Language,
                    contentDescription = UIText.StringResource(R.string.theme_icon).likeString(),
                    tint = if (isTurkish) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondaryContainer
                )
            }
        }
    }
}