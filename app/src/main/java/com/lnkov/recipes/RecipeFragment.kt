package com.lnkov.recipes

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.lnkov.recipes.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {
    private val binding by lazy { FragmentRecipeBinding.inflate(layoutInflater) }

    private lateinit var ingredientsListAdapter: IngredientsAdapter
    private lateinit var methodAdapter: MethodAdapter

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

            val drawable: Drawable? = try {
                Drawable.createFromStream(
                    context?.assets?.open(recipe?.imageUrl ?: ""),
                    null
                )
            } catch (e: Exception) {
                Log.d("!!!", "Image not found: ${recipe?.imageUrl}")
                null
            }

            binding.ivBcgRecipe.setImageDrawable(drawable)
            binding.tvRecipe.text = recipe?.title

            if (recipe != null) initRecycler(recipe)
        }

    }

    private fun initRecycler(recipe: Recipe) {
        ingredientsListAdapter = IngredientsAdapter(recipe.ingredients)
        methodAdapter = MethodAdapter(recipe.method)

        val decorator = MaterialDividerItemDecoration(
            requireContext(),
            MaterialDividerItemDecoration.VERTICAL,
        )
        decorator.isLastItemDecorated = false

        val inset = (12 * requireContext().resources.displayMetrics.density).toInt()

        decorator.dividerInsetStart = inset
        decorator.dividerInsetEnd = inset

        binding.rvRecipeIngredients.addItemDecoration(decorator)
        binding.rvRecipeCookingMethod.addItemDecoration(decorator)

        binding.rvRecipeIngredients.adapter = ingredientsListAdapter
        binding.rvRecipeCookingMethod.adapter = methodAdapter
    }


}