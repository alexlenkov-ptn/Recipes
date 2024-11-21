package com.lnkov.recipes.ui.recipes.recipe_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lnkov.recipes.R
import com.lnkov.recipes.data.Constants
import com.lnkov.recipes.model.Recipe
import com.lnkov.recipes.databinding.ItemRecipeBinding

class RecipesListAdapter(private var dataSet: List<Recipe?>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    fun updateData(dataSet: List<Recipe?>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

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

        val recipe: Recipe? = dataSet[position]
        val binding = viewHolder.binding

        binding.apply {
            tvTitleCardRecipe.text = recipe?.title

            Glide.with(viewHolder.itemView.context)
                .load("${Constants.BASE_IMAGE_URL}${recipe?.imageUrl}")
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(ivCardRecipe)

            ivCardRecipe.contentDescription =
                root.context.getString(R.string.text_content_description_card_recipe, recipe?.title)

            cvRecipe.setOnClickListener() {
                if (recipe != null) itemClickListener?.onItemClick(recipe.id)
            }
        }
    }

    override fun getItemCount() = dataSet.size

}