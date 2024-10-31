package com.lnkov.recipes.ui.recipes.recipe

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
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
    private val recipe = getRecipe(arguments)

    private val vmRecipe: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vmRecipe.loadRecipe(recipe?.id)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

            ibIcHeart.apply {
                val heartIconStatus = vmRecipe.recipeUiState.value?.isFavorite

                if (heartIconStatus == true) setImageResource(R.drawable.ic_heart_recipe)
                else setImageResource(R.drawable.ic_heart_empty_recipe)

                setOnClickListener {
                    val isFavorite = vmRecipe.onFavoritesClicked(recipe.id)

                    when (isFavorite) {
                        true -> setImageResource(R.drawable.ic_heart_empty_recipe)
                        false -> setImageResource(R.drawable.ic_heart_recipe)
                    }
                    vmRecipe.saveFavorite(isFavorite)
                }
            }
        }

        vmRecipe.recipeUiState.observe(
            viewLifecycleOwner
        )
        { recipeState: RecipeViewModel.RecipeUiState ->
            Log.i("!!!", "state heartIconStatus ${recipeState.isFavorite}")
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

    companion object {

        fun getRecipe(arguments: Bundle?): Recipe? {
            var recipe: Recipe? = null

            arguments?.let {
                Log.d("!!!", "recipeDeprecatedMethod title: ${recipe?.title}")

                recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.getParcelable(Constants.ARG_RECIPE, Recipe::class.java)
                } else {
                    it.getParcelable(Constants.ARG_RECIPE)
                }
            }
            return recipe
        }

    }
}