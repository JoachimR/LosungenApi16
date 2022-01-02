package de.reiss.android.losungen.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import de.reiss.android.losungen.architecture.AppActivity
import de.reiss.android.losungen.databinding.PrivacyPolicyActivityBinding

class PrivacyPolicyActivity : AppActivity() {

    companion object {
        fun createIntent(context: Context): Intent =
            Intent(context, PrivacyPolicyActivity::class.java)
    }

    private lateinit var binding: PrivacyPolicyActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PrivacyPolicyActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.privacyPolicyToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.privacyPolicyWebView.loadUrl("file:///android_asset/privacy_policy.html")
    }
}