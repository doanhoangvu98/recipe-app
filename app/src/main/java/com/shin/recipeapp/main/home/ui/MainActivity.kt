package com.shin.recipeapp.main.home.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.shin.recipeapp.R
import com.shin.recipeapp.base.BaseBindingActivity
import com.shin.recipeapp.databinding.ActivityMainBinding
import com.shin.recipeapp.localDb.room.model.Recipe
import com.shin.recipeapp.main.home.adapter.RecipeAdapter
import com.shin.recipeapp.main.home.navigator.MainNavigator
import com.shin.recipeapp.main.home.vm.MainViewModel
import com.shin.recipeapp.util.ParserDataSource
import javax.inject.Inject
import kotlin.reflect.KClass

class MainActivity : BaseBindingActivity<ActivityMainBinding, MainViewModel>() {

    override val layoutId: Int = R.layout.activity_main

    override val viewModelClass: KClass<MainViewModel> = MainViewModel::class

    @Inject
    lateinit var navigator: MainNavigator

    override fun init(savedInstanceState: Bundle?) {
        viewModel.navigator bindTo navigator::navigate
    }

    override fun initView(savedInstanceState: Bundle?) {
        setUpToolbar()
        setUpSpinner()
        setUpRecyclerview()
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setUpSpinner() {
        var recipeList = mutableListOf<String>()
        // Add all type for default select
        recipeList.add(Recipe.ALL_TYPES)
        // Get recipe type from xml
        var recipeTypeListSource = ParserDataSource.readDataXML()
        recipeTypeListSource.map {
            recipeList.add(it)
        }
        val adapter = ArrayAdapter<String>(
            this, R.layout.spinner_item,
            recipeList
        ).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }
        binding.spnRecipeType.adapter = adapter
        binding.spnRecipeType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.filter(parent?.getItemAtPosition(position).toString())
                }
            }
    }

    private fun setUpRecyclerview() {
        val recipeAdapter = RecipeAdapter()
        binding.rvRecipe.adapter = recipeAdapter
        viewModel.recipeListFilter.observe(this, Observer {
            recipeAdapter.submitList(it)
        })
        // View detail
        recipeAdapter.onItemSelected = {
            it?.let {
                viewModel.showRecipeDetail(it)
            }
        }
    }

}