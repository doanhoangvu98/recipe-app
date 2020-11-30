package com.shin.recipeapp.main.addRecipe.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shin.recipeapp.databinding.ItemStepInteractBinding

class StepAdapter : ListAdapter<String, StepAdapter.ViewHolder>(NewEventDiffCallback) {

    var onItemDelete: ((String?) -> Unit)? = null
    var onItemUpdate: ((String?, String?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemStepInteractBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.step = getItem(position)
        holder.binding.apply {
            btnDeleteItem.setOnClickListener {
                onItemDelete?.invoke(holder.binding.step)
            }
            edtContent.addTextChangedListener (object: TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    onItemUpdate?.invoke(holder.binding.step, p0.toString())
                }

            })
        }
    }

    override fun submitList(list: List<String>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    class ViewHolder(val binding: ItemStepInteractBinding) : RecyclerView.ViewHolder(binding.root)

    object NewEventDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

}