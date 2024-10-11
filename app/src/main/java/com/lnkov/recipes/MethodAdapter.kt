package com.lnkov.recipes

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

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val method: String = dataSet[position]
        val binding = viewHolder.binding
        val layout = binding.llIngredientDescription

        binding.tvIngredientDescription.text = method

        if (position == 0) {
            layout.setPadding(
                0,
                layout.context.resources.getDimensionPixelSize(R.dimen.padding_small_4),
                0,
                0
            )
        }

        if (position == dataSet.size - 1) layout.setPadding(
            0,
            0,
            0,
            layout.context.resources.getDimensionPixelSize(R.dimen.padding_small_4)
        )

    }

    override fun getItemCount() = dataSet.size

}