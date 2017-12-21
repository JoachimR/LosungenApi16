package de.reiss.android.losungen.architecture

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.arch.lifecycle.ViewModel
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.support.annotation.LayoutRes
import android.support.v4.app.ActivityCompat.checkSelfPermission

abstract class AppFragmentWithSdCard<T : ViewModel>(@LayoutRes private val fragmentLayout: Int)
    : AppFragment<T>(fragmentLayout) {

    companion object {

        private val REQUEST_WRITE_EXTERNAL_STORAGE = 11

    }

    fun requestSdCardPermission() {
        if (hasExternalStoragePermission()) {
            onSdCardPermissionGranted()
        } else {
            requestPermissions(arrayOf(WRITE_EXTERNAL_STORAGE),
                    REQUEST_WRITE_EXTERNAL_STORAGE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE
                && permissions[0] == WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PERMISSION_GRANTED) {
                onSdCardPermissionGranted()
            } else {
                onSdCardPermissionDenied()
            }
        }
    }

    private fun hasExternalStoragePermission(): Boolean {

        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || context?.let {
            checkSelfPermission(it, WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED
        } ?: false)
    }

    abstract fun onSdCardPermissionGranted()

    abstract fun onSdCardPermissionDenied()

}