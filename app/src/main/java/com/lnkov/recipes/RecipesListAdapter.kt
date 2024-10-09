package com.lnkov.recipes

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lnkov.recipes.databinding.ItemRecipeBinding

class RecipesListAdapter(private val dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemRecipeBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val recipe: Recipe = dataSet[position]
        val binding = viewHolder.binding

        val drawable = try {
            Drawable.createFromStream(
                viewHolder.itemView.context.assets.open(recipe.imageUrl),
                null
            )
        } catch (e: Exception) {
            Log.d("!!!", "Image not found: ${recipe.imageUrl}")
            null
        }

        binding.tvTitleCardRecipe.text = recipe.title
        binding.ivCardRecipe.setImageDrawable(drawable)
        binding.ivCardRecipe.contentDescription =
            binding.root.context.getString(
                R.string.text_content_description_card_recipe,
                recipe.title
            )
        binding.cvRecipe.setOnClickListener() {
            itemClickListener?.onItemClick(recipe.id)
        }


    }

    override fun getItemCount() = dataSet.size

}