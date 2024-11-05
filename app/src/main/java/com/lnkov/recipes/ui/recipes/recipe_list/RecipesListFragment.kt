package com.lnkov.recipes.ui.recipes.recipe_list

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
import androidx.fragment.app.viewModels
import com.lnkov.recipes.R
import com.lnkov.recipes.data.Constants
import com.lnkov.recipes.data.STUB
import com.lnkov.recipes.databinding.FragmentRecipesListBinding
import com.lnkov.recipes.ui.recipes.recipe.RecipeFragment

class RecipesListFragment : Fragment() {
    private val binding by lazy { FragmentRecipesListBinding.inflate(layoutInflater) }

    private lateinit var recipesListAdapter: RecipesListAdapter

    private val viewModel: RecipeListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.loadCategory(getCategoryId(arguments))
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val categoryId = it.getInt(Constants.ARG_CATEGORY_ID)
            val categoryName = it.getString(Constants.ARG_CATEGORY_NAME).toString()
            val categoryImageUrl = it.getString(Constants.ARG_CATEGORY_IMAGE_URL).toString()

            // todo: эти три переменные нужно получать в LD
            // todo нам нужно получать стейт из которого мы бы могли брать эти данные
            // и инициализировать их во фрагменте

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

            // todo: drawable нам нужно получать в LD

            binding.apply {
                ivBcgRecipeList.setImageDrawable(drawable)
            }

//            initRecycler(categoryId)
            initUi()
        }

    }

    private fun initUi() {
        recipesListAdapter = RecipesListAdapter(emptyList())

        viewModel.recipeListUiState.observe(
            viewLifecycleOwner
        )
        { recipesListState: RecipeListViewModel.RecipeListUiState ->
            Log.d("!!!", "recipe list state: ${recipesListState.category?.title}")

            binding.apply {

                ivBcgRecipeList.contentDescription = "Image: ${recipesListState.category?.imageUrl}"
                tvBcgRecipeList.text = recipesListState.category?.title


            }

            initRecycler(recipesListState.category?.id)
        }
    }

    private fun initRecycler(categoryId: Int?) {
        recipesListAdapter = RecipesListAdapter(
            STUB.getRecipesByCategoryId(categoryId))

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
        var bundle: Bundle? = null

        bundle = bundleOf(
            Constants.ARG_RECIPE_ID to recipeId
        )

        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    private fun getCategoryId(arguments: Bundle?): Int? {
        return arguments.let {
            it?.getInt(Constants.ARG_CATEGORY_ID)
        }
    }
}