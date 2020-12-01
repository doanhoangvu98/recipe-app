package com.shin.recipeapp.di.component

import com.shin.recipeapp.MyApplication
import com.shin.recipeapp.di.module.ActivityModule
import com.shin.recipeapp.di.module.AppModule
import com.shin.recipeapp.di.module.DataModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ActivityModule::class,
        AppModule::class,
        DataModule::class,
        AndroidSupportInjectionModule::class
    ]
)
interface AppComponent : AndroidInjector<MyApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: MyApplication): Builder

        fun build(): AndroidInjector<MyApplication>
    }
}