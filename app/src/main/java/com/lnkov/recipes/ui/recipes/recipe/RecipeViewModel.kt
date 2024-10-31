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
    }

    val recipeUiState: LiveData<RecipeUiState>
        get() = _recipeUiState

    fun onFavoritesClicked(recipeId: Int): Boolean = sharedPreferences.contains(recipeId.toString())

    fun saveFavorite(boolean: Boolean) {
        if (boolean) {
            _recipeUiState.value = recipeUiState.value?.copy(isFavorite = false)
        } else {
            _recipeUiState.value = recipeUiState.value?.copy(isFavorite = true)
        }
        updateFavorites()
    }

    fun updateFavorites() {
        val favoriteSet: HashSet<String> =
            HashSet(sharedPreferences?.getStringSet(Constants.FAVORITES_KEY, emptySet()))
        val recipeIdString = recipeUiState.value?.recipe?.id.toString()

        if (recipeUiState.value?.isFavorite == true) {
            favoriteSet.add(recipeIdString)
        } else {
            favoriteSet.remove(recipeIdString)
        }

        sharedPreferences.edit().putStringSet(Constants.FAVORITES_KEY, favoriteSet)
    }

    fun updateNumberOfPortions(newNumberOfPortions: Int) {
        _recipeUiState.value = _recipeUiState.value?.copy(portionsCount = newNumberOfPortions)
    }

    private fun getFavorites(): Set<String> {
        return HashSet(sharedPreferences?.getStringSet(Constants.FAVORITES_KEY, emptySet()))
    }


    fun loadRecipe(recipeId: Int?) {
        // TODO: load from network
        _recipeUiState.value = recipeUiState.value?.copy(
            recipe = recipeId?.let { STUB.getRecipeById(0, it) },
            isFavorite = getFavorites().contains(recipeId.toString()),
            drawable = recipeUiState.value?.drawable,
            portionsCount = recipeUiState.value?.portionsCount ?: 1,
        )
    }
}