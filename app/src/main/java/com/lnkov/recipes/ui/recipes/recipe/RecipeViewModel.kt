package com.lnkov.recipes.ui.recipes.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lnkov.recipes.model.RecipeUiState

class RecipeViewModel : ViewModel() {

    private val _recipeUiState = MutableLiveData<RecipeUiState>()

    val recipeUiState: LiveData<RecipeUiState>
        get() = _recipeUiState

    fun updateRecipeUiState(state: RecipeUiState) {
        _recipeUiState.value = state
    }

    fun updateHeartIconStatus(newIconStatus: Boolean) {
        _recipeUiState.value = _recipeUiState.value?.copy(heartIconStatus = newIconStatus)
    }

    fun updateNumberOfPortions(newNumberOfPortions: Int) {
        _recipeUiState.value = _recipeUiState.value?.copy(numberOfPortions = newNumberOfPortions)
    }

}