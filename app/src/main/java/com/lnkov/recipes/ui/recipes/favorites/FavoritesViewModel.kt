package com.lnkov.recipes.ui.recipes.favorites

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lnkov.recipes.MyApplication
import com.lnkov.recipes.data.Constants
import com.lnkov.recipes.data.RecipeRepository
import com.lnkov.recipes.model.Recipe
import kotlinx.coroutines.launch

class FavoritesViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val sharedPreferences: SharedPreferences = application.getSharedPreferences(
        Constants.FAVORITES_KEY,
        Context.MODE_PRIVATE,
    )

    private val recipeRepository = RecipeRepository()

    private val threadPool = (application as MyApplication).threadPool

    private fun getFavoriteSet(): HashSet<String> {
        return HashSet<String>(sharedPreferences?.getStringSet(Constants.FAVORITES_KEY, emptySet()))
    }

    private val _favoriteUiState =
        MutableLiveData<FavoritesUiState>(FavoritesUiState())

    val favoriteUiState: LiveData<FavoritesUiState>
        get() = _favoriteUiState

    data class FavoritesUiState(
        val favoriteList: List<Recipe?>? = emptyList(),
        val isLoaded: Boolean? = null
    )

    fun loadRecipes() {

        viewModelScope.launch {
            val favoriteList = recipeRepository.loadRecipesByIds(getRecipesIds())
            var isLoaded: Boolean? = null

            if (favoriteList != null) isLoaded = true
            else isLoaded = false

            _favoriteUiState.postValue(
                favoriteUiState.value?.copy(
                    favoriteList = favoriteList,
                    isLoaded = isLoaded
                )
            )

            Log.d("FavoritesViewModel", "favoriteList: $favoriteList")

            Log.d("FavoritesViewModel", "${_favoriteUiState.value}")
        }
    }

    private fun getRecipesIds(): String {
        val recipesIds = getFavoriteSet().map { it.toInt() }.toSet()

        Log.d("FavoritesViewModel", "${recipesIds.toString().formatRecipesIdsSet()}")
        return recipesIds.toString().formatRecipesIdsSet()
    }

    private fun String.formatRecipesIdsSet(): String {
        return this
            .substringAfter("[")
            .substringBefore("]")
            .replace(" ", "")
    }

}