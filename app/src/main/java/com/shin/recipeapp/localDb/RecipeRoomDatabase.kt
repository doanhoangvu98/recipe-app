package com.shin.recipeapp.localDb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shin.recipeapp.localDb.dao.RecipeDao
import com.shin.recipeapp.localDb.model.Converters
import com.shin.recipeapp.localDb.model.Recipe

@Database(entities = [Recipe::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)

abstract class RecipeRoomDatabase : RoomDatabase() {
    abstract val recipeDao: RecipeDao
}
