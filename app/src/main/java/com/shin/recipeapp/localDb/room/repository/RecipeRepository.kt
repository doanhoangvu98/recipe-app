package com.shin.recipeapp.localDb.room.repository

import com.shin.recipeapp.localDb.room.model.Recipe
import io.reactivex.Completable
import io.reactivex.Flowable

interface RecipeRepository {
    fun getAll(): Flowable<List<Recipe>>
    fun insert(recipe: Recipe): Completable
    fun insertAll(list: List<Recipe>): Completable
    fun update(recipe: Recipe): Completable
    fun delete(id: Int): Completable
}