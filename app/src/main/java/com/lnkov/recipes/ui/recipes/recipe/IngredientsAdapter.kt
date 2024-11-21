package com.lnkov.recipes.ui.recipes.recipe

import android.annotation.SuppressLint
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lnkov.recipes.model.Ingredient
import com.lnkov.recipes.databinding.ItemRecipeIngredientsBinding


class IngredientsAdapter(var dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {
    private var quantity: Double = 1.0


    fun updateData(dataSet: List<Ingredient>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemRecipeIngredientsBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemRecipeIngredientsBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val ingredient: Ingredient = dataSet[position]
        var ingredientQuantity = ""

        ingredientQuantity = try {
            "${ingredient.quantity.toDouble() * quantity}".removeSuffix(".0")
        } catch (e: NumberFormatException) {
            ingredient.quantity
        }

        viewHolder.binding.apply {
            tvIngredientDescription.text = ingredient.description
            tvIngredientQuantityAndUnitOfMeasure.text =
                "$ingredientQuantity ${ingredient.unitOfMeasure}"
        }

    }

    override fun getItemCount() = dataSet.size

    fun updateIngredients(progress: Int?) {
        Log.d("!!!", "updateIngredients progress: $progress")

        if (progress != null) quantity = progress.toDouble()

        Log.d("!!!", "updateIngredients quantity: $quantity")
    }

}