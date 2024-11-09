package com.lnkov.recipes.ui.recipes.favorites


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
import androidx.navigation.fragment.findNavController
import com.lnkov.recipes.R
import com.lnkov.recipes.data.Constants
import com.lnkov.recipes.data.STUB
import com.lnkov.recipes.databinding.FragmentFavoritesBinding
import com.lnkov.recipes.ui.recipes.recipe.RecipeFragment
import com.lnkov.recipes.ui.recipes.recipe_list.RecipesListAdapter

class FavoritesFragment : Fragment() {
    private val binding by lazy { FragmentFavoritesBinding.inflate(layoutInflater) }
    private lateinit var recipesListAdapter: RecipesListAdapter

    private val viewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.loadRecipes()
        super.onViewCreated(view, savedInstanceState)
        initUi()
        Log.d("FavoriteFragment", "onViewCreated")
    }

    private fun initUi() {
        recipesListAdapter = RecipesListAdapter(emptyList()).apply {
            setOnItemClickListener(
                object : RecipesListAdapter.OnItemClickListener {
                    override fun onItemClick(recipeId: Int) {
                        openRecipeByRecipesId(recipeId)
                    }
                }
            )
        }

        viewModel.favoriteUiState.observe(
            viewLifecycleOwner
        ) { state: FavoritesViewModel.FavoritesUiState ->
            Log.i("!!!", "state favoriteList ${state.favoriteList}")

            binding.apply {
                if (state.favoriteList.isEmpty()) {
                    rvRecipes.visibility = View.GONE
                    tvFavoritesRecyclerIsNull.visibility = View.VISIBLE
                } else {
                    tvFavoritesRecyclerIsNull.visibility = View.GONE
                    recipesListAdapter.updateData(state.favoriteList)
                    rvRecipes.adapter = recipesListAdapter
                }
            }
        }

    }

    private fun openRecipeByRecipesId(recipeId: Int) {

        val action = FavoritesFragmentDirections.actionFavoritesFragmentToRecipeFragment(recipeId)

        findNavController().navigate(action)
    }

}