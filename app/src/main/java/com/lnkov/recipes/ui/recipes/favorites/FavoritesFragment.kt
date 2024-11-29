package com.lnkov.recipes.ui.recipes.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lnkov.recipes.R
import com.lnkov.recipes.RecipeApplication
import com.lnkov.recipes.databinding.FragmentFavoritesBinding
import com.lnkov.recipes.ui.recipes.recipe_list.RecipesListAdapter

class FavoritesFragment : Fragment() {
    private val binding by lazy { FragmentFavoritesBinding.inflate(layoutInflater) }
    private lateinit var recipesListAdapter: RecipesListAdapter
    private lateinit var viewModel: FavoritesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (requireActivity().application as RecipeApplication).appContainer
        viewModel = appContainer.favoritesViewModelFactory.create()
    }

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
            Log.i("FavoritesFragment", "state favoriteList ${state.favoriteList}")

            if (state.isLoaded == false) {
                Toast.makeText(context, R.string.toast_error_message, Toast.LENGTH_LONG).show()
            } else {
                binding.apply {
                    if (state.favoriteList?.isEmpty() == true) {
                        rvRecipes.visibility = View.GONE
                        tvFavoritesRecyclerIsNull.visibility = View.VISIBLE

                        Log.d("FavoritesFragment", "state.favoriteList is Empty")
                    } else {
                        tvFavoritesRecyclerIsNull.visibility = View.GONE
                        rvRecipes.visibility = View.VISIBLE
                        recipesListAdapter.updateData(state.favoriteList ?: emptyList())
                        rvRecipes.adapter = recipesListAdapter

                        Log.d("FavoritesFragment", "state.favoriteList is NOT Empty")
                    }
                }
            }
        }

    }

    private fun openRecipeByRecipesId(recipeId: Int) {
        val action = FavoritesFragmentDirections.actionFavoritesFragmentToRecipeFragment(recipeId)
        findNavController().navigate(action)
    }

}