package com.example.notely.services

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.notely.R

class NotifierWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
      val data = inputData
        val title =data.getString("title")
        val content =data.getString("content")

        if (content != null && title != null) {
            buildNotification(title, content)
        }
        return Result.success()
    }

    fun buildNotification(title: String, content: String){
        var builder = NotificationCompat.Builder(applicationContext, "CHANNEL_ID")
            .setSmallIcon(R.drawable.bell)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(applicationContext)) {
            // notificationId is a unique int for each notification that you must define
            notify(12354, builder.build())
        }
    }
}