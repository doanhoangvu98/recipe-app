package com.shin.recipeapp.localDb.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.lang.reflect.Type

@Entity(tableName = "recipe")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "title")
    var title: String?,
    @ColumnInfo(name = "recipeType")
    var recipeType: String?,
    @ColumnInfo(name = "picturePath")
    var picturePath: String? = null,
    @ColumnInfo(name = "steps")
    var steps: ArrayList<String>,
    @ColumnInfo(name = "ingredients")
    var ingredients: ArrayList<String>
) : Serializable {

    companion object {
        const val ALL_TYPES = "All types"
        const val SOUP = "Soup"
        const val DESSERT = "Dessert"
        const val BREAKFAST = "Breakfast"
    }

}

class Converters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromString(value: String?): ArrayList<String> {
            val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        @JvmStatic
        fun fromArrayList(list: ArrayList<String?>?): String {
            val gSon = Gson()
            return gSon.toJson(list)
        }
    }
}