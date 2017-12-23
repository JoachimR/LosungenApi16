package de.reiss.android.losungen.xmlparser

import java.util.*

sealed class LosungXmlItem

data class DailyLosungXmlItem(val Datum: Date,
                              val Sonntag: String,
                              val Losungstext: String,
                              val Losungsvers: String,
                              val Lehrtext: String,
                              val Lehrtextvers: String) : LosungXmlItem()

data class WeeklyLosungXmlItem(val StartDatum: Date,
                               val EndDatum: Date,
                               val Sonntag: String,
                               val Losungstext: String,
                               val Losungsvers: String) : LosungXmlItem()

data class MonthlyLosungXmlItem(val Datum: Date,
                                val Losungstext: String,
                                val Losungsvers: String) : LosungXmlItem()

data class YearlyLosungXmlItem(val Datum: Date,
                               val Losungstext: String,
                               val Losungsvers: String) : LosungXmlItem()