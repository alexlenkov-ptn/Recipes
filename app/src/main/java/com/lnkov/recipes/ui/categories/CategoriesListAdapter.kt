package com.lnkov.recipes.ui.categories

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lnkov.recipes.model.Category
import com.lnkov.recipes.R
import com.lnkov.recipes.data.Constants
import com.lnkov.recipes.databinding.ItemCategoryBinding


class CategoriesListAdapter(private var dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    fun updateData(dataSet: List<Category>) {
        this.dataSet = dataSet
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemCategoryBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val category: Category = dataSet[position]
        val binding = viewHolder.binding

        viewHolder.binding.apply {
            tvTitleCardCategory.text = category.title
            tvDescriptionCardCategory.text = category.description

            Glide.with(viewHolder.itemView.context)
                .load("${Constants.BASE_IMAGE_URL}${category.imageUrl}")
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(ivCardCategory)

            ivCardCategory.contentDescription =
                binding.root.context.getString(
                    R.string.text_content_description_card_category,
                    category.title
                )

            cvCategory.setOnClickListener() {
                itemClickListener?.onItemClick(category.id)
            }
        }
    }

    override fun getItemCount() = dataSet.size

}