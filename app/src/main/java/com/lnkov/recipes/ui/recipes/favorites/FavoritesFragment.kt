package com.lnkov.recipes.ui.recipes.favorites

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.lnkov.recipes.R
import com.lnkov.recipes.data.Constants
import com.lnkov.recipes.data.STUB
import com.lnkov.recipes.databinding.FragmentFavoritesBinding
import com.lnkov.recipes.ui.recipes.recipe.RecipeFragment
import com.lnkov.recipes.ui.recipes.recipe_list.RecipesListAdapter

class FavoritesFragment : Fragment() {
    private val binding by lazy { FragmentFavoritesBinding.inflate(layoutInflater) }
    private lateinit var recipesListAdapter: RecipesListAdapter

    private val sharePrefs by lazy {
        requireContext().getSharedPreferences(
            Constants.FAVORITES_KEY,
            Context.MODE_PRIVATE
        )
    }

    private lateinit var favoritesSet: Set<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesSet = getFavorites(sharePrefs)

        if (favoritesSet.isEmpty()) {
            binding.rvRecipes.visibility = View.GONE
            binding.tvFavoritesRecyclerIsNull.visibility = View.VISIBLE
        } else {
            binding.tvFavoritesRecyclerIsNull.visibility = View.GONE
            initRecycler()
        }

    }

    private fun initRecycler() {
        recipesListAdapter =
            RecipesListAdapter(STUB.getRecipeByIds(favoritesSet.map { it.toInt() }.toSet()))
        binding.rvRecipes.adapter = recipesListAdapter

        recipesListAdapter.setOnItemClickListener(
            object : RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipesId(0, recipeId)
                }
            }
        )

    }

    private fun openRecipeByRecipesId(categoryId: Int, recipeId: Int) {
        var bundle: Bundle? = null

        val recipe = STUB.getRecipeById(categoryId, recipeId)

        bundle = bundleOf(
            Constants.ARG_RECIPE_ID to recipe?.id
        )

        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    private fun getFavorites(sharePrefs: SharedPreferences?): MutableSet<String> {
        return HashSet<String>(sharePrefs?.getStringSet(Constants.FAVORITES_KEY, emptySet()))
    }

}