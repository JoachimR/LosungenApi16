package de.reiss.android.losungen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.reiss.android.losungen.language.LanguageActivity
import de.reiss.android.losungen.main.MainActivity


class SplashScreenActivity : AppCompatActivity() {

    companion object {

        fun createIntent(context: Context): Intent =
                Intent(context, SplashScreenActivity::class.java)

    }

    private val appPreferences by lazy {
        App.component.appPreferences
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goToRightStart()
        supportFinishAfterTransition()
    }

    private fun goToRightStart() {
        startActivity(
                when {
                    appPreferences.chosenLanguage == null -> {
                        LanguageActivity.createIntent(this)
                    }
                    else -> {
                        MainActivity.createIntent(this)
                    }
                })

    }

}
