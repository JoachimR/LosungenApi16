package de.reiss.android.losungen.xmlparser

import org.jsoup.Jsoup
import org.jsoup.nodes.Element


object DailyLosungXmlParser : LosungXmlParser<DailyLosungXmlItem> {

    private const val FreeXml = "FreeXml"

    private const val Losungen = "Losungen"
    private const val Datum = "Datum"
    private const val Sonntag = "Sonntag"
    private const val Losungstext = "Losungstext"
    private const val Losungsvers = "Losungsvers"
    private const val Lehrtext = "Lehrtext"
    private const val Lehrtextvers = "Lehrtextvers"

    override fun parse(string: String): List<DailyLosungXmlItem> =
        Jsoup.parse(string).body().getElementsByTag(FreeXml)?.let { content ->
            content
                .flatMap { it.getElementsByTag(Losungen) }
                .mapNotNull { element ->
                    parseItem(element)
                }
        } ?: emptyList()

    private fun parseItem(element: Element) =
        dateFromString(element.getElementsByTag(Datum).text())?.let { d ->
            element.getElementsByTag(Sonntag).text()?.let { s ->
                element.getElementsByTag(Losungstext).text()?.let { lo ->
                    element.getElementsByTag(Losungsvers).text()?.let { lov ->
                        element.getElementsByTag(Lehrtext).text()?.let { lt ->
                            element.getElementsByTag(Lehrtextvers).text()?.let { ltv ->

                                DailyLosungXmlItem(
                                    Datum = d,
                                    Sonntag = s,
                                    Losungstext = lo,
                                    Losungsvers = lov,
                                    Lehrtext = lt,
                                    Lehrtextvers = ltv
                                )
                            }
                        }
                    }
                }
            }
        }

}