package com.shin.recipeapp.util

import com.shin.recipeapp.MyApplication
import com.shin.recipeapp.localDb.model.RecipeType
import com.shin.recipeapp.localDb.model.readRecipeType
import com.shin.recipeapp.localDb.model.skipParse
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream


object ParserDataSource {

    @Throws(XmlPullParserException::class, IOException::class)
    fun readDataXML(): List<String> {
        val parserFactory: XmlPullParserFactory = XmlPullParserFactory.newInstance()
        val parser = parserFactory.newPullParser()
        val am = MyApplication.getAppContext()!!.assets
        val inputStream: InputStream = am.open("recipetypes.xml")
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(inputStream, null)
        parser.nextTag()

        // Read data
        val recipeTypes = mutableListOf<RecipeType>()
        val listType: ArrayList<String>

        parser.require(XmlPullParser.START_TAG, null, "recipeTypes")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            // Starts by looking for the entry tag
            if (parser.name == "recipeType") {
                recipeTypes.add(readRecipeType(parser))
            } else {
                skipParse(parser)
            }
        }
        listType = recipeTypes.map(RecipeType::type) as ArrayList<String>

        return listType
    }
}