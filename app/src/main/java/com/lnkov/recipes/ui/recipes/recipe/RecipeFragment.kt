package com.lnkov.recipes.ui.recipes.recipe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.lnkov.recipes.R
import com.lnkov.recipes.databinding.FragmentRecipeBinding
import com.lnkov.recipes.ui.IngredientsAdapter
import com.lnkov.recipes.ui.MethodAdapter

class RecipeFragment : Fragment() {
    private val binding by lazy { FragmentRecipeBinding.inflate(layoutInflater) }
    private lateinit var ingredientsListAdapter: IngredientsAdapter
    private lateinit var methodAdapter: MethodAdapter

    private val viewModel: RecipeViewModel by viewModels()
    private val args: RecipeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadRecipe(args.recipeId)
        initUI()
    }

    private fun initUI() {
        ingredientsListAdapter = IngredientsAdapter(emptyList())

        methodAdapter = MethodAdapter(emptyList())

        val decorator = MaterialDividerItemDecoration(
            requireContext(),
            MaterialDividerItemDecoration.VERTICAL,
        ).apply {
            isLastItemDecorated = false
            dividerInsetStart = resources.getDimensionPixelSize(R.dimen.main_space_12)
            dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.main_space_12)
            dividerColor = resources.getColor(R.color.gray)
        }

        viewModel.recipeUiState.observe(
            viewLifecycleOwner
        )
        { state: RecipeViewModel.RecipeUiState ->
            Log.i("RecipeFragment", "state heartIconStatus ${state.isFavorite}")
            Log.i("RecipeFragment", "state portionCount ${state.portionsCount}")

            if (state.isLoaded == false) {
                Toast.makeText(context, R.string.toast_error_message, Toast.LENGTH_LONG).show()
            } else {
                binding.apply {
                    if (state.isFavorite) ibIcHeart.setImageResource(R.drawable.ic_heart_recipe)
                    else ibIcHeart.setImageResource(R.drawable.ic_heart_empty_recipe)
                    ivBcgRecipe.setImageDrawable(state.drawable)

                    ingredientsListAdapter.updateData(state.recipe?.ingredients ?: emptyList())

                    methodAdapter.updateData(state.recipe?.method ?: emptyList())

                    ingredientsListAdapter.updateIngredients(state.portionsCount)
                    tvNumberOfPortions.text = state.portionsCount.toString()
                    rvRecipeIngredients.adapter = ingredientsListAdapter

                    ivBcgRecipe.contentDescription = "Image: ${state.recipe?.imageUrl}"
                    tvRecipe.text = state.recipe?.title

                    rvRecipeIngredients.adapter = ingredientsListAdapter
                    rvRecipeCookingMethod.adapter = methodAdapter

                    sbCountsOfRecipes.setOnSeekBarChangeListener(
                        PortionSeekBarListener { progress ->
                            viewModel.updateNumberOfPortions(progress)
                        }
                    )
                }
            }


        }

        binding.apply {
            ibIcHeart.setOnClickListener { viewModel.onFavoritesClicked() }
            rvRecipeIngredients.addItemDecoration(decorator)
            rvRecipeCookingMethod.addItemDecoration(decorator)
        }
    }
}

class PortionSeekBarListener(
    val onChangeIngredientListener: (Int) -> Unit
) : OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        onChangeIngredientListener(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}