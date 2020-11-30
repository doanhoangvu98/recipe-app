package com.shin.recipeapp.di.module

import com.shin.recipeapp.di.ActivityScope
import com.shin.recipeapp.main.home.di.MainModule
import com.shin.recipeapp.main.home.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun main(): MainActivity
}