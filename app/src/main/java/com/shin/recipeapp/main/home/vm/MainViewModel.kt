package com.shin.recipeapp.main.home.vm

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.shin.recipeapp.base.BaseViewModel
import com.shin.recipeapp.di.ActivityScope
import com.shin.recipeapp.localDb.model.Recipe
import com.shin.recipeapp.localDb.repository.RecipeRepository
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

@ActivityScope
class MainViewModel @Inject constructor(
    val context: Context,
    private val recipeRepository: RecipeRepository
) : BaseViewModel() {

    private var recipeListLiveData = MutableLiveData<List<Recipe>>()
    var recipeListFilter = MutableLiveData<List<Recipe>>()
    private var recipeTypeFilter: String? = null
    var isLoading = MutableLiveData<Boolean>()

    sealed class NavigationEvent {
        object ShowAddRecipe : NavigationEvent()
        class ShowRecipeDetail(val recipe: Recipe) : NavigationEvent()
    }

    private val navigationSubject = PublishSubject.create<NavigationEvent>()
    val navigator: Observable<NavigationEvent> = navigationSubject.hide()

    init {
        getAllRecipe()
    }

    fun showAddRecipe() {
        navigationSubject.onNext(NavigationEvent.ShowAddRecipe)
    }

    fun showRecipeDetail(recipe: Recipe) {
        navigationSubject.onNext(NavigationEvent.ShowRecipeDetail(recipe))
    }

    @SuppressLint("CheckResult")
    fun getAllRecipe() {
        isLoading.value = true
        recipeRepository.getAll()
            .subscribe({
                recipeListLiveData.value = it
                if (recipeTypeFilter == null) {
                    recipeTypeFilter = Recipe.ALL_TYPES
                }
                filter(recipeTypeFilter!!)
            }, {
                isLoading.value = false
                it.printStackTrace()
            })
    }

    fun filter(type: String) {
        recipeTypeFilter = type
        if (type == Recipe.ALL_TYPES) {
            recipeListFilter.value = recipeListLiveData.value
        } else {
            recipeListFilter.value = recipeListLiveData.value?.filter {
                it.recipeType == type
            }
        }
        isLoading.value = false
    }
}