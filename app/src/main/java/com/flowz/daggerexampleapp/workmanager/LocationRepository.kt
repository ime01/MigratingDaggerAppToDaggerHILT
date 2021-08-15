package com.flowz.daggerexampleapp.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.media.AudioAttributes
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.flowz.daggerexampleapp.MainActivity
import com.flowz.daggerexampleapp.R
import com.flowz.daggerexampleapp.util.checkLocationPermission
import com.flowz.daggerexampleapp.util.isGPSEnabled
import com.google.android.gms.location.LocationServices

class LocationRepository(context : Context) {

    val context:Context = context
    private var  userLocation = ""

     fun fetcMyLocation() {

        if (context.isGPSEnabled() && context.checkLocationPermission()){

            LocationServices.getFusedLocationProviderClient(context)
                .lastLocation
                .addOnSuccessListener {location: Location?->
                    if (location != null ){
                        Log.e("USERLOCATION", "User Location is ${location}")
                        val lat = location.latitude
                        val long = location.longitude
                        userLocation = "$lat : $long "
                        showNotification(userLocation)
                    }
                }
                .addOnFailureListener {
                    Log.e("FAILED", "Failed error message ${it}")
                }

        }
        //        else{
//            showNotification("Lekki Lagos")
//        }
    }

     fun showNotification(currentLocation: String) {
        val randomId = (0..5000).random()

        val clickIntent = Intent( context, MainActivity::class.java).apply {
            action = CLICK_ACTION
//            data = deeplink
        }

        val openPendingIntent = PendingIntent.getActivity( context, NOTIFICATION_REQUEST_CODE, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT )

        val notification = NotificationCompat.Builder( context, NOTIFICATION_CHANNEL)
            .setContentIntent(openPendingIntent)
            .setSmallIcon(R.drawable.ic_baseline_location_on_24)
            .setContentTitle("LOCATION UPDATE")
            .setContentText("Your Current Location is ${currentLocation}")
//            .setLargeIcon(R.drawable.ic_baseline_person_24)
//            .setStyle(
//                NotificationCompat.BigPictureStyle()
//                    .bigPicture(R.drawable.ic_baseline_person_24)
//                    .bigLargeIcon(null)
//            )
            .setAutoCancel(true)
            .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200))

        val myNotificationManager =  context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
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