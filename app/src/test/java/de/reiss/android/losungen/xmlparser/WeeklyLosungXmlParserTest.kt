package de.reiss.android.losungen.xmlparser

import de.reiss.android.losungen.xmlparser.testutils.loadXmlString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import java.util.*

@Suppress("FunctionName", "IllegalIdentifier")
class WeeklyLosungXmlParserTest {

    @Test
    fun `parse weeklylosungen_cz_2019`() {
        check(
            fileName = "weeklylosungen_cz_2019.xml",
            year = 2019
        )
    }

    @Test
    fun `parse weeklylosungen_hu_2019`() {
        check(
            fileName = "weeklylosungen_hu_2019.xml",
            year = 2019
        )
    }


    private fun check(
        fileName: String,
        year: Int
    ) {

        val rawString = loadXmlString(fileName, javaClass.classLoader)
        val parsed = WeeklyLosungXmlParser.parse(rawString)

        assertEquals(parsed.size, parsed.map { it.StartDatum }.toSet().size)
        assertEquals(parsed.size, parsed.map { it.EndDatum }.toSet().size)
        parsed.forEach {
            assertEquals(
                year,
                Calendar.getInstance().apply { time = it.StartDatum }.get(Calendar.YEAR)
            )
            assertEquals(
                year,
                Calendar.getInstance().apply { time = it.EndDatum }.get(Calendar.YEAR)
            )
            assertFalse(it.Sonntag.isEmpty())
            assertFalse(it.Losungstext.isEmpty())
            assertFalse(it.Losungsvers.isEmpty())
        }
    }

}