package com.shin.recipeapp.main.home.di

import androidx.lifecycle.ViewModel
import com.shin.recipeapp.main.home.vm.MainViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
abstract class MainModule {
    @IntoMap
    @Binds
    @ViewModelKey(MainViewModel::class)
    abstract fun main(viewModel: MainViewModel): ViewModel
}

@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)