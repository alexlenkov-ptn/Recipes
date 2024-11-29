package com.lnkov.recipes.ui.recipes.favorites

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lnkov.recipes.data.RecipeRepository
import com.lnkov.recipes.model.Recipe
import kotlinx.coroutines.launch

class FavoritesViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val recipeRepository = RecipeRepository(application)

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
            val favoriteList = recipeRepository.getFavoritesRecipes()

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
}