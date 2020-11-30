package com.shin.recipeapp.localDb.repository

import com.shin.recipeapp.localDb.dao.RecipeDao
import com.shin.recipeapp.localDb.model.Recipe
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(private val recipeDao: RecipeDao) :
    RecipeRepository {
    override fun getAll(): Single<List<Recipe>> {
        return recipeDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun insert(recipe: Recipe): Completable {
        return recipeDao.insert(recipe)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}