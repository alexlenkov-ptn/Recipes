package com.lnkov.recipes

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lnkov.recipes.databinding.ItemCategoryBinding


class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {


    class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemCategoryBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val category: Category = dataSet[position]

        viewHolder.binding.tvTitleCardCategory.text = category.title
        viewHolder.binding.tvDescriptionCardCategory.text = category.description

        val drawable = try {
            Drawable.createFromStream(
                viewHolder.itemView.context.assets.open(category.imageUrl),
                null
            )
        } catch (e: Exception) {
            Log.d("!!!", "Image not found: ${category.imageUrl}")
            null
        }

        viewHolder.binding.ivCardCategory.setImageDrawable(drawable)
        viewHolder.binding.ivCardCategory.contentDescription =
            viewHolder.binding.root.context.getString(R.string.category_name) + " " +
                    category.title
    }

    override fun getItemCount() = dataSet.size

}