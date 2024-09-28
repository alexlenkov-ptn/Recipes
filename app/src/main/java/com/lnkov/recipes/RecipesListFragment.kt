package com.lnkov.recipes

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.lnkov.recipes.databinding.FragmentRecipesListBinding

class RecipesListFragment : Fragment() {
    private val binding by lazy { FragmentRecipesListBinding.inflate(layoutInflater) }

    private lateinit var recipesListAdapter: RecipesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val categoryId = it.getInt(Constants.ARG_CATEGORY_ID)
            val categoryName = it.getString(Constants.ARG_CATEGORY_NAME).toString()
            val categoryImageUrl = it.getString(Constants.ARG_CATEGORY_IMAGE_URL).toString()

            Log.d("!!!", "Id: $categoryId")
            Log.d("!!!", "Text: $categoryName")
            Log.d("!!!", "Image: $categoryImageUrl")

            val drawable = try {
                Drawable.createFromStream(
                    context?.assets?.open(categoryImageUrl),
                    null
                )
            } catch (e: Exception) {
                Log.d("!!!", "Image not found: $categoryImageUrl")
                null
            }

            binding.ivBcgRecipeList.setImageDrawable(drawable)
            binding.tvBcgRecipeList.text = categoryName

            initRecycler(categoryId)
        }

    }

    private fun initRecycler(id : Int) {
        recipesListAdapter = RecipesListAdapter(STUB.getRecipesByCategoryId(id))
        binding.rvRecipes.adapter = recipesListAdapter

        recipesListAdapter.setOnItemClickListener(
            object : RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipesId(recipeId)
                }
            }
        )
    }

    private fun openRecipeByRecipesId(recipeId: Int) {
        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}