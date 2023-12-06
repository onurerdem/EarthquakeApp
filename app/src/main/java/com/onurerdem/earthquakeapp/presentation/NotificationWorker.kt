package com.onurerdem.earthquakeapp.presentation

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

class NotificationWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    val notificationService = NotificationService(context = context)

    override suspend fun doWork(): Result {
        delay(5000)
        Log.d("NotificationWorker", "Success")
        notificationService.showBasicNotification()

        return Result.success()
    }
}