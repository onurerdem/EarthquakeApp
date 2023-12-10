package com.onurerdem.earthquakeapp.presentation

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.onurerdem.earthquakeapp.R
import java.util.Random

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseService : FirebaseMessagingService() {

    val channelId = "firebase_channel"

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_IMMUTABLE)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random().nextInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notification = NotificationCompat.Builder(this,channelId)
            .setContentTitle(p0.data["title"])
            .setContentText(p0.data["message"])
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationId,notification)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "firebaseChannel"

        val channel = NotificationChannel(channelId,channelName, NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Notification Channel Description"
            enableLights(true)
            lightColor = Color.GREEN
        }

        notificationManager.createNotificationChannel(channel)
    }

}