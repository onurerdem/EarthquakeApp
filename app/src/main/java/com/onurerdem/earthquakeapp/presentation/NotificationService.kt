package com.onurerdem.earthquakeapp.presentation

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.annotation.DrawableRes
import com.onurerdem.earthquakeapp.R
import kotlin.random.Random

class NotificationService(
    private val context: Context
) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showBasicNotification() {
        val notificationBuilder = NotificationCompat.Builder(context, "earthquake_reminder")
            .setContentTitle("Earthquake Reminder")
            .setContentText("Have you made any preparations for an earthquake?")
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = notificationBuilder.build()
        notification.contentIntent = pendingIntent
        notificationManager.notify(Random.nextInt(), notification)
    }

    private fun Context.bitmapFromResource(@DrawableRes resId: Int) =
        ContextCompat.getDrawable(this, resId)
}
