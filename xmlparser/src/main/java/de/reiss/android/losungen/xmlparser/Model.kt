package de.reiss.android.losungen.xmlparser

import java.util.*

data class DailyLosungXmlItem(val Datum: Date,
                              val Sonntag: String,
                              val Losungstext: String,
                              val Losungsvers: String,
                              val Lehrtext: String,
                              val Lehrtextvers: String)

data class WeeklyLosungXmlItem(val StartDatum: Date,
                               val EndDatum: Date,
                               val Sonntag: String,
                               val Losungstext: String,
                               val Losungsvers: String)

data class MonthlyLosungXmlItem(val Datum: Date,
                                val Losungstext: String,
                                val Losungsvers: String)

data class YearlyLosungXmlItem(val Datum: Date,
                               val Losungstext: String,
                               val Losungsvers: String)