package com.lnkov.recipes.ui.recipes.recipe

import android.annotation.SuppressLint
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

    private val vmRecipeFragment: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vmRecipeFragment.loadRecipe(getRecipeId(arguments))

        super.onViewCreated(view, savedInstanceState)
        val recipe = vmRecipeFragment.recipeUiState.value?.recipe

        if (recipe != null) {
            initUI(recipe)
        }
    }

    private fun initUI(recipe: Recipe) {
        val vmState = vmRecipeFragment.recipeUiState.value

        ingredientsListAdapter =
            IngredientsAdapter(vmState?.recipe?.ingredients ?: emptyList())
        methodAdapter =
            MethodAdapter(vmState?.recipe?.method ?: emptyList())

        val decorator = MaterialDividerItemDecoration(
            requireContext(),
            MaterialDividerItemDecoration.VERTICAL,
        ).apply {
            isLastItemDecorated = false
            dividerInsetStart = resources.getDimensionPixelSize(R.dimen.main_space_12)
            dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.main_space_12)
            dividerColor = resources.getColor(R.color.gray)
        }

        vmRecipeFragment.recipeUiState.observe(
            viewLifecycleOwner
        )
        { recipeState: RecipeViewModel.RecipeUiState ->
            Log.i("!!!", "state heartIconStatus ${recipeState.isFavorite}")
            Log.i("!!!", "state portionCount ${recipeState.portionsCount}")


            binding.apply {
                if (recipeState.isFavorite) ibIcHeart.setImageResource(R.drawable.ic_heart_recipe)
                else ibIcHeart.setImageResource(R.drawable.ic_heart_empty_recipe)
                ivBcgRecipe.setImageDrawable(recipeState.drawable)

                ingredientsListAdapter.updateIngredients(recipeState.portionsCount)
                tvNumberOfPortions.text = recipeState.portionsCount.toString()
                rvRecipeIngredients.adapter = ingredientsListAdapter
            }
        }

        binding.apply {
            ivBcgRecipe.contentDescription = "Image: ${recipe.imageUrl}"
            tvRecipe.text = recipe.title
            ibIcHeart.setOnClickListener { vmRecipeFragment.onFavoritesClicked() }

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
                        Log.d("!!!", "portions: $portionsCount")
                        vmRecipeFragment.updateNumberOfPortions(portionsCount)
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {}
                    override fun onStopTrackingTouch(p0: SeekBar?) {}
                }
            )
        }
    }

}

private fun getRecipeId(arguments: Bundle?): Int? {
    var recipeId: Int? = null

    arguments.let {
        recipeId = it?.getInt(Constants.ARG_RECIPE_ID)
        Log.d("!!!", "recipe Id: $recipeId")
    }

    return recipeId
}