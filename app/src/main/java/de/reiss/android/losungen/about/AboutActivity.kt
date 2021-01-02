package de.reiss.android.losungen.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import de.reiss.android.losungen.App
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AppActivity
import de.reiss.android.losungen.preferences.AppPreferences
import de.reiss.android.losungen.util.extensions.onClick
import kotlinx.android.synthetic.main.about_activity.*

class AboutActivity : AppActivity() {

    companion object {

        fun createIntent(context: Context): Intent =
                Intent(context, AboutActivity::class.java)

    }

    private val appPreferences: AppPreferences by lazy {
        App.component.appPreferences
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_activity)
        setSupportActionBar(about_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        about_app_version.text = appVersion()

        about_root.setBackgroundColor(appPreferences.backgroundColor())

        val fontColor = appPreferences.fontColor()
        about_app_name.setTextColor(fontColor)
        about_app_version.setTextColor(fontColor)
        about_info_prefix.setTextColor(fontColor)
        about_info_general.setTextColor(fontColor)
        about_source_code_prefix.setTextColor(fontColor)
        about_source_code_url.setLinkTextColor(fontColor)
        about_developer_prefix.setTextColor(fontColor)
        about_developer_name.setTextColor(fontColor)
        about_developer_mail.setLinkTextColor(fontColor)
        about_developer_github.setLinkTextColor(fontColor)
        hesla_info.setTextColor(fontColor)
        hesla_info_url.setLinkTextColor(fontColor)

        about_privacy_policy_button.onClick {
            startActivity(PrivacyPolicyActivity.createIntent(this))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_about, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                R.id.about_share -> {
                    val text = getString(R.string.share_app, getString(R.string.play_store_url))
                    startActivity(Intent.createChooser(Intent()
                            .setAction(Intent.ACTION_SEND)
                            .putExtra(Intent.EXTRA_TEXT, text)
                            .setType("text/plain"),
                            getString(R.string.share_app_title)))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    private fun appVersion(): String {
        val version = packageManager.getPackageInfo(packageName, 0).versionName
        return getString(R.string.app_version, version)
    }

}