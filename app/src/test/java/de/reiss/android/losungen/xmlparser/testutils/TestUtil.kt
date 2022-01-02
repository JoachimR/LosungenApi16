package de.reiss.android.losungen.xmlparser.testutils

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*


fun expectedMonth(year: Int, month: Int) =
    Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.MONTH, month)

        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

fun expectedDayOfYear(year: Int, dayOfYear: Int) =
    Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.DAY_OF_YEAR, dayOfYear)

        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

fun loadXmlString(xmlFileName: String, classLoader: ClassLoader): String {
    val reader = BufferedReader(
        InputStreamReader(
            classLoader.getResourceAsStream(xmlFileName)
        )
    )

    val total = StringBuilder()

    var line = reader.readLine()
    while (line != null) {
        total.append(line).append('\n')
        line = reader.readLine()
    }
    return total.toString()
}