package com.lnkov.recipes.ui.recipes.recipe

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.lnkov.recipes.R
import com.lnkov.recipes.data.Constants
import com.lnkov.recipes.databinding.FragmentRecipeBinding
import com.lnkov.recipes.model.Recipe
import com.lnkov.recipes.ui.IngredientsAdapter
import com.lnkov.recipes.ui.MethodAdapter

class RecipeFragment : Fragment() {
    private val binding by lazy { FragmentRecipeBinding.inflate(layoutInflater) }
    private lateinit var ingredientsListAdapter: IngredientsAdapter
    private lateinit var methodAdapter: MethodAdapter

    private val vmRecipe: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vmRecipe.loadRecipe(getRecipeId(arguments))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recipe = vmRecipe.recipeUiState.value?.recipe

        if (recipe != null) {
            initUI(recipe)
            initRecycler(recipe)
        }
    }

    private fun initUI(recipe: Recipe) {

        val drawable: Drawable? = try {
            Drawable.createFromStream(
                context?.assets?.open(recipe.imageUrl ?: ""),
                null
            )
        } catch (e: Exception) {
            Log.d("!!!", "Image not found: ${recipe.imageUrl}")
            null
        }

        binding.apply {
            ivBcgRecipe.setImageDrawable(drawable)
            ivBcgRecipe.contentDescription = "Image: ${recipe.imageUrl}"
            tvRecipe.text = recipe.title

            ibIcHeart.setOnClickListener { vmRecipe.onFavoritesClicked() }
        }

        vmRecipe.recipeUiState.observe(
            viewLifecycleOwner
        )
        { recipeState: RecipeViewModel.RecipeUiState ->
            Log.i("!!!", "state heartIconStatus ${recipeState.isFavorite}")
            if (recipeState.isFavorite) binding.ibIcHeart.setImageResource(R.drawable.ic_heart_recipe)
            else binding.ibIcHeart.setImageResource(R.drawable.ic_heart_empty_recipe)
        }

    }

    private fun initRecycler(recipe: Recipe) {
        ingredientsListAdapter = IngredientsAdapter(recipe.ingredients)
        methodAdapter = MethodAdapter(recipe.method)

        val decorator = MaterialDividerItemDecoration(
            requireContext(),
            MaterialDividerItemDecoration.VERTICAL,
        ).apply {
            isLastItemDecorated = false
            dividerInsetStart = resources.getDimensionPixelSize(R.dimen.main_space_12)
            dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.main_space_12)
            dividerColor = resources.getColor(R.color.gray)
        }

        binding.apply {
            rvRecipeIngredients.addItemDecoration(decorator)
            rvRecipeCookingMethod.addItemDecoration(decorator)
            rvRecipeIngredients.adapter = ingredientsListAdapter
            rvRecipeCookingMethod.adapter = methodAdapter

            sbCountsOfRecipes.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    @SuppressLint("SetTextI18n")
                    override fun onProgressChanged(
                        p0: SeekBar?,
                        portionsCount: Int,
                        p2: Boolean
                    ) {
                        ingredientsListAdapter.updateIngredients(portionsCount)
                        tvNumberOfPortions.text = portionsCount.toString()
                        rvRecipeIngredients.adapter = ingredientsListAdapter
                        vmRecipe.updateNumberOfPortions(portionsCount)
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {}
                    override fun onStopTrackingTouch(p0: SeekBar?) {}
                }
            )
        }
    }

    fun getRecipeId(arguments: Bundle?): Int? {
        var recipeId: Int? = null

        arguments.let {
            recipeId = it?.getInt(Constants.ARG_RECIPE_ID)
            Log.d("!!!", "recipe Id: $recipeId")
        }

        return recipeId
    }
}