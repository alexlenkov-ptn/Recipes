package com.lnkov.recipes.ui.recipes.recipe

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lnkov.recipes.model.Recipe
import android.graphics.drawable.Drawable
import com.lnkov.recipes.data.Constants


class RecipeViewModel : ViewModel() {
    data class RecipeUiState(
        val recipe: Recipe? = null,
        val heartIconStatus: Boolean = false,
        val drawable: Drawable? = null,
        val numberOfPortions: Int = 1,
    )

    private val _recipeUiState = MutableLiveData<RecipeUiState>(RecipeUiState())

    init {
        Log.i("!!!", "RecipeViewModel created")
        updateHeartIconStatus(true)
//        loadRecipe(0)
    }

    val recipeUiState: LiveData<RecipeUiState>
        get() = _recipeUiState

    private fun updateRecipeUiState(state: RecipeUiState) {
        _recipeUiState.value = state
    }

    private fun updateHeartIconStatus(newIconStatus: Boolean) {
        _recipeUiState.value = _recipeUiState.value?.copy(heartIconStatus = newIconStatus)
    }

    private fun updateNumberOfPortions(newNumberOfPortions: Int) {
        _recipeUiState.value = _recipeUiState.value?.copy(numberOfPortions = newNumberOfPortions)
    }

    private fun loadRecipe(recipeId: Int) {
        // TODO: load from network
    }

    fun getFavorites(sharePrefs: SharedPreferences?): MutableSet<String> {
        return HashSet(sharePrefs?.getStringSet(Constants.FAVORITES_KEY, emptySet()))
    }

}