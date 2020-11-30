package com.shin.recipeapp.di.module

import android.content.Context
import com.shin.recipeapp.MyApplication
import dagger.Module
import dagger.Provides

@Module
class AppModule {
    @Provides
    fun context(): Context {
        return MyApplication.getAppContext()!!
    }
}