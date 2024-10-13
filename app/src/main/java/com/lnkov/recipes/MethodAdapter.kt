package com.lnkov.recipes

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lnkov.recipes.databinding.ItemRecipeMethodBinding


class MethodAdapter(private val dataSet: List<String>) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemRecipeMethodBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemRecipeMethodBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val method: String = dataSet[position]
        val binding = viewHolder.binding

        binding.tvIngredientDescription.text = "${position + 1}. $method"
    }

    override fun getItemCount() = dataSet.size

}