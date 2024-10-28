package com.lnkov.recipes.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lnkov.recipes.data.Constants
import com.lnkov.recipes.data.STUB
import com.lnkov.recipes.model.RecipeUiState

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val _recipeUiState = MutableLiveData<RecipeUiState>()
    private val recipeId = 0

    private val sharePrefs by lazy {
        application.getSharedPreferences(
            Constants.FAVORITES_KEY,
            Context.MODE_PRIVATE
        )
    }

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

    fun updateNumberOfPortions(newNumberOfPortions: Int) {
        _recipeUiState.value = _recipeUiState.value?.copy(numberOfPortions = newNumberOfPortions)
    }

    fun loadRecipe() {
        val recipe = STUB.getRecipeById(0, recipeId)
        val heartIconStatus = getFavorites(sharePrefs).contains(recipeId.toString())

        _recipeUiState.value = _recipeUiState.value?.copy(
            recipe = recipe,
            heartIconStatus = heartIconStatus
        )

        _recipeUiState.value = RecipeUiState()

        // TODO: пункт 4 доделать
    }

    fun getFavorites(sharePrefs: SharedPreferences?): MutableSet<String> {
        return HashSet(sharePrefs?.getStringSet(Constants.FAVORITES_KEY, emptySet()))
    }

}