package com.onurerdem.earthquakeapp.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun formatDateForTurkishLocale(date: String): String {
    val currentLocale = LocalContext.current.resources.configuration.locales[0]

    val inputFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())

    val outputFormat = if (currentLocale.language == "tr") {
        SimpleDateFormat("dd.MM.yyyy", Locale("tr", "TR"))
    } else {
        inputFormat
    }

    return outputFormat.format(inputFormat.parse(date)!!)
}