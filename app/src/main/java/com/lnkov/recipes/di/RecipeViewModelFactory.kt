package com.lnkov.recipes.di

import com.lnkov.recipes.data.RecipeRepository
import com.lnkov.recipes.ui.recipes.recipe.RecipeViewModel

class RecipeViewModelFactory(
    private val recipeRepository: RecipeRepository,
) : Factory<RecipeViewModel> {

    override fun create(): RecipeViewModel {
        return RecipeViewModel(recipeRepository)
    }
}