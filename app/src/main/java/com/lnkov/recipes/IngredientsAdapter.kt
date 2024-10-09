package com.lnkov.recipes

import android.util.DisplayMetrics
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.marginTop
import com.lnkov.recipes.databinding.ItemRecipeIngredientsBinding


class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemRecipeIngredientsBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemRecipeIngredientsBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val ingredient: Ingredient = dataSet[position]
        val binding = viewHolder.binding
        val displayMetrics = binding.llIngredient.context.resources.displayMetrics
        val layoutParams = viewHolder.itemView.layoutParams as RecyclerView.LayoutParams

        binding.tvIngredientDescription.text = ingredient.description
        binding.tvIngredientQuantityAndUnitOfMeasure.text =
            "${ingredient.quantity} ${ingredient.unitOfMeasure}"

        if (position == 0) {
            binding.llIngredient.setPadding(
                0,
                dpToPx(displayMetrics, 12),
                0,
                dpToPx(displayMetrics, 8)
            )
        } else {
            binding.llIngredient.setPadding(
                0,
                dpToPx(displayMetrics, 8),
                0,
                dpToPx(displayMetrics, 8)
            )
        }

        if (position == dataSet.size - 1) binding.llIngredient.setPadding(
            0,
            dpToPx(displayMetrics, 8),
            0,
            dpToPx(displayMetrics, 16)
        )
    }

    override fun getItemCount() = dataSet.size

    private fun dpToPx(displayMetrics: DisplayMetrics, dp: Int): Int {
        return (dp * displayMetrics.density).toInt()
    }
}