package de.reiss.android.losungen.xmlparser

import de.reiss.android.losungen.xmlparser.testutils.expectedMonth
import de.reiss.android.losungen.xmlparser.testutils.loadXmlString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

@Suppress("FunctionName", "IllegalIdentifier")
class MonthlyLosungXmlParserTest {

    @Test
    fun `parse monthlylosungen_cz_2019`() {
        check(
                fileName = "monthlylosungen_cz_2019.xml",
                year = 2019
        )
    }

    @Test
    fun `parse monthlylosungen_hu_2019`() {
        check(
                fileName = "monthlylosungen_hu_2019.xml",
                year = 2019
        )
    }

    private fun check(fileName: String,
                      year: Int) {

        val rawString = loadXmlString(fileName, javaClass.classLoader)
        val parsed = MonthlyLosungXmlParser.parse(rawString)

        assertEquals(12, parsed.size)
        assertEquals(12, parsed.map { it.Datum }.toSet().size)

        for ((index, xmlItem) in parsed.withIndex()) {
            with(xmlItem) {
                assertEquals(expectedMonth(year = year, month = index), Datum)
                assertFalse(Losungstext.isEmpty())
                assertFalse(Losungsvers.isEmpty())
            }
        }
    }

}