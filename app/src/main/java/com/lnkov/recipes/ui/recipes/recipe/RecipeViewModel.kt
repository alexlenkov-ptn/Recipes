package com.lnkov.recipes.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lnkov.recipes.model.Recipe
import android.graphics.drawable.Drawable


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
    }

    val recipeUiState: LiveData<RecipeUiState>
        get() = _recipeUiState

    fun updateRecipeUiState(state: RecipeUiState) {
        _recipeUiState.value = state
    }

    fun updateHeartIconStatus(newIconStatus: Boolean) {
        _recipeUiState.value = _recipeUiState.value?.copy(heartIconStatus = newIconStatus)
    }

    private fun updateNumberOfPortions(newNumberOfPortions: Int) {
        _recipeUiState.value = _recipeUiState.value?.copy(numberOfPortions = newNumberOfPortions)
    }

}