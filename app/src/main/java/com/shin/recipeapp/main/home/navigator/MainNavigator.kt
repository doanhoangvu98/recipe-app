package com.shin.recipeapp.main.home.navigator

import com.shin.recipeapp.main.addRecipe.ui.AddRecipeActivity
import com.shin.recipeapp.main.home.ui.MainActivity
import com.shin.recipeapp.main.home.vm.MainViewModel
import javax.inject.Inject

interface MainNavigatorInterface {
    fun navigate(event: MainViewModel.NavigationEvent)
}

class MainNavigator @Inject constructor(private val activity: MainActivity) :
    MainNavigatorInterface {
    override fun navigate(event: MainViewModel.NavigationEvent) {
        when (event) {
            is MainViewModel.NavigationEvent.ShowAddRecipe -> {
                activity.startActivity(AddRecipeActivity.newInstance(activity))
            }
        }
    }

}