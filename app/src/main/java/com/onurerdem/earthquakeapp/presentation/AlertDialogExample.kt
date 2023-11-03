package com.onurerdem.earthquakeapp.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp

@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    iconContentColor: Color
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Simge")
        },
        title = {
            Text(
                text = dialogTitle,
                fontSize = 30.sp
            )
        },
        text = {
            Text(
                text = dialogText,
                fontSize = 20.sp
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
                Text(
                    text = "Evet",
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
                Text(
                    text = "Hayır",
                    color = Color.Red,
                    fontSize = 20.sp
                )
            }
        },
        iconContentColor = iconContentColor,
        containerColor = if (isSystemInDarkTheme()) Color.Black else Color.White
    )
}