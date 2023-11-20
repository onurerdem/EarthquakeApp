package com.onurerdem.earthquakeapp.presentation

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun formatDateForTurkishLocale(date: String, sharedPreferencesManager: SharedPreferencesManager): String {
    val currentLocale = LocalContext.current.resources.configuration.locales[0]

    val inputFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())

    val outputFormat = if (currentLocale.language == "tr") {
        SimpleDateFormat(sharedPreferencesManager.getDateFormat() ?: "dd.MM.yyyy", Locale("tr", "TR"))
    } else {
        SimpleDateFormat(sharedPreferencesManager.getDateFormat() ?: "MM.dd.yyyy", Locale.getDefault())
    }

    return outputFormat.format(inputFormat.parse(date)!!)
}

class SharedPreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveDateFormat(dateFormat: String) {
        editor.putString("dateFormat", dateFormat)
        editor.apply()
    }

    fun getDateFormat(): String? {
        return sharedPreferences.getString("dateFormat", null)
    }

    fun saveDMYorN(isDMYorN: Boolean) {
        editor.putBoolean("isDMYorN", isDMYorN)
        editor.apply()
    }

    fun getDMYorN(): Boolean {
        return sharedPreferences.getBoolean("isDMYorN", false)
    }
}