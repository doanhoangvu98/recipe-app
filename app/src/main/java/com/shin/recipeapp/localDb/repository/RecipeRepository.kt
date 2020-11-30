package com.shin.recipeapp.localDb.repository

import androidx.lifecycle.LiveData
import com.shin.recipeapp.localDb.model.Recipe
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface RecipeRepository {
    fun getAll(): Flowable<List<Recipe>>
    fun insert(recipe: Recipe): Completable
    fun update(recipe: Recipe): Completable
    fun delete(id: Int): Completable
}