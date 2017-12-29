package de.reiss.android.losungen.main.daily

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import de.reiss.android.losungen.R


class LosungFragmentNoCards : LosungFragment(R.layout.losung_fragment_no_cards) {

    companion object {

        private const val KEY_POSITION = "KEY_POSITION"

        fun createInstance(position: Int) = LosungFragmentNoCards().apply {
            arguments = Bundle().apply {
                putInt(KEY_POSITION, position)
            }
        }

    }

    private lateinit var divider: View

    override fun findViews(layout: View) {
        super.findViews(layout)
        divider = layout.findViewById<View>(R.id.losung_note_divider)
    }

    override fun setFontColor(fontColor: Int) {
        super.setFontColor(fontColor)
        divider.setBackgroundColor(fontColor)
        (note_edit as ImageView).setColorFilter(fontColor, android.graphics.PorterDuff.Mode.SRC_IN);
    }

}