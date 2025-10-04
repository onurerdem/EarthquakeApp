package com.onurerdem.earthquakeapp.presentation.onboarding.views

import android.app.Activity
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.onurerdem.earthquakeapp.R
import com.onurerdem.earthquakeapp.presentation.AlertDialogExample
import com.onurerdem.earthquakeapp.presentation.MainActivity
import com.onurerdem.earthquakeapp.presentation.Screen
import com.onurerdem.earthquakeapp.presentation.UIText
import com.onurerdem.earthquakeapp.presentation.onboarding.OnboardingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    navController: NavHostController,
    context: MainActivity,
    onboardingViewModel: OnboardingViewModel = hiltViewModel()
) {
    val state = onboardingViewModel.state.value

    val images = listOf(
        R.drawable.earthquake,
        R.drawable.earthquake2,
        R.drawable.earthquake3
    )
    val titles = listOf(
        UIText.StringResource(R.string.earthquake_information).likeString(),
        UIText.StringResource(R.string.latest_information).likeString(),
        UIText.StringResource(R.string.notifications_ready).likeString()
    )

    val descriptions = listOf(
        UIText.StringResource(R.string.onboarding_descripton).likeString(),
        UIText.StringResource(R.string.onboarding_descripton2).likeString(),
        UIText.StringResource(R.string.onboarding_descripton3).likeString()
    )
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        images.size
    }

    val scope = rememberCoroutineScope()

    val openAlertDialog = remember { mutableStateOf(false) }

    val activity = (LocalContext.current as? Activity)

    when {
        openAlertDialog.value -> {
            AlertDialogExample(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = {
                    openAlertDialog.value = false
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
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(10f)
        ) { currentPage ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(0.5f))

                Image(
                    painter = painterResource(id = images[currentPage]),
                    contentDescription = titles[currentPage],
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .weight(8f),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.weight(0.5f))
                Text(
                    text = titles[currentPage],
                    textAlign = TextAlign.Center,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
                Spacer(modifier = Modifier.weight(0.5f))
                Text(
                    text = descriptions[currentPage],
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.5f))

        PageIndicator(
            pageCount = images.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier,
            pagerState = pagerState,
            scope = scope
        )

        Spacer(modifier = Modifier.weight(0.5f))

        ButtonsSection(
            pagerState = pagerState,
            navController = navController,
            context = context,
            scope = scope
        )

        if (state.error.isNotBlank() || state.isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (state.error.isNotBlank()) {
            Text(
                text = state.error,
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        Spacer(modifier = Modifier.weight(0.5f))
    }

    BackHandler {
        scope.launch {
            if (pagerState.currentPage == 0) {
                openAlertDialog.value = !openAlertDialog.value
            } else {
                val prevPage = pagerState.currentPage - 1
                pagerState.scrollToPage(prevPage)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ButtonsSection(
    pagerState: PagerState,
    navController: NavHostController,
    context: MainActivity,
    scope: CoroutineScope
) {

    val openAlertDialog = remember { mutableStateOf(false) }

    when {
        openAlertDialog.value -> {
            AlertDialogExample(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = {
                    openAlertDialog.value = false
                    onBoardingIsFinished(context = context)
                    navController.popBackStack()
                    navController.navigate(Screen.RegisterScreen.route)
                },
                dialogTitle = UIText.StringResource(R.string.warning).likeString(),
                dialogText = UIText.StringResource(R.string.are_you_sure_you_want_to_start)
                    .likeString(),
                icon = Icons.Default.Warning,
                iconContentColor = Color.Red,
                confirmButtonText = UIText.StringResource(R.string.yes).likeString(),
                dismissButtonText = UIText.StringResource(R.string.no).likeString(),
                dismissButtonColor = Color.Red,
                confirmButtonIcon = null,
                dismissButtonIcon = null,
                context = context
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = UIText.StringResource(R.string.back).likeString(),
            modifier = Modifier
                .clickable(
                    enabled = pagerState.currentPage != 0,
                    onClick = {
                        scope.launch {
                            pagerState.scrollToPage(pagerState.currentPage - 1)
                        }
                    }
                )
                .alpha(if (pagerState.currentPage == 0) 0f else 1f),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSystemInDarkTheme()) Color.White else Color.Black,
        )

        if (pagerState.currentPage != 2) {
            Text(
                text = UIText.StringResource(R.string.next).likeString(),
                modifier = Modifier
                    .clickable {
                        scope.launch {
                            val nextPage = pagerState.currentPage + 1
                            pagerState.scrollToPage(nextPage)
                        }
                    },
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black
            )
        } else {
            OutlinedButton(
                onClick = {
                    openAlertDialog.value = !openAlertDialog.value
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(),
                colors = ButtonDefaults.buttonColors(
                    if (isSystemInDarkTheme()) Color.Gray else Color.Gray
                )
            ) {
                Text(
                    text = UIText.StringResource(R.string.get_started).likeString(),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier,
    pagerState: PagerState,
    scope: CoroutineScope
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        repeat(pageCount) {
            IndicatorSingleDot(isSelected = it == currentPage) {
                scope.launch {
                    pagerState.scrollToPage(it)
                }
            }
        }
    }
}

@Composable
fun IndicatorSingleDot(isSelected: Boolean, onClick: () -> Unit) {
    val width = animateDpAsState(targetValue = if (isSelected) 35.dp else 15.dp, label = "")
    Box(
        modifier = Modifier
            .padding(2.dp)
            .height(15.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(
                if (isSystemInDarkTheme()) {
                    if (isSelected) Color.White else Color.Gray
                } else {
                    if (isSelected) Color.Black else Color.Gray
                }
            )
            .clickable(onClick = onClick)
    )
}

private fun onBoardingIsFinished(context: MainActivity) {
    val sharedPreferences = context.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("isFinished", true)
    editor.apply()
}