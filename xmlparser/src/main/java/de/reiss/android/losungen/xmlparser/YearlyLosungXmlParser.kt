package de.reiss.android.losungen.xmlparser

import org.jsoup.Jsoup
import org.jsoup.nodes.Element


object YearlyLosungXmlParser {

    private const val FreeXml = "FreeXml"

    private const val JahresLosungen = "JahresLosungen"
    private const val Datum = "Datum"
    private const val Losungstext = "Losungstext"
    private const val Losungsvers = "Losungsvers"

    fun parse(string: String): List<YearlyLosungXmlItem> =
            Jsoup.parse(string).body().getElementsByTag(FreeXml)?.let { content ->
                content
                        .flatMap { it.getElementsByTag(JahresLosungen) }
                        .mapNotNull { element ->
                            parseItem(element)
                        }
            } ?: emptyList()

    private fun parseItem(element: Element) =
            dateFromString(element.getElementsByTag(Datum).text())?.let { d ->
                element.getElementsByTag(Losungstext).text()?.let { lo ->
                    element.getElementsByTag(Losungsvers).text()?.let { lov ->

                        YearlyLosungXmlItem(
                                Datum = d,
                                Losungstext = lo,
                                Losungsvers = lov)
                    }
                }
            }

}