package com.shin.recipeapp.di.module

import com.shin.recipeapp.localDb.room.repository.RecipeRepository
import com.shin.recipeapp.localDb.room.repository.RecipeRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class DataModule {
    @Binds
    abstract fun recipeRepository(repository: RecipeRepositoryImpl): RecipeRepository
}