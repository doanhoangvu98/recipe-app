package com.shin.recipeapp.main.home.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shin.recipeapp.R
import com.shin.recipeapp.base.BaseBindingActivity
import com.shin.recipeapp.databinding.ActivityMainBinding
import com.shin.recipeapp.main.home.vm.MainViewModel
import kotlin.reflect.KClass

class MainActivity : BaseBindingActivity<ActivityMainBinding, MainViewModel>() {

    override val layoutId: Int = R.layout.activity_main

    override val viewModelClass: KClass<MainViewModel> = MainViewModel::class

    override fun init(savedInstanceState: Bundle?) {
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

}