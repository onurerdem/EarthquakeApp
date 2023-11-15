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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
        "Deprem Bilgileri",
        "En Güncel Bilgiler",
        "Bildirimler Hazır"
    )

    val descriptions = listOf(
        "Hoş geldiniz! Uygulamamızla Türkiye'nin dört bir yanında meydana gelen deprem bilgilerine erişebilirsiniz.",
        "Uygulamamızla, deprem verilerini detaylarıyla birlikte inceleyebilir, depremleri harita üzerinde görebilirsiniz.",
        "Uygulamamızla bildirimler almayı tercih ederek yeni depremlerin verilerini inceleyebilirisiniz."
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
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.wrapContentSize()
        ) { currentPage ->
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight(if (images[currentPage] == images[1]) 0.0465f else 0.04f)
                )
                Image(
                    painter = painterResource(id = images[currentPage]),
                    contentDescription = titles[currentPage],
                    modifier = Modifier
                        .fillMaxHeight(if (images[currentPage] == images[1]) 0.4831f else 0.5f)
                        .fillMaxWidth(if (images[currentPage] == images[0]) 0.87f else 0.8f),
                    alignment = Alignment.TopCenter,
                    contentScale = ContentScale.FillBounds
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight(if (images[currentPage] == images[1]) 0.0765f else 0.05f)
                )
                Text(
                    text = titles[currentPage],
                    textAlign = TextAlign.Center,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxHeight(0.175f),
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight(0.1f)
                )
                Text(
                    text = descriptions[currentPage],
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxHeight(0.31f),
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight(0.2f)
                )
            }
        }

        PageIndicator(
            pageCount = images.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier,
            pagerState = pagerState,
            scope = scope
        )

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
    }

    ButtonsSection(
        pagerState = pagerState,
        navController = navController,
        context = context,
        scope = scope
    )

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
                dialogTitle = "Uyarı",
                dialogText = "Başlamak istediğinize emin misiniz?",
                icon = Icons.Default.Warning,
                iconContentColor = Color.Red,
                confirmButtonText = "Evet",
                dismissButtonText = "Hayır",
                dismissButtonColor = Color.Red,
                condirmButtonIcon = null,
                dismissButtonIcon = null
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp)
    ) {
        if (pagerState.currentPage != 0) {
            Text(
                text = "Back",
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .clickable {
                        scope.launch {
                            val prevPage = pagerState.currentPage - 1
                            pagerState.scrollToPage(prevPage)
                        }
                    },
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black
            )
        }
        if (pagerState.currentPage != 2) {
            Text(
                text = "Next",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
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
                    .fillMaxWidth(0.7f)
                    .align(Alignment.BottomEnd),
                colors = ButtonDefaults.buttonColors(
                    if (isSystemInDarkTheme()) Color.Gray else Color.Gray
                )
            ) {
                Text(
                    text = "Get Started",
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