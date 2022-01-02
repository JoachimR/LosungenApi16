package de.reiss.android.losungen.xmlparser


import android.annotation.SuppressLint
import de.reiss.android.losungen.logger.logWarn
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
private fun simpleDateFormat() = SimpleDateFormat("yyyy-MM-dd") // use function for thread safety

fun dateFromString(dateString: String?): Date? {
    return try {
        simpleDateFormat().parse(dateString)
    } catch (e: Exception) {
        logWarn(e) { "Error when trying to parse date from string $dateString" }
        null
    }
}

fun flattedDateFromString(dateString: String?): Date? {
    return try {
        Calendar.getInstance().apply {
            time = simpleDateFormat().parse(dateString)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
    } catch (e: ParseException) {
        null
    }
}