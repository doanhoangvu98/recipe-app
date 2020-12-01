package com.shin.recipeapp.di.module

import com.shin.recipeapp.di.ActivityScope
import com.shin.recipeapp.main.addRecipe.di.AddRecipeModule
import com.shin.recipeapp.main.addRecipe.ui.AddRecipeActivity
import com.shin.recipeapp.main.home.di.MainModule
import com.shin.recipeapp.main.home.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun main(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [AddRecipeModule::class])
    abstract fun addRecipe(): AddRecipeActivity
}