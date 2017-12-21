package de.reiss.android.losungen.model

import android.os.Parcel
import android.os.Parcelable

data class LosungContent(val text1: String,
                         val source1: String,
                         val text2: String,
                         val source2: String) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(text1)
        writeString(source1)
        writeString(text2)
        writeString(source2)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<LosungContent> = object : Parcelable.Creator<LosungContent> {
            override fun createFromParcel(source: Parcel): LosungContent = LosungContent(source)
            override fun newArray(size: Int): Array<LosungContent?> = arrayOfNulls(size)
        }
    }
}