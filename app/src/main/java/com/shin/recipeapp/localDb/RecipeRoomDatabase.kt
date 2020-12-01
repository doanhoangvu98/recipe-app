package com.shin.recipeapp.localDb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shin.recipeapp.localDb.room.dao.RecipeDao
import com.shin.recipeapp.localDb.room.model.Converters
import com.shin.recipeapp.localDb.room.model.Recipe

@Database(entities = [Recipe::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)

abstract class RecipeRoomDatabase : RoomDatabase() {
    abstract val recipeDao: RecipeDao
}
