package de.reiss.android.losungen.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import de.reiss.android.losungen.App
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AppActivity
import de.reiss.android.losungen.databinding.AboutActivityBinding
import de.reiss.android.losungen.preferences.AppPreferences

class AboutActivity : AppActivity() {

    companion object {

        fun createIntent(context: Context): Intent =
            Intent(context, AboutActivity::class.java)

    }

    private lateinit var binding: AboutActivityBinding


    private val appPreferences: AppPreferences by lazy {
        App.component.appPreferences
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AboutActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.aboutToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.aboutAppVersion.text = appVersion()


        binding.aboutRoot.setBackgroundColor(appPreferences.backgroundColor())


        val fontColor = appPreferences.fontColor()
        binding.aboutAppName.setTextColor(fontColor)
        binding.aboutAppVersion.setTextColor(fontColor)
        binding.aboutInfoPrefix.setTextColor(fontColor)
        binding.aboutInfoGeneral.setTextColor(fontColor)
        binding.aboutSourceCodePrefix.setTextColor(fontColor)
        binding.aboutSourceCodeUrl.setLinkTextColor(fontColor)
        binding.aboutDeveloperPrefix.setTextColor(fontColor)
        binding.aboutDeveloperName.setTextColor(fontColor)
        binding.aboutDeveloperMail.setLinkTextColor(fontColor)
        binding.aboutDeveloperGithub.setLinkTextColor(fontColor)
        binding.heslaInfo.setTextColor(fontColor)
        binding.heslaInfoUrl.setLinkTextColor(fontColor)

        binding.aboutPrivacyPolicyButton.setOnClickListener {
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
                startActivity(
                    Intent.createChooser(
                        Intent()
                            .setAction(Intent.ACTION_SEND)
                            .putExtra(Intent.EXTRA_TEXT, text)
                            .setType("text/plain"),
                        getString(R.string.share_app_title)
                    )
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun appVersion(): String {
        val version = packageManager.getPackageInfo(packageName, 0).versionName
        return getString(R.string.app_version, version)
    }

}