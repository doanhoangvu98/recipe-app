package com.shin.recipeapp.main.addRecipe.vm

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.shin.recipeapp.base.BaseViewModel
import com.shin.recipeapp.localDb.model.Recipe
import com.shin.recipeapp.localDb.repository.RecipeRepository
import timber.log.Timber
import javax.inject.Inject

class AddRecipeViewModel @Inject constructor(
    val context: Context,
    private val recipeRepository: RecipeRepository
) : BaseViewModel() {
    var step = MutableLiveData<String>()
    private var stepList = mutableListOf<String>()
    var stepListLiveData = MutableLiveData<List<String>>()

    var ingredient = MutableLiveData<String>()
    private var ingredientList = mutableListOf<String>()
    var ingredientListLiveData = MutableLiveData<List<String>>()

    var title = MutableLiveData<String>()
    var recipeType = MutableLiveData<String>()
    var imagePath = MutableLiveData<String>()

    var addSuccess = MutableLiveData<Boolean>()
    var updateSuccess = MutableLiveData<Boolean>()
    var deleteSuccess = MutableLiveData<Boolean>()

    var recipeLiveData = MutableLiveData<Recipe>()
    var recipe: Recipe? = null

    fun setDataInit(recipe: Recipe?) {
        if (recipe != null) {
            recipeLiveData.value = recipe
            this.recipe = recipe

            title.value = recipe.title
            recipeType.value = recipe.recipeType
            imagePath.value = recipe.picturePath

            stepList = recipe.steps
            stepListLiveData.value = recipe.steps

            ingredientList = recipe.ingredients
            ingredientListLiveData.value = recipe.ingredients
        }
    }

    fun addStep() {
        step.value?.let {
            stepList.add(it)
            stepListLiveData.value = stepList
        }
        step.value = null
    }

    fun addIngredient() {
        ingredient.value?.let {
            ingredientList.add(it)
            ingredientListLiveData.value = ingredientList
        }
        ingredient.value = null
    }

    fun removeIngredient(ingredient: String) {
        ingredientList.remove(ingredient)
        ingredientListLiveData.value = ingredientList
    }

    fun removeStep(step: String) {
        stepList.remove(step)
        stepListLiveData.value = stepList
    }

    fun updateStep(oldData: String?, newData: String?) {
        var index = stepList.indexOf(oldData)
        if (newData != null) {
            stepList[index] = newData
            stepListLiveData.value = stepList
        }
    }

    fun updateIngredient(oldData: String?, newData: String?) {
        Timber.d("updateingredient $ingredientList - $oldData - $newData")
        var index = ingredientList.indexOf(oldData)
        if (newData != null) {
            ingredientList[index] = newData
            ingredientListLiveData.value = ingredientList
        }
    }

    @SuppressLint("CheckResult")
    fun addRecipe() {
        val recipe = Recipe(
            null,
            title.value!!,
            recipeType.value!!,
            imagePath.value!!,
            stepListLiveData.value!! as ArrayList<String>,
            ingredientListLiveData.value!! as ArrayList<String>
        )
        recipeRepository.insert(recipe).subscribe({
            addSuccess.value = true
        }, {
        })
    }

    @SuppressLint("CheckResult")
    fun updateRecipe() {
        var recipe = Recipe(
            recipeLiveData.value?.id,
            title.value!!,
            recipeType.value!!,
            imagePath.value!!,
            stepListLiveData.value!! as ArrayList<String>,
            ingredientListLiveData.value!! as ArrayList<String>
        )
        recipeRepository.update(recipe).subscribe({
            updateSuccess.value = true
        }, {
        })
    }

    fun deleteRecipe() {
        recipeLiveData.value?.id?.let {
            recipeRepository.delete(it).subscribe({
                deleteSuccess.value = true
            }, {
            })
        }
    }
}