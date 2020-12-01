package com.shin.recipeapp.localDb.room.di

import android.content.Context
import androidx.room.Room
import com.shin.recipeapp.localDb.RecipeRoomDatabase
import com.shin.recipeapp.localDb.room.dao.RecipeDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RecipeDbModule {
    @Provides
    @Singleton
    fun provideRecipeDb(context: Context): RecipeRoomDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            RecipeRoomDatabase::class.java,
            "recipeDb"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideRecipeDao(recipeRoomDatabase: RecipeRoomDatabase): RecipeDao {
        return recipeRoomDatabase.recipeDao
    }
}