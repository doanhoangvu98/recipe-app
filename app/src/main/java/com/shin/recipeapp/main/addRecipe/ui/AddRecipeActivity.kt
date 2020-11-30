package com.shin.recipeapp.main.addRecipe.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import com.shin.recipeapp.R
import com.shin.recipeapp.base.BaseBindingActivity
import com.shin.recipeapp.databinding.ActivityAddRecipeBinding
import com.shin.recipeapp.main.addRecipe.adapter.IngredientAdapter
import com.shin.recipeapp.main.addRecipe.adapter.StepAdapter
import com.shin.recipeapp.main.addRecipe.vm.AddRecipeViewModel
import com.shin.recipeapp.util.ImageUtil
import com.shin.recipeapp.util.ParserDataSource
import timber.log.Timber
import kotlin.reflect.KClass

class AddRecipeActivity : BaseBindingActivity<ActivityAddRecipeBinding, AddRecipeViewModel>() {

    companion object {
        const val IMAGE_PICK_CODE = 1000
        const val PERMISSION_CODE = 1001
        @JvmStatic
        fun newInstance(context: Context): Intent {
            var intent = Intent(context, AddRecipeActivity::class.java)
            var args = Bundle()
            intent.putExtras(args)
            return intent
        }
    }

    override val layoutId: Int = R.layout.activity_add_recipe

    override val viewModelClass: KClass<AddRecipeViewModel> = AddRecipeViewModel::class

    override fun init(savedInstanceState: Bundle?) {
    }

    override fun initView(savedInstanceState: Bundle?) {
        setUpToolbar()
        setUpButton()
        setUpSpinner()
        setUpRecyclerView()
        // Add success
        viewModel.addSuccess.observe(this, Observer {
            if (it) {
                Toast.makeText(this, "Add successfully", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
        })
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setUpSpinner() {
        val adapter = ArrayAdapter<String>(this, R.layout.spinner_item, ParserDataSource.readDataXML())
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
                    viewModel.recipeType.value = parent?.getItemAtPosition(position).toString()
                }
            }
    }

    private fun setUpButton() {
        // Add step
        binding.btnAddStep.setOnClickListener {
            if (!viewModel.step.value.isNullOrEmpty()) {
                viewModel.addStep()
            } else {
                Toast.makeText(this, "Enter step", Toast.LENGTH_SHORT).show()
            }
        }
        // Add ingredient
        binding.btnAddIngredient.setOnClickListener {
            if (!viewModel.ingredient.value.isNullOrEmpty()) {
                viewModel.addIngredient()
            } else {
                Toast.makeText(this, "Enter ingredient", Toast.LENGTH_SHORT).show()
            }
        }
        // Add recipe
        binding.btnAddRecipe.setOnClickListener {
            validate { viewModel.addRecipe() }
        }
        // Add image
        binding.btnAddImage.setOnClickListener {
            // check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    //permission already granted
                    pickImageFromGallery()
                }
            } else {
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
        }
    }

    private fun setUpRecyclerView() {
        // Step recyclerview
        val stepAdapter = StepAdapter()
        binding.rvSteps.adapter = stepAdapter
        viewModel.stepListLiveData.observe(this, Observer {
            stepAdapter.submitList(it)
        })
        // Ingredient recyclerview
        val ingredientAdapter = IngredientAdapter()
        binding.rvIngredients.adapter = ingredientAdapter
        viewModel.ingredientListLiveData.observe(this, Observer {
            ingredientAdapter.submitList(it)
        })
        // Delete ingredient item
        ingredientAdapter.onItemDelete = {
            it?.let {
                viewModel.removeIngredient(it)
            }
        }
        // Delete step item
        stepAdapter.onItemDelete = {
            it?.let {
                viewModel.removeStep(it)
            }
        }
    }

    private fun validate(callback: (() -> Unit)? = null) {
        if (viewModel.title.value.isNullOrEmpty()) {
            Timber.d("checknull title")
        }
        if (viewModel.recipeType.value.isNullOrEmpty()) {
            Timber.d("checknull type")
        }
        if (viewModel.imagePath.value.isNullOrEmpty()) {
            Timber.d("checknull path")
        }
        if (viewModel.stepListLiveData.value.isNullOrEmpty()) {
            Timber.d("checknull step")
        }
        if (viewModel.ingredientListLiveData.value.isNullOrEmpty()) {
            Timber.d("checknull ingredi")
        }
        if (!(viewModel.title.value.isNullOrEmpty() || viewModel.recipeType.value.isNullOrEmpty()
                    || viewModel.imagePath.value.isNullOrEmpty()
                    || viewModel.stepListLiveData.value.isNullOrEmpty()
                    || viewModel.ingredientListLiveData.value.isNullOrEmpty()
                    )
        ) {
            callback?.invoke()
        }
    }

    private fun pickImageFromGallery() {
        // Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    // handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            binding.imgRecipe.setImageURI(data?.data)
            data?.let {
                val uri = it.data
                viewModel.imagePath.value = ImageUtil.getPathImg(this, uri)
            }
        }
    }

}