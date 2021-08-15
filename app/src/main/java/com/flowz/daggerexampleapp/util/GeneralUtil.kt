package com.flowz.daggerexampleapp.util

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.text.NumberFormat
import java.util.*



fun Context?.showToast(message: String?) {
    var mToast: Toast? = null
    mToast?.cancel()
    mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    mToast.show()
}

fun Context?.showLongToast(message: String?) {
    var mToast: Toast? = null
    mToast?.cancel()
    mToast = Toast.makeText(this, message, Toast.LENGTH_LONG)
    mToast.show()
}

fun Int.toDp(ctx: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        ctx.resources.displayMetrics
    ).toInt()
}

fun Context?.navigateToPlayStore() {
    val appPackageName = this!!.packageName

    try {
        this.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$appPackageName")
            )
        )
    } catch (anfe: ActivityNotFoundException) {
        this.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            )
        )
    }
}

fun Context.isGPSEnabled(): Boolean {
    val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return try {
        lm.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    } catch (ex: Exception) {
        ex.printStackTrace()
        false
    }
}

fun Context.checkLocationPermission(): Boolean =
    this.checkCallingOrSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

const val KEY_REQUESTING_LOCATION_UPDATES = "requesting_location_updates"

/**
 * Returns true if requesting location updates, otherwise returns false.
 *
 * @param context The [Context].
 */
//fun Context?.requestingLocationUpdates(): Boolean {
//    return PreferenceManager.getDefaultSharedPreferences(this)
//        .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false)
//}

/**
 * Stores the location updates state in SharedPreferences.
 * @param requestingLocationUpdates The location updates state.
 */
//fun Context?.setRequestingLocationUpdates(requestingLocationUpdates: Boolean) {
//    PreferenceManager.getDefaultSharedPreferences(this)
//        .edit()
//        .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
//        .apply()
//}

fun Context?.isAirplaneModeOn() = if (this == null) {
    false
} else {
    Settings.System.getInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
}

fun Double.formatToCurrency(locale: String): String {
    val loc = Locale.forLanguageTag(locale)
    Log.e("GenUtil", "Country: ${loc.country}\nLanguage: ${loc.language}")
    val formatter: NumberFormat = try {
        NumberFormat.getCurrencyInstance(loc)
    } catch (e: Exception) {
        NumberFormat.getCurrencyInstance(Locale("en", "NG"))
    }
    formatter.maximumFractionDigits = 2
    return formatter.format(this)
}