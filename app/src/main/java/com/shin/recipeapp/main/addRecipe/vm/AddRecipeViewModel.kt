package com.shin.recipeapp.main.addRecipe.vm

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.shin.recipeapp.R
import com.shin.recipeapp.base.BaseViewModel
import com.shin.recipeapp.localDb.room.model.Recipe
import com.shin.recipeapp.localDb.room.repository.RecipeRepository
import timber.log.Timber
import javax.inject.Inject

class AddRecipeViewModel @Inject constructor(
    val context: Context,
    private val recipeRepository: RecipeRepository
) : BaseViewModel() {
    var step = MutableLiveData<String>()
    var ingredient = MutableLiveData<String>()

    var stepEdit = MutableLiveData<String>()
    var ingredientEdit = MutableLiveData<String>()

    private var stepList = mutableListOf<String>()
    var stepListLiveData = MutableLiveData<List<String>>()

    private var ingredientList = mutableListOf<String>()
    var ingredientListLiveData = MutableLiveData<List<String>>()

    var title = MutableLiveData<String>()
    var recipeType = MutableLiveData<String>()
    var imagePath = MutableLiveData<String>()

    var addSuccess = MutableLiveData<Boolean>()
    var updateSuccess = MutableLiveData<Boolean>()
    var deleteSuccess = MutableLiveData<Boolean>()

    private var recipeLiveData = MutableLiveData<Recipe>()
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
        } else {
            imagePath.value = ""
        }
    }

    fun addStep() {
        step.value?.let {
            if (it.trim().isNullOrEmpty()) {
                Toast.makeText(
                    context,
                    context.getString(R.string.validate_enter_step),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                stepList.add(it.trim())
                stepListLiveData.value = stepList
            }
        }
        step.value = null
    }

    fun addIngredient() {
        ingredient.value?.let {
            if (it.trim().isNullOrEmpty()) {
                Toast.makeText(
                    context,
                    context.getString(R.string.validate_enter_ingredient),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                ingredientList.add(it.trim())
                ingredientListLiveData.value = ingredientList
            }
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
        val index = stepList.indexOf(oldData)
        if (!newData?.trim().isNullOrEmpty()) {
            stepList[index] = newData?.trim().toString()
            stepListLiveData.value = stepList
        }
        stepEdit.value = null
    }

    fun updateIngredient(oldData: String?, newData: String?) {
        Timber.d("UPDATE INGREDIENT ITEM $oldData - $newData")
        val index = ingredientList.indexOf(oldData)
        if (!newData?.trim().isNullOrEmpty()) {
            ingredientList[index] = newData?.trim().toString()
            ingredientListLiveData.value = ingredientList
        }
        ingredientEdit.value = null
    }

    private fun validate(callback: (() -> Unit)? = null) {
        if (title.value?.trim().isNullOrEmpty()) {
            Toast.makeText(
                context,
                context.getString(R.string.validate_enter_title),
                Toast.LENGTH_SHORT
            ).show()
        }
        if (stepListLiveData.value.isNullOrEmpty()) {
            Toast.makeText(
                context,
                context.getString(R.string.validate_enter_step),
                Toast.LENGTH_SHORT
            ).show()
        }
        if (ingredientListLiveData.value.isNullOrEmpty()) {
            Toast.makeText(
                context,
                context.getString(R.string.validate_enter_ingredient),
                Toast.LENGTH_SHORT
            ).show()
        }
        if (!(title.value.isNullOrEmpty()
                    || stepListLiveData.value.isNullOrEmpty()
                    || ingredientListLiveData.value.isNullOrEmpty()
                    )
        ) {
            callback?.invoke()
        }
    }

    @SuppressLint("CheckResult")
    fun addRecipe() {
        validate {
            val recipe = Recipe(
                null,
                title.value!!.trim(),
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

    @SuppressLint("CheckResult")
    fun updateRecipe() {
        validate {
            val recipe = Recipe(
                recipeLiveData.value?.id,
                title.value!!.trim(),
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