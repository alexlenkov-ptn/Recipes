package com.lnkov.recipes

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lnkov.recipes.databinding.ItemRecipeIngredientsBinding


class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {
    private var quantity: Double = 1.0

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
        val ingredientQuantity = ingredient.quantity.toDouble() * quantity

        binding.tvIngredientDescription.text = ingredient.description

        binding.tvIngredientQuantityAndUnitOfMeasure.text =
            binding.root.context.getString(
                R.string.text_concat,
                if (ingredientQuantity % 1.0 == 0.0) ingredientQuantity.toInt().toString()
                else ingredientQuantity,
                ingredient.unitOfMeasure
            )
    }

    override fun getItemCount() = dataSet.size

    fun updateIngredients(progress: Int) {
        quantity = progress.toDouble()
    }

}