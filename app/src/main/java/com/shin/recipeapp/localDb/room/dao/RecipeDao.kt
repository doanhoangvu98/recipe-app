package com.shin.recipeapp.localDb.room.dao

import androidx.room.*
import com.shin.recipeapp.localDb.room.model.Recipe
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface RecipeDao {
    @Insert
    fun insert(recipe: Recipe): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Recipe>): Completable

    @Query("DELETE from recipe WHERE id = :id")
    fun delete(id: Int): Completable

    @Update
    fun update(vararg recipe: Recipe): Completable

    @Query("SELECT * from recipe ORDER BY id ASC")
    fun getAll(): Flowable<List<Recipe>>
}