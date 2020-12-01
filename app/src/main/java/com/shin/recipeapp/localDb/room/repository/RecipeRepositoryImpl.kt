package com.shin.recipeapp.localDb.room.repository

import com.shin.recipeapp.localDb.room.dao.RecipeDao
import com.shin.recipeapp.localDb.room.model.Recipe
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(private val recipeDao: RecipeDao) :
    RecipeRepository {
    override fun getAll(): Flowable<List<Recipe>> {
        return recipeDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun insert(recipe: Recipe): Completable {
        return recipeDao.insert(recipe)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun insertAll(list: List<Recipe>): Completable {
        return recipeDao.insertAll(list)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun update(recipe: Recipe): Completable {
        return recipeDao.update(recipe)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun delete(id: Int): Completable {
        return recipeDao.delete(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}