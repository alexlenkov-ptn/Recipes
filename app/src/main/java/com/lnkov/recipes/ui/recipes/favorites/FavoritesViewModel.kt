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

class FavoritesViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val sharedPreferences: SharedPreferences = application.getSharedPreferences(
        Constants.FAVORITES_KEY,
        Context.MODE_PRIVATE,
    )

    private fun getFavoriteSet(): HashSet<String> {
        return HashSet<String>(sharedPreferences?.getStringSet(Constants.FAVORITES_KEY, emptySet()))
    }

    private val _favoriteUiState =
        MutableLiveData<FavoritesUiState>(FavoritesUiState())

    val favoriteUiState: LiveData<FavoritesUiState>
        get() = _favoriteUiState

    data class FavoritesUiState(
        val favoriteList: List<Recipe?> = emptyList()
    )

    private fun getFavoriteRecipes(): List<Recipe?> {
        return STUB.getRecipeByIds(getFavoriteSet().map { it.toInt() }.toSet())
    }

    fun loadRecipes() {
        _favoriteUiState.value =
            favoriteUiState.value?.copy(favoriteList = getFavoriteRecipes())
    }

}