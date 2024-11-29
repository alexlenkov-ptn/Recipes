package com.lnkov.recipes.di

import com.lnkov.recipes.data.RecipeRepository
import com.lnkov.recipes.ui.recipes.favorites.FavoritesViewModel

class FavoritesViewModelFactory(
    private val recipeRepository: RecipeRepository,
) : Factory<FavoritesViewModel> {

    override fun create(): FavoritesViewModel {
        return FavoritesViewModel(recipeRepository)
    }
}