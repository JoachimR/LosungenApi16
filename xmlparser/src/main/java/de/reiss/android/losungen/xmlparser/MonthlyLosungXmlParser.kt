package de.reiss.android.losungen.xmlparser

import org.jsoup.Jsoup
import org.jsoup.nodes.Element


object MonthlyLosungXmlParser : LosungXmlParser<MonthlyLosungXmlItem> {

    private const val FreeXml = "FreeXml"

    private const val MonatsLosungen = "MonatsLosungen"
    private const val Datum = "Datum"
    private const val Losungstext = "Losungstext"
    private const val Losungsvers = "Losungsvers"

    override fun parse(string: String): List<MonthlyLosungXmlItem> =
            Jsoup.parse(string).body().getElementsByTag(FreeXml)?.let { content ->
                content
                        .flatMap { it.getElementsByTag(MonatsLosungen) }
                        .mapNotNull { element ->
                            parseItem(element)
                        }
            } ?: emptyList()

    private fun parseItem(element: Element) =
            dateFromString(element.getElementsByTag(Datum).text())?.let { d ->
                element.getElementsByTag(Losungstext).text()?.let { lo ->
                    element.getElementsByTag(Losungsvers).text()?.let { lov ->

                        MonthlyLosungXmlItem(
                                Datum = d,
                                Losungstext = lo,
                                Losungsvers = lov)
                    }
                }
            }

}