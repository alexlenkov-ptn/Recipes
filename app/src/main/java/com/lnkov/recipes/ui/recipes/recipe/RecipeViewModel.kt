package com.lnkov.recipes.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lnkov.recipes.model.Recipe
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import com.lnkov.recipes.data.Constants


class RecipeViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val sharedPreferences: SharedPreferences = application.getSharedPreferences(
        Constants.FAVORITES_KEY,
        Context.MODE_PRIVATE,
    )

    data class RecipeUiState(
        val recipe: Recipe? = null,
        val isFavorite: Boolean = false,
        val drawable: Drawable? = null,
        val portionsCount: Int = 1,
    )

    private val _recipeUiState = MutableLiveData<RecipeUiState>(RecipeUiState())

    init {
        Log.i("!!!", "RecipeViewModel created")
        loadRecipe(0)
    }

    val recipeUiState: LiveData<RecipeUiState>
        get() = _recipeUiState

    private fun updateRecipeUiState(state: RecipeUiState) {
        _recipeUiState.value = state
    }

    fun onFavoritesClicked(heartIconStatus: Boolean) {
        _recipeUiState.value = _recipeUiState.value?.copy(isFavorite = heartIconStatus)
    }

    fun updateNumberOfPortions(newNumberOfPortions: Int) {
        _recipeUiState.value = _recipeUiState.value?.copy(portionsCount = newNumberOfPortions)
    }

    private fun getFavorites(): Set<String> {
        return HashSet(sharedPreferences?.getStringSet(Constants.FAVORITES_KEY, emptySet()))
    }

    fun saveFavorites(sharePrefs: SharedPreferences?, stringSet: Set<String>) {
        with(sharePrefs?.edit()) {
            this?.putStringSet(Constants.FAVORITES_KEY, stringSet)
            this?.apply()
        }
    }

    private fun loadRecipe(recipeId: Int) {
        // TODO: load from network

        _recipeUiState.value = _recipeUiState.value?.copy(
            recipe = _recipeUiState.value?.recipe,
            isFavorite = getFavorites().contains(recipeId.toString()),
            drawable = _recipeUiState.value?.drawable,
            portionsCount = _recipeUiState.value?.portionsCount ?: 1,
        )
    }
}