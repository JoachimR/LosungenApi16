package de.reiss.android.losungen.apk

import de.reiss.android.losungen.apk.migrate.ApkMigrate
import de.reiss.android.losungen.apk.prepare.ApkPrepare
import java.util.concurrent.Executor
import javax.inject.Inject

class ApkHandler @Inject constructor(private val executor: Executor,
                                     private val apkPrepare: ApkPrepare,
                                     private val apkMigrate: ApkMigrate) {

    fun handleApk() {
        executor.execute {
            apkPrepare.prepareApkIfNeeded()
            apkMigrate.migrateIfNeeded()
        }
    }

}