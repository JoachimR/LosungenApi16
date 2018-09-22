package de.reiss.android.losungen.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AppActivity
import kotlinx.android.synthetic.main.privacy_policy_activity.*

class PrivacyPolicyActivity : AppActivity() {

    companion object {

        fun createIntent(context: Context): Intent =
                Intent(context, PrivacyPolicyActivity::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.privacy_policy_activity)
        setSupportActionBar(privacy_policy_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        privacy_policy_web_view.loadUrl("file:///android_asset/privacy_policy.html");
    }

}