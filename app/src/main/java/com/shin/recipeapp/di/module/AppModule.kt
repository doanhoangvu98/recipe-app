package com.shin.recipeapp.di.module

import android.content.Context
import com.shin.recipeapp.MyApplication
import com.shin.recipeapp.localDb.RecipeRoomDatabase
import com.shin.recipeapp.localDb.di.RecipeDbModule
import dagger.Module
import dagger.Provides

@Module(includes = [RecipeDbModule::class])
class AppModule {
    @Provides
    fun context(): Context {
        return MyApplication.getAppContext()!!
    }
}