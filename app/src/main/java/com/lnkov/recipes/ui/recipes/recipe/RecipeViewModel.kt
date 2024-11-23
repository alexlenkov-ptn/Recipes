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
import androidx.lifecycle.viewModelScope
import com.lnkov.recipes.data.Constants
import com.lnkov.recipes.data.RecipeRepository
import kotlinx.coroutines.launch


class RecipeViewModel(
    application: Application,
) : AndroidViewModel(application) {
    init {
        Log.i("RecipeViewModel", "RecipeViewModel created")
    }

    private val sharedPreferences: SharedPreferences = application.getSharedPreferences(
        Constants.FAVORITES_KEY,
        Context.MODE_PRIVATE,
    )

    data class RecipeUiState(
        val recipe: Recipe? = null,
        val isFavorite: Boolean = false,
        val drawableUrl: String = "",
        val portionsCount: Int = 1,
        val isLoaded: Boolean? = null
    )

    private val _recipeUiState = MutableLiveData<RecipeUiState>(RecipeUiState())

    val recipeUiState: LiveData<RecipeUiState>
        get() = _recipeUiState

    private val recipeRepository = RecipeRepository()

    fun onFavoritesClicked(recipeIdString: String) {
        val favoriteSet: HashSet<String> =
            HashSet(sharedPreferences?.getStringSet(Constants.FAVORITES_KEY, emptySet()))

        Log.d("RecipeViewModel", "$recipeIdString")

        if (favoriteSet.contains(recipeIdString)) {
            favoriteSet.remove(recipeIdString)
            _recipeUiState.value = recipeUiState.value?.copy(isFavorite = false)
        } else {
            favoriteSet.add(recipeIdString)
            _recipeUiState.value = recipeUiState.value?.copy(isFavorite = true)
        }

        sharedPreferences.edit().putStringSet(Constants.FAVORITES_KEY, favoriteSet).apply()

    }

    fun updateNumberOfPortions(count: Int) {
        _recipeUiState.value = recipeUiState.value?.copy(portionsCount = count)
        Log.d("!!!", "updateNumberOfPortions: ${recipeUiState.value?.portionsCount}")
    }

    private fun getFavorites(): Set<String> {
        return HashSet(sharedPreferences?.getStringSet(Constants.FAVORITES_KEY, emptySet()))
    }

    private fun getDrawable(recipe: Recipe?): Drawable? {
        val drawableUrl = recipe?.imageUrl
        return try {
            Drawable.createFromStream(
                getApplication<Application>().assets.open(drawableUrl ?: ""),
                null
            )
        } catch (e: Exception) {
            Log.d("!!!", "Image not found: $drawableUrl")
            null
        }
    }


    fun loadRecipe(recipeId: Int?) {

        viewModelScope.launch {
            val recipe = recipeId?.let { recipeRepository.loadRecipeById(recipeId) }
            var isLoaded = false

            if (recipe != null) {
                isLoaded = true
            }

            _recipeUiState.postValue(
                recipeUiState.value?.copy(
                    recipe = recipe,
                    isFavorite = getFavorites().contains(recipeId.toString()),
                    drawableUrl = "${Constants.BASE_IMAGE_URL}${recipe?.imageUrl}",
                    portionsCount = recipeUiState.value?.portionsCount ?: 1,
                    isLoaded = isLoaded
                )
            )

        }
    }

}