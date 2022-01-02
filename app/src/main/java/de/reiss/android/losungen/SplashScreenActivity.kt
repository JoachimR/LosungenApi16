package de.reiss.android.losungen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.reiss.android.losungen.language.LanguageActivity
import de.reiss.android.losungen.main.MainActivity
import de.reiss.android.losungen.main.MainActivityNoToolbar


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
                    if (appPreferences.showToolbar()) MainActivity.createIntent(this)
                    else MainActivityNoToolbar.createIntent(this)
                }
            }
        )

    }

}
