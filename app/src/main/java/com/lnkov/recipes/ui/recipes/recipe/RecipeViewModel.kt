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

    fun onFavoritesClicked() {
        val favoriteSet: HashSet<String> =
            HashSet(sharedPreferences?.getStringSet(Constants.FAVORITES_KEY, emptySet()))

        val recipeIdString = recipeUiState.value?.recipe?.id.toString()

        if (favoriteSet.contains(recipeIdString)) {
            favoriteSet.remove(recipeIdString)
            _recipeUiState.value = recipeUiState.value?.copy(isFavorite = false)
        } else {
            favoriteSet.add(recipeIdString)
            _recipeUiState.value = recipeUiState.value?.copy(isFavorite = true)
        }

        sharedPreferences.edit().putStringSet(Constants.FAVORITES_KEY, favoriteSet).apply()
    }

    fun updateNumberOfPortions(newNumberOfPortions: Int) {
        _recipeUiState.value = _recipeUiState.value?.copy(portionsCount = newNumberOfPortions)
    }

    private fun getFavorites(): Set<String> {
        return HashSet(sharedPreferences?.getStringSet(Constants.FAVORITES_KEY, emptySet()))
    }


    private fun getDrawable(recipe: Recipe?): Drawable? {

        val drawableUrl = recipe?.imageUrl

        val assetManager = getApplication<Application>().assets

        return try {
            Drawable.createFromStream(
                assetManager.open(drawableUrl ?: ""),
                null
            )
        } catch (e: Exception) {
            Log.d("!!!", "Image not found: $drawableUrl")
            null
        }
    }


    fun loadRecipe(recipeId: Int?) {
        // TODO: load from network

        val recipe = recipeId?.let { STUB.getRecipeById(0, it) }

        _recipeUiState.value = recipeUiState.value?.copy(
            recipe = recipe,
            isFavorite = getFavorites().contains(recipeId.toString()),
            drawable = getDrawable(recipe),
            portionsCount = recipeUiState.value?.portionsCount ?: 1,
        )
    }
}