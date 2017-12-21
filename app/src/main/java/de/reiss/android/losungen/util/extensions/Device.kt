package de.reiss.android.losungen.util.extensions

import android.content.Context
import android.content.pm.PackageManager

fun Context.isPackageInstalled(packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}