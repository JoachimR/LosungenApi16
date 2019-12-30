package de.reiss.android.losungen.main.daily

import android.os.Bundle
import androidx.cardview.widget.CardView
import de.reiss.android.losungen.R
import de.reiss.android.losungen.model.DailyLosung


class LosungFragmentWithCards : LosungFragment(R.layout.losung_fragment_with_cards) {

    companion object {

        private const val KEY_POSITION = "KEY_POSITION"

        fun createInstance(position: Int) = LosungFragmentWithCards().apply {
            arguments = Bundle().apply {
                putInt(KEY_POSITION, position)
            }
        }

    }

    override fun updateStyle(losung: DailyLosung?) {
        super.updateStyle(viewModel.losung())
        (text1root as CardView).setCardBackgroundColor(
                appPreferences.cardBackgroundColor())
        (text2root as CardView).setCardBackgroundColor(
                appPreferences.cardBackgroundColor())
        (note_root as CardView).setCardBackgroundColor(
                appPreferences.cardBackgroundColor())
    }

}