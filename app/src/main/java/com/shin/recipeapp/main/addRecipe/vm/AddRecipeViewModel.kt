package com.shin.recipeapp.main.addRecipe.vm

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.shin.recipeapp.base.BaseViewModel
import com.shin.recipeapp.localDb.model.Recipe
import com.shin.recipeapp.localDb.repository.RecipeRepository
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
}