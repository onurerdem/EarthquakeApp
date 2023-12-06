package com.onurerdem.earthquakeapp.presentation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UIText {
    data class DynamicString(val value: String): UIText()

    object Empty : UIText()

    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ): UIText()

    @Composable
    fun likeString(): String {
        return when(this) {
            is Empty -> ""
            is DynamicString -> value
            is StringResource -> stringResource(resId, *args)
        }
    }
}