package com.onurerdem.earthquakeapp.presentation

import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    iconContentColor: Color,
    confirmButtonText: String,
    dismissButtonText: String,
    dismissButtonColor: Color,
    confirmButtonIcon: ImageVector?,
    dismissButtonIcon: ImageVector?,
    context: Context
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Simge")
        },
        title = {
            Text(
                text = dialogTitle,
                fontSize = 30.sp,
                color = if (isDarkThemeMode(context = context)) Color.White else Color.Black
            )
        },
        text = {
            Text(
                text = dialogText,
                fontSize = 20.sp,
                color = if (isDarkThemeMode(context = context)) Color.White else Color.Black
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                if (confirmButtonIcon != null) {
                    Icon(
                        imageVector = confirmButtonIcon,
                        contentDescription = "Simge",
                        tint = if (isDarkThemeMode(context = context)) Color.White else Color.Black
                    )
                }

                Spacer(modifier = Modifier.widthIn(8.dp))

                Text(
                    text = confirmButtonText,
                    color = Color.Blue,
                    fontSize = 20.sp
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                if (dismissButtonIcon != null) {
                    Icon(
                        imageVector = dismissButtonIcon,
                        contentDescription = "Simge",
                        tint = if (isDarkThemeMode(context = context)) Color.White else Color.Black
                    )
                }

                Spacer(modifier = Modifier.widthIn(8.dp))

                Text(
                    text = dismissButtonText,
                    color = dismissButtonColor,
                    fontSize = 20.sp
                )
            }
        },
        iconContentColor = iconContentColor,
        containerColor = if (isDarkThemeMode(context = context)) Color.Black else Color.White
    )
}