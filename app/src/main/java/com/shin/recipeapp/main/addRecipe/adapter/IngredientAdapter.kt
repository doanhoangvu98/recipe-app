package com.shin.recipeapp.main.addRecipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shin.recipeapp.databinding.ItemIngredientInteractBinding

class IngredientAdapter : ListAdapter<String, IngredientAdapter.ViewHolder>(NewEventDiffCallback) {

    var onItemDelete: ((String?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemIngredientInteractBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.ingredient = getItem(position)
        holder.binding.apply {
            btnDeleteItem.setOnClickListener {
                onItemDelete?.invoke(holder.binding.ingredient)
            }
        }
    }

    override fun submitList(list: List<String>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    class ViewHolder(val binding: ItemIngredientInteractBinding) :
        RecyclerView.ViewHolder(binding.root)

    object NewEventDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

}