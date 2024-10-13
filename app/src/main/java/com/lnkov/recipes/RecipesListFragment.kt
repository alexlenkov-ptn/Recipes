package com.lnkov.recipes

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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

            val drawable: Drawable? = try {
                Drawable.createFromStream(
                    context?.assets?.open(categoryImageUrl),
                    null
                )
            } catch (e: Exception) {
                Log.d("!!!", "Image not found: $categoryImageUrl")
                null
            }


            binding.apply {
                ivBcgRecipeList.setImageDrawable(drawable)
                ivBcgRecipeList.contentDescription = "Image: $categoryImageUrl"
                tvBcgRecipeList.text = categoryName
            }



            initRecycler(categoryId)
        }

    }

    private fun initRecycler(categoryId: Int) {
        recipesListAdapter = RecipesListAdapter(STUB.getRecipesByCategoryId(categoryId))
        binding.rvRecipes.adapter = recipesListAdapter

        recipesListAdapter.setOnItemClickListener(
            object : RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipesId(categoryId, recipeId)
                }
            }
        )
    }

    private fun openRecipeByRecipesId(categoryId: Int, recipeId: Int) {
        var bundle: Bundle? = null

        val recipe = STUB.getRecipeById(categoryId, recipeId)

        bundle = bundleOf(
            Constants.ARG_RECIPE to recipe
        )

        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}