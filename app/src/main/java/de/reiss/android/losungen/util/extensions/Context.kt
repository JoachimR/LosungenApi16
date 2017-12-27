package de.reiss.android.losungen.util.extensions

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

fun Context.isPlayServiceAvailable() = GoogleApiAvailability.getInstance()
        .isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS


fun Context.dipToPx(dip: Float): Int {
    val scale = this.resources.displayMetrics.density
    return (dip * scale + 0.5f).toInt() // 0.5f for rounding
}

fun Context.rawIdFor(name: String) = resources.getIdentifier(name, "raw", packageName)