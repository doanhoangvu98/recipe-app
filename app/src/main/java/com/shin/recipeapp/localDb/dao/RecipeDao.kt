package com.shin.recipeapp.localDb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.shin.recipeapp.localDb.model.Recipe
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface RecipeDao {
    @Insert
    fun insert(recipe: Recipe): Completable

    @Query("DELETE from recipe WHERE id = :id")
    fun delete(id: Int): Completable

    @Update
    fun update(vararg recipe: Recipe): Completable

    @Query("SELECT * from recipe ORDER BY id ASC")
    fun getAll(): Flowable<List<Recipe>>
}