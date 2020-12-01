package com.shin.recipeapp.main.addRecipe.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import com.shin.recipeapp.R
import com.shin.recipeapp.base.BaseBindingActivity
import com.shin.recipeapp.databinding.ActivityAddRecipeBinding
import com.shin.recipeapp.localDb.room.model.Recipe
import com.shin.recipeapp.main.addRecipe.adapter.IngredientAdapter
import com.shin.recipeapp.main.addRecipe.adapter.StepAdapter
import com.shin.recipeapp.main.addRecipe.vm.AddRecipeViewModel
import com.shin.recipeapp.util.ImageUtil
import com.shin.recipeapp.util.InputFilterSpace
import com.shin.recipeapp.util.ParserDataSource
import timber.log.Timber
import kotlin.reflect.KClass

class AddRecipeActivity : BaseBindingActivity<ActivityAddRecipeBinding, AddRecipeViewModel>() {

    companion object {
        const val IMAGE_PICK_CODE = 1000
        const val PERMISSION_CODE = 1001
        const val RECIPE = "recipe"

        @JvmStatic
        fun newInstance(context: Context, recipe: Recipe?): Intent {
            val intent = Intent(context, AddRecipeActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable(RECIPE, recipe)
            intent.putExtras(bundle)
            return intent
        }
    }

    override val layoutId: Int = R.layout.activity_add_recipe

    override val viewModelClass: KClass<AddRecipeViewModel> = AddRecipeViewModel::class

    var recipe: Recipe? = null

    private lateinit var ingredientDialog: EditIngredientDialog
    private lateinit var stepDialog: EditStepDialog

    override fun init(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        recipe = bundle?.getSerializable(RECIPE) as Recipe?
        viewModel.setDataInit(recipe)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setUpToolbar()
        setUpButton()
        setUpSpinner()
        setUpRecyclerView()
        // Add success
        viewModel.addSuccess.observe(this, Observer {
            if (it) {
                Toast.makeText(
                    this,
                    getString(R.string.notification_add_successful),
                    Toast.LENGTH_SHORT
                ).show()
                onBackPressed()
            }
        })
        // Update success
        viewModel.updateSuccess.observe(this, Observer {
            if (it) {
                Toast.makeText(
                    this,
                    getString(R.string.notification_update_successfully),
                    Toast.LENGTH_SHORT
                ).show()
                onBackPressed()
            }
        })
        // Delete success
        viewModel.deleteSuccess.observe(this, Observer {
            if (it) {
                Toast.makeText(
                    this,
                    getString(R.string.notification_remove_successfully),
                    Toast.LENGTH_SHORT
                ).show()
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
        val recipeTypeList = ParserDataSource.readDataXML()
        val adapter = ArrayAdapter(
            this, R.layout.spinner_item,
            recipeTypeList
        ).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item)
        }
        binding.spnRecipeType.adapter = adapter
        binding.spnRecipeType.setSelection(recipeTypeList.indexOf(viewModel.recipeType.value))
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
                Toast.makeText(this, getString(R.string.validate_enter_step), Toast.LENGTH_SHORT)
                    .show()
            }
        }
        // Add ingredient
        binding.btnAddIngredient.setOnClickListener {
            if (!viewModel.ingredient.value.isNullOrEmpty()) {
                viewModel.addIngredient()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.validate_enter_ingredient),
                    Toast.LENGTH_SHORT
                ).show()
            }
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
            Timber.d("LIST STEP: $it")
            stepAdapter.submitList(it)
        })
        // Ingredient recyclerview
        val ingredientAdapter = IngredientAdapter()
        binding.rvIngredients.adapter = ingredientAdapter
        viewModel.ingredientListLiveData.observe(this, Observer {
            Timber.d("LIST INGREDIENT: $it")
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
        // On item selected
        ingredientAdapter.onItemSelected = {
            it?.let {
                showEditIngredientDialog(it)
            }
        }
        stepAdapter.onItemSelected = {
            it?.let {
                showEditStepDialog(it)
            }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        if (recipe != null) inflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> {
                viewModel.deleteRecipe()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showEditIngredientDialog(text: String) {
        ingredientDialog = EditIngredientDialog(this, text, viewModel)
        ingredientDialog.show()
        ingredientDialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        ingredientDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        ingredientDialog.onSubmit = {
            if (it.trim().isNullOrEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.validate_enter_ingredient),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.updateIngredient(text, it)
                ingredientDialog.dismiss()
            }
        }
    }

    private fun showEditStepDialog(text: String) {
        stepDialog = EditStepDialog(this, text, viewModel)
        stepDialog.show()
        stepDialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        stepDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        stepDialog.onSubmit = {
            if (it.trim().isNullOrEmpty()) {
                Toast.makeText(this, getString(R.string.validate_enter_step), Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.updateStep(text, it)
                stepDialog.dismiss()
            }
        }
    }

}
