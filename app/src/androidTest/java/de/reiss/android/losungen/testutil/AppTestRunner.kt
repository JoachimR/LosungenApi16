package de.reiss.android.losungen.testutil

import android.app.Application
import android.content.Context
import android.support.test.runner.AndroidJUnitRunner
import de.reiss.android.losungen.TestApp


class AppTestRunner : AndroidJUnitRunner() {

    @Throws(InstantiationException::class, IllegalAccessException::class, ClassNotFoundException::class)
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application =
            super.newApplication(cl, TestApp::class.java.canonicalName, context)

}
