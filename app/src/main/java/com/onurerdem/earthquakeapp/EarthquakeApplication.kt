package com.onurerdem.earthquakeapp

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.onurerdem.earthquakeapp.presentation.NotificationWorker
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EarthquakeApplication : Application(), Configuration.Provider {
    @SuppressLint("ServiceCast")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        val notificationChannel = NotificationChannel(
            "earthquake_reminder",
            "Earthquake reminder channel",
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationChannel.description = "A notification channel for earthquake reminders"

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

    override val workManagerConfiguration =
        Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(CustomWorkerFactory())
            .build()
}

class CustomWorkerFactory(): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = NotificationWorker(appContext, workerParameters)

}