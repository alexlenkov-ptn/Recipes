package com.lnkov.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lnkov.recipes.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {
    private val binding by lazy { FragmentFavoritesBinding.inflate(layoutInflater) }
    private lateinit var recipesListAdapter: RecipesListAdapter
    val favoritesSet: MutableSet<String> = RecipeFragment.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipesListAdapter = RecipesListAdapter(STUB.getRecipesByCategoryId(categoryId))

        initRecycler(categoryId)
    }

    private fun initRecycler(categoryId: Int) {

    }
}