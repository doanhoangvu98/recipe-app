package com.shin.recipeapp.main.home.vm

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.shin.recipeapp.base.BaseViewModel
import com.shin.recipeapp.di.ActivityScope
import com.shin.recipeapp.localDb.room.model.Recipe
import com.shin.recipeapp.localDb.room.repository.RecipeRepository
import com.shin.recipeapp.localDb.sharePrefs.SharePreferenceManager
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
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
    var isNewUser: Boolean = SharePreferenceManager.getInstance(context).isNewUser

    sealed class NavigationEvent {
        object ShowAddRecipe : NavigationEvent()
        class ShowRecipeDetail(val recipe: Recipe) : NavigationEvent()
    }

    private val navigationSubject = PublishSubject.create<NavigationEvent>()
    val navigator: Observable<NavigationEvent> = navigationSubject.hide()

    init {
        initDataFirstTime()
        getAllRecipe()
    }

    fun showAddRecipe() {
        navigationSubject.onNext(NavigationEvent.ShowAddRecipe)
    }

    fun showRecipeDetail(recipe: Recipe) {
        navigationSubject.onNext(NavigationEvent.ShowRecipeDetail(recipe))
    }

    @SuppressLint("CheckResult")
    private fun initDataFirstTime() {
        if (isNewUser) {
            val recipeListSample = mutableListOf<Recipe>()
            recipeListSample.add(
                Recipe(
                    1,
                    "Stove Pot Roast With Mashed Potatoes",
                    Recipe.BREAKFAST,
                    "",
                    arrayListOf(
                        "Season chuck roast with salt and black pepper; sear in a large",
                        "Pour beef broth and water into the skillet with roast"
                    ),
                    arrayListOf(
                        "1 (3 pound) beef chuck roast",
                        "salt and ground black",
                        "4 (10.5 ounce) cans condensed beef broth"
                    )
                )
            )
            recipeListSample.add(
                Recipe(
                    2,
                    "Instant Pot Chicken and Dumplings",
                    Recipe.SOUP,
                    "",
                    arrayListOf(
                        "Pour the olive oil into a multi-functional pressure cooker",
                        "Add chicken broth, chicken thighs, chicken breasts, thyme, marjoram, salt, and pepper."
                    ),
                    arrayListOf("0.5 ablespoon olive oil", "1 cup diced onion", "1 bay leaf")
                )
            )
            recipeListSample.add(
                Recipe(
                    3,
                    "Original Nestle Toll House Chocolate Chip Cookies",
                    Recipe.DESSERT,
                    "",
                    arrayListOf(
                        "Preheat oven to 375 degrees F.",
                        "Combine flour, baking soda and salt in small bowl.",
                        "Bake for 9 to 11 minutes or until golden brown. "
                    ),
                    arrayListOf(
                        "1 teaspoon baking soda",
                        "1 teaspoon salt",
                        "1 cup butter, softened"
                    )
                )
            )
            recipeRepository.insertAll(recipeListSample).subscribe({
                SharePreferenceManager.getInstance(context).setNewUser(false)
            }, {
                it.printStackTrace()
            })
        }
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