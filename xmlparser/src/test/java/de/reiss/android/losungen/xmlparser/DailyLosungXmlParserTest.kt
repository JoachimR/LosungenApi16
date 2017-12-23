package de.reiss.android.losungen.xmlparser

import de.reiss.android.losungen.xmlparser.testutils.expectedDayOfYear
import de.reiss.android.losungen.xmlparser.testutils.loadXmlString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

@Suppress("FunctionName", "IllegalIdentifier")
class DailyLosungXmlParserTest {

    @Test
    fun `parse dailylosungen_cz_2018`() {
        check(
                fileName = "dailylosungen_cz_2018.xml",
                year = 2018
        )
    }

    @Test
    fun `parse dailylosungen_hu_2018`() {
        check(
                fileName = "dailylosungen_hu_2018.xml",
                year = 2018
        )
    }

    private fun check(fileName: String,
                      year: Int) {

        val rawString = loadXmlString(fileName, javaClass.classLoader)
        val parsed = DailyLosungXmlParser.parse(rawString)

        assertEquals(365, parsed.size)
        assertEquals(365, parsed.map { it.Datum }.toSet().size)

        for ((index, xmlItem) in parsed.withIndex()) {

            checkItem(index, xmlItem, year)
        }
    }

    private fun checkItem(index: Int, dailyLosungXmlItem: DailyLosungXmlItem, expectedYear: Int) {
        with(dailyLosungXmlItem) {
            assertEquals(expectedDayOfYear(year = expectedYear, dayOfYear = index + 1), Datum)
            assertFalse(Losungstext.isEmpty())
            assertFalse(Losungsvers.isEmpty())
            assertFalse(Lehrtext.isEmpty())
            assertFalse(Lehrtextvers.isEmpty())
        }
    }

}