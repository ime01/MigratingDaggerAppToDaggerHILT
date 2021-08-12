package com.flowz.daggerexampleapp.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.flowz.daggerexampleapp.MainActivity
import com.flowz.daggerexampleapp.R
import kotlin.random.Random

class ShowNotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
       try {
           for (i in 0..20000){
               Log.i("WORK IS BEING DONE", "The Number is $i")
           }
           val tellMeTime = "The time is ${System.currentTimeMillis()}"
           showNotification()

           return Result.success()

       }catch (e:Exception){
           e.printStackTrace()
           return Result.failure()
       }
    }

    private fun showNotification() {
        val randomId = (0..5000).random()

        val clickIntent = Intent(applicationContext, MainActivity::class.java).apply {
            action = CLICK_ACTION
//            data = deeplink
        }

        val openPendingIntent = PendingIntent.getActivity(applicationContext, NOTIFICATION_REQUEST_CODE, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT )

        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setContentIntent(openPendingIntent)
            .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
            .setContentTitle("WORK MANAGER NOTIFICATION")
            .setContentText("Reminder to make a call on V2 app and document it")
//            .setLargeIcon(R.drawable.ic_baseline_person_24)
//            .setStyle(
//                NotificationCompat.BigPictureStyle()
//                    .bigPicture(R.drawable.ic_baseline_person_24)
//                    .bigLargeIcon(null)
//            )
            .setAutoCancel(true)
            .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200))

        val myNotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notification", importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern =  longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

            notification.setChannelId(NOTIFICATION_CHANNEL_ID)
            myNotificationManager.createNotificationChannel(notificationChannel)
            myNotificationManager.notify(NOTIFICATION_ID, notification.build() )


        }

    }

    companion object{
        const val CLICK_ACTION = "clickNotification"
        const val NOTIFICATION_REQUEST_CODE = 40
        const val NOTIFICATION_CHANNEL = "My Channel"
        const val NOTIFICATION_CHANNEL_ID = "1000"
        const val NOTIFICATION_ID = 35
    }


}