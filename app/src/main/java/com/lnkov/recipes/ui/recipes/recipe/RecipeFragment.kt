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
    private val sharePrefs by lazy {
        context?.getSharedPreferences(
            Constants.FAVORITES_KEY,
            Context.MODE_PRIVATE
        )
    }

    private lateinit var viewModel : RecipeViewModel

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
            var recipe: Recipe? = null

            recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(Constants.ARG_RECIPE, Recipe::class.java)
            } else {
                it.getParcelable(Constants.ARG_RECIPE)
            }

            Log.d("!!!", "recipeDeprecatedMethod title: ${recipe?.title}")

            if (recipe != null) {
                initUI(recipe)
                initRecycler(recipe)
            }
        }

    }

    private fun initUI(recipe: Recipe) {
        var heartIconStatus: Boolean
        val favoritesSet: MutableSet<String> = getFavorites(sharePrefs)
        val recipeIdString = recipe.id.toString()

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
                heartIconStatus = favoritesSet.contains(recipeIdString) == true

                if (heartIconStatus) setImageResource(R.drawable.ic_heart_recipe)
                else setImageResource(R.drawable.ic_heart_empty_recipe)

                setOnClickListener {
                    heartIconStatus = !heartIconStatus

                    when (heartIconStatus) {
                        true -> {
                            setImageResource(R.drawable.ic_heart_recipe)
                            favoritesSet.add(recipeIdString)
                            saveFavorites(sharePrefs, favoritesSet.toSet())
                        }

                        false -> {
                            setImageResource(R.drawable.ic_heart_empty_recipe)
                            favoritesSet.remove(recipeIdString)
                            saveFavorites(sharePrefs, favoritesSet)
                        }
                    }
                }
            }
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
                    override fun onProgressChanged(p0: SeekBar?, numberOfPortions: Int, p2: Boolean) {
                        ingredientsListAdapter.updateIngredients(numberOfPortions)
                        tvNumberOfPortions.text = numberOfPortions.toString()
                        rvRecipeIngredients.adapter = ingredientsListAdapter
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {}
                    override fun onStopTrackingTouch(p0: SeekBar?) {}
                }
            )
        }
    }

    companion object {

        fun saveFavorites(sharePrefs: SharedPreferences?, stringSet: Set<String>) {
            with(sharePrefs?.edit()) {
                this?.putStringSet(Constants.FAVORITES_KEY, stringSet)
                this?.apply()
            }
        }

        fun getFavorites(sharePrefs: SharedPreferences?): MutableSet<String> {
            return HashSet(sharePrefs?.getStringSet(Constants.FAVORITES_KEY, emptySet()))
        }

    }
}