package com.shin.recipeapp.main.addRecipe.di

import androidx.lifecycle.ViewModel
import com.shin.recipeapp.main.addRecipe.vm.AddRecipeViewModel
import com.shin.recipeapp.main.home.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AddRecipeModule {
    @IntoMap
    @Binds
    @ViewModelKey(AddRecipeViewModel::class)
    abstract fun main(viewModel: AddRecipeViewModel): ViewModel
}
