package de.reiss.android.losungen.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class DailyLosung(val language: String,
                       val date: Date,
                       val holiday: String,
                       val content: LosungContent) : Comparable<DailyLosung>, Parcelable {

    override fun compareTo(other: DailyLosung): Int = this.date.compareTo(other.date)

    constructor(source: Parcel) : this(
            source.readString(),
            source.readSerializable() as Date,
            source.readString(),
            source.readParcelable<LosungContent>(LosungContent::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(language)
        writeSerializable(date)
        writeString(holiday)
        writeParcelable(content, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DailyLosung> = object : Parcelable.Creator<DailyLosung> {
            override fun createFromParcel(source: Parcel): DailyLosung = DailyLosung(source)
            override fun newArray(size: Int): Array<DailyLosung?> = arrayOfNulls(size)
        }
    }

}