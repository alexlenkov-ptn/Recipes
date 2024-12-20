package com.lnkov.recipes.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lnkov.recipes.model.Recipe
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lnkov.recipes.data.Constants
import com.lnkov.recipes.data.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
) : ViewModel() {

    init {
        Log.i("RecipeViewModel", "RecipeViewModel created")
    }

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

    fun onFavoritesClicked(recipeId: Int?) {
        Log.d("RecipeViewModel", "recipe id: $recipeId")

        viewModelScope.launch {
            val recipe = recipeId?.let { recipeRepository.getRecipeByRecipeId(recipeId) }

            Log.d("RecipeViewModel", "$recipe")

            if (recipe?.isFavorite == false) recipe.isFavorite = true
            else recipe?.isFavorite = false

            Log.d("RecipeViewModel", "recipe isFavorite: ${recipe?.isFavorite}")

            _recipeUiState.value =
                recipeUiState.value?.copy(isFavorite = recipe?.isFavorite ?: false)

            recipe?.let { recipeRepository.loadRecipeToCache(it) }

            Log.d("RecipeViewModel", "favorite state: ${_recipeUiState.value?.isFavorite}")
        }
    }

    fun updateNumberOfPortions(count: Int) {
        _recipeUiState.value = recipeUiState.value?.copy(portionsCount = count)
        Log.d("!!!", "updateNumberOfPortions: ${recipeUiState.value?.portionsCount}")
    }

    fun loadRecipe(recipeId: Int?) {

        viewModelScope.launch {
            val recipe = recipeId?.let { recipeRepository.getRecipeByRecipeId(recipeId) }

            var isLoaded = false

            if (recipe != null) {
                isLoaded = true
            }

            _recipeUiState.postValue(
                recipe?.isFavorite?.let {
                    recipeUiState.value?.copy(
                        recipe = recipe,
                        isFavorite = it,
                        drawableUrl = "${Constants.BASE_IMAGE_URL}${recipe.imageUrl}",
                        isLoaded = isLoaded
                    )
                }
            )

        }
    }

}