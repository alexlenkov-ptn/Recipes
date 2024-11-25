package com.lnkov.recipes.ui.recipes.recipe_list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lnkov.recipes.data.Constants
import com.lnkov.recipes.data.RecipeRepository
import com.lnkov.recipes.model.Category
import com.lnkov.recipes.model.Recipe
import kotlinx.coroutines.launch
import okio.IOException

class RecipesListViewModel(
    application: Application
) : AndroidViewModel(application) {

    init {
        Log.i("RecipeListViewModel", "RecipeListViewModel created")
    }

    private val _recipeListUiState = MutableLiveData<RecipeListUiState>(RecipeListUiState())

    val recipeListUiState: LiveData<RecipeListUiState>
        get() = _recipeListUiState

    data class RecipeListUiState(
        val category: Category? = null,
        val drawableUrl: String? = "",
        val recipeList: List<Recipe>? = emptyList(),
    )

    private val recipeRepository = RecipeRepository(application)

    fun loadCategory(categoryId: Int?) {

        viewModelScope.launch {

            val category = categoryId?.let { recipeRepository.loadCategoryById(it) }
            val recipeList = categoryId?.let { recipeRepository.loadRecipesById(it) }

            _recipeListUiState.postValue(
                recipeListUiState.value?.copy(
                    category = category,
                    drawableUrl = "${Constants.BASE_IMAGE_URL}${category?.imageUrl}",
                    recipeList = recipeList
                )
            )

        }
    }
}