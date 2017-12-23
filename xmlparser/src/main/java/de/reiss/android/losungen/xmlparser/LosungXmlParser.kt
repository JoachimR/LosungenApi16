package de.reiss.android.losungen.xmlparser

interface LosungXmlParser<out XmlItem : LosungXmlItem> {

    fun parse(string: String): List<XmlItem>

}