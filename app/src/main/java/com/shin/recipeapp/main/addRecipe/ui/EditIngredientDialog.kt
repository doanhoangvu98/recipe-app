package com.shin.recipeapp.main.addRecipe.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import com.shin.recipeapp.databinding.DialogEditIngredientBinding
import com.shin.recipeapp.main.addRecipe.vm.AddRecipeViewModel
import com.shin.recipeapp.util.InputFilterSpace

class EditIngredientDialog(
    context: Context,
    val text: String,
    val vm: AddRecipeViewModel
) : Dialog(context) {
    var onSubmit: ((String) -> Unit)? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = DialogEditIngredientBinding.inflate(LayoutInflater.from(context))
        binding.vm = vm
        vm.ingredientEdit.value = text
        binding.submitButton.setOnClickListener {
            onSubmit?.invoke(vm.ingredientEdit.value.toString())
        }
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
    }
}