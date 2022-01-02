package de.reiss.android.losungen.model

import android.os.Parcel
import android.os.Parcelable

data class BibleTextPair(
    val first: BibleText,
    val second: BibleText
) : Parcelable {

    constructor(
        firstText: String,
        firstSource: String,
        secondText: String,
        secondSource: String
    ) : this(
        BibleText(firstText, firstSource),
        BibleText(secondText, secondSource)
    )

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(BibleText::class.java.classLoader)!!,
        parcel.readParcelable(BibleText::class.java.classLoader)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(first, flags)
        parcel.writeParcelable(second, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BibleTextPair> {
        override fun createFromParcel(parcel: Parcel): BibleTextPair {
            return BibleTextPair(parcel)
        }

        override fun newArray(size: Int): Array<BibleTextPair?> {
            return arrayOfNulls(size)
        }
    }
}