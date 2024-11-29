package com.lnkov.recipes.di

import com.lnkov.recipes.data.RecipeRepository
import com.lnkov.recipes.ui.recipes.recipe_list.RecipesListViewModel

class RecipesListViewModelFactory(
    private val recipeRepository: RecipeRepository,
) : Factory<RecipesListViewModel> {

    override fun create(): RecipesListViewModel {
        return RecipesListViewModel(recipeRepository)
    }

}