package com.lnkov.recipes.di

import com.lnkov.recipes.data.RecipeRepository
import com.lnkov.recipes.ui.categories.CategoriesViewModel

class CategoriesListViewModelFactory(
    private val recipeRepository: RecipeRepository,
) : Factory<CategoriesViewModel> {

    override fun create(): CategoriesViewModel {
        return CategoriesViewModel(recipeRepository)
    }
}