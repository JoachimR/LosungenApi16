package de.reiss.android.losungen.model

import android.os.Parcel
import android.os.Parcelable

data class Language(val key: String,
                    val name: String,
                    val languageCode: String) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString()!!,
            source.readString()!!,
            source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(key)
        writeString(name)
        writeString(languageCode)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Language> = object : Parcelable.Creator<Language> {
            override fun createFromParcel(source: Parcel): Language = Language(source)
            override fun newArray(size: Int): Array<Language?> = arrayOfNulls(size)
        }
    }
}