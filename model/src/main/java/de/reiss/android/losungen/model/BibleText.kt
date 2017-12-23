package de.reiss.android.losungen.model

import android.os.Parcel
import android.os.Parcelable

data class BibleText(val text: String,
                     val source: String) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(text)
        writeString(source)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<BibleText> = object : Parcelable.Creator<BibleText> {
            override fun createFromParcel(source: Parcel): BibleText = BibleText(source)
            override fun newArray(size: Int): Array<BibleText?> = arrayOfNulls(size)
        }
    }
}