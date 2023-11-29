package com.onurerdem.earthquakeapp.presentation.register.views

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onurerdem.earthquakeapp.R
import com.onurerdem.earthquakeapp.presentation.HeadingTextComponent
import com.onurerdem.earthquakeapp.presentation.MainActivity
import com.onurerdem.earthquakeapp.presentation.UIText
import com.onurerdem.earthquakeapp.presentation.isDarkThemeMode

@Composable
fun TermsAndConditionsScreen(context: Context) {
    val privicyPolicy = UIText.StringResource(R.string.privicy_policy).likeString()

    Surface(modifier = Modifier
        .fillMaxSize()
        .background(color = if (isDarkThemeMode(context = context)) Color.DarkGray else Color.White)
        .padding(16.dp)) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = if (isDarkThemeMode(context = context)) Color.DarkGray else Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            HeadingTextComponent(value = UIText.StringResource(R.string.terms_of_use).likeString(), context = context)

            Text(
                text = privicyPolicy,
                modifier = Modifier
                    .fillMaxSize(),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Normal
                ), color = if (isDarkThemeMode(context = context)) Color.White else Color.Black,
                textAlign = TextAlign.Start
            )
        }

    }
}

@Preview
@Composable
fun TermsAndConditionsScreenPreview(){
    TermsAndConditionsScreen(context = MainActivity())
}