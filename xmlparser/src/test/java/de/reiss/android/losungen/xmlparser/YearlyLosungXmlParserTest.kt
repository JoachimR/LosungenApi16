package de.reiss.android.losungen.xmlparser

import de.reiss.android.losungen.xmlparser.testutils.loadXmlString
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.util.*

@Suppress("FunctionName", "IllegalIdentifier")
class YearlyLosungXmlParserTest {

    @Test
    fun `parse yearlylosungen_cz_2018`() {
        check(
                fileName = "yearlylosungen_cz_2018.xml",
                year = 2018
        )
    }

    @Test
    fun `parse yearlylosungen_hu_2018`() {
        check(
                fileName = "yearlylosungen_hu_2018.xml",
                year = 2018
        )
    }

    private fun check(fileName: String, year: Int) {

        val rawString = loadXmlString(fileName, javaClass.classLoader)
        val parsed = YearlyLosungXmlParser.parse(rawString)

        val yearLosung = parsed.firstOrNull {
            Calendar.getInstance().apply { time = it.Datum }.get(Calendar.YEAR) == year
        }
        assertNotNull(yearLosung)
        with(yearLosung!!) {
            assertFalse(Losungstext.isEmpty())
            assertFalse(Losungsvers.isEmpty())
        }
    }

}