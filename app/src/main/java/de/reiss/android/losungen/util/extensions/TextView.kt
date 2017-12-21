package de.reiss.android.losungen.util.extensions

import android.view.View
import android.widget.TextView

fun TextView.textOrHide(charSequence: CharSequence) {
    if (charSequence.isEmpty()) {
        visibility = View.GONE
    } else {
        visibility = View.VISIBLE
        text = charSequence
    }
}