package com.onurerdem.earthquakeapp.presentation

import androidx.compose.foundation.isSystemInDarkTheme
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
    condirmButtonIcon: ImageVector?,
    dismissButtonIcon: ImageVector?
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
                if (condirmButtonIcon != null) {
                    Icon(
                        imageVector = condirmButtonIcon,
                        contentDescription = "Simge",
                        tint = if (isSystemInDarkTheme()) Color.White else Color.Black
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
                        tint = if (isSystemInDarkTheme()) Color.White else Color.Black
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
        containerColor = if (isSystemInDarkTheme()) Color.Black else Color.White
    )
}