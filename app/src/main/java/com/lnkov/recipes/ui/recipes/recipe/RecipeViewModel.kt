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
import com.lnkov.recipes.data.STUB


class RecipeViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private var isFavorite: Boolean = false

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

    fun onFavoritesClicked(recipeId: Int) {
        if (sharedPreferences.contains(recipeId.toString())) isFavorite = true
    }

    fun saveFavorite() {
        if (isFavorite)
            _recipeUiState.value = _recipeUiState.value?.copy(isFavorite = true)
        else
            _recipeUiState.value = _recipeUiState.value?.copy(isFavorite = false)
    }

    fun updateNumberOfPortions(newNumberOfPortions: Int) {
        _recipeUiState.value = _recipeUiState.value?.copy(portionsCount = newNumberOfPortions)
    }

    private fun getFavorites(): Set<String> {
        return HashSet(sharedPreferences?.getStringSet(Constants.FAVORITES_KEY, emptySet()))
    }


    private fun loadRecipe(recipeId: Int) {
        // TODO: load from network
        _recipeUiState.value = recipeUiState.value?.copy(
            recipe = STUB.getRecipeById(0, recipeId),
            isFavorite = getFavorites().contains(recipeId.toString()),
            drawable = recipeUiState.value?.drawable,
            portionsCount = recipeUiState.value?.portionsCount ?: 1,
        )
    }
}