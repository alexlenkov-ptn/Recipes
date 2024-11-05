package com.lnkov.recipes.ui.recipes.favorites

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lnkov.recipes.data.Constants
import com.lnkov.recipes.data.STUB
import com.lnkov.recipes.model.Recipe
import com.lnkov.recipes.ui.recipes.recipe.RecipeViewModel.RecipeUiState

class FavoritesViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val sharedPreferences: SharedPreferences = application.getSharedPreferences(
        Constants.FAVORITES_KEY,
        Context.MODE_PRIVATE,
    )

    private val favoritesSet =
        HashSet<String>(sharedPreferences?.getStringSet(Constants.FAVORITES_KEY, emptySet()))

    private val _favoriteUiState =
        MutableLiveData<FavoritesUiState>(FavoritesUiState())

    val favoriteUiState: LiveData<FavoritesUiState>
        get() = _favoriteUiState

    data class FavoritesUiState(
        val recipeList: List<Recipe?> = emptyList()
    )

    private fun getFavoriteRecipes(): List<Recipe?> {
        return STUB.getRecipeByIds(favoritesSet.map { it.toInt() }.toSet())
    }

    fun updateRecipeList() {
        _favoriteUiState.value =
            favoriteUiState.value?.copy(recipeList = getFavoriteRecipes())
    }

}