package de.reiss.android.losungen.xmlparser

import org.jsoup.Jsoup
import org.jsoup.nodes.Element


object WeeklyLosungXmlParser : LosungXmlParser<WeeklyLosungXmlItem> {

    private const val FreeXml = "FreeXml"

    private const val WochenLosungen = "WochenLosungen"
    private const val StartDatum = "StartDatum"
    private const val EndDatum = "EndDatum"
    private const val Sonntag = "Sonntag"
    private const val Losungstext = "Losungstext"
    private const val Losungsvers = "Losungsvers"

    override fun parse(string: String): List<WeeklyLosungXmlItem> =
        Jsoup.parse(string).body().getElementsByTag(FreeXml)?.let { content ->
            content
                .flatMap { it.getElementsByTag(WochenLosungen) }
                .mapNotNull { element ->
                    parseItem(element)
                }
        } ?: emptyList()

    private fun parseItem(element: Element) =
        dateFromString(element.getElementsByTag(StartDatum).text())?.let { start ->
            dateFromString(element.getElementsByTag(EndDatum).text())?.let { end ->
                element.getElementsByTag(Sonntag).text()?.let { s ->
                    element.getElementsByTag(Losungstext).text()?.let { lo ->
                        element.getElementsByTag(Losungsvers).text()?.let { lov ->

                            WeeklyLosungXmlItem(
                                StartDatum = start,
                                EndDatum = end,
                                Sonntag = s,
                                Losungstext = lo,
                                Losungsvers = lov
                            )
                        }
                    }
                }
            }

        }

}