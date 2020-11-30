package com.shin.recipeapp.localDb.repository

import androidx.lifecycle.LiveData
import com.shin.recipeapp.localDb.model.Recipe
import io.reactivex.Completable
import io.reactivex.Single

interface RecipeRepository {
    fun getAll(): Single<List<Recipe>>
    fun insert(recipe: Recipe): Completable
}