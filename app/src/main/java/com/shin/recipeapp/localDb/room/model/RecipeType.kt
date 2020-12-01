package com.shin.recipeapp.localDb.room.model

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

data class RecipeType(
    var type: String? = null
)

@Throws(XmlPullParserException::class, IOException::class)
fun readRecipeType(parser: XmlPullParser): RecipeType {
    parser.require(XmlPullParser.START_TAG, null, "recipeType")
    var type: String? = null
    while (parser.next() != XmlPullParser.END_TAG) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            continue
        }
        when (parser.name) {
            "type" -> type = readType(parser)
            else -> skipParse(parser)
        }
    }
    return RecipeType(type)
}

// Processes title tags in the feed.
@Throws(IOException::class, XmlPullParserException::class)
private fun readType(parser: XmlPullParser): String {
    parser.require(XmlPullParser.START_TAG, null, "type")
    val type = readText(parser)
    parser.require(XmlPullParser.END_TAG, null, "type")
    return type
}

// For the tags title and summary, extracts their text values.
@Throws(IOException::class, XmlPullParserException::class)
private fun readText(parser: XmlPullParser): String {
    var result = ""
    if (parser.next() == XmlPullParser.TEXT) {
        result = parser.text
        parser.nextTag()
    }
    return result
}

@Throws(XmlPullParserException::class, IOException::class)
fun skipParse(parser: XmlPullParser) {
    if (parser.eventType != XmlPullParser.START_TAG) {
        throw IllegalStateException()
    }
    var depth = 1
    while (depth != 0) {
        when (parser.next()) {
            XmlPullParser.END_TAG -> depth--
            XmlPullParser.START_TAG -> depth++
        }
    }
}