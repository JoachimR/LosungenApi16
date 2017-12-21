package de.reiss.android.losungen

import android.os.Bundle
import android.widget.FrameLayout
import de.reiss.android.losungen.architecture.AppActivity

class UnderTestAppActivity : AppActivity() {

    lateinit var contentView: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentView = FrameLayout(this)
        contentView.id = R.id.under_test_content_view
        setContentView(contentView)
        this.contentView = contentView
    }

}
