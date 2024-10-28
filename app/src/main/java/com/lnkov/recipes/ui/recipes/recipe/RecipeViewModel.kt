package com.lnkov.recipes.ui.recipes.recipe

import android.app.Application
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lnkov.recipes.data.Constants
import com.lnkov.recipes.data.STUB
import com.lnkov.recipes.model.Recipe
import com.lnkov.recipes.model.RecipeUiState

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val _recipeUiState = MutableLiveData<RecipeUiState>()


    init {
        Log.i("!!!", "RecipeViewModel created")
        _recipeUiState.value = RecipeUiState()
        updateHeartIconStatus(true)
        val recipe = loadRecipe(0) // todo recipeId получить из bundle

        val drawable: Drawable? = try {
            Drawable.createFromStream(
                application.assets?.open(recipe?.imageUrl ?: ""),
                null
            )
        } catch (e: Exception) {
            Log.d("!!!", "Image not found: ${recipe?.imageUrl}")
            null
        }

        _recipeUiState.value = _recipeUiState.value?.copy(
            recipe = recipe,
            drawable = drawable,
        )

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

    fun loadRecipe(recipeId: Int): Recipe? {
        return STUB.getRecipeById(0, recipeId)
    }

}