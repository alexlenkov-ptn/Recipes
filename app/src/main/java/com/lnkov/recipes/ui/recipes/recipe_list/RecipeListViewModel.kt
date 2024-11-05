package com.lnkov.recipes.ui.recipes.recipe_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lnkov.recipes.data.STUB
import com.lnkov.recipes.model.Category

class RecipeListViewModel : ViewModel() {

    init {
        Log.i("!!!", "RecipeListViewModel created")
    }

    private val _recipeListUiState = MutableLiveData<RecipeListUiState>(RecipeListUiState())

    val recipeListUiState: LiveData<RecipeListUiState>
        get() = _recipeListUiState

    data class RecipeListUiState(
        val category: Category? = null,
    )

    fun loadCategory(categoryId: Int?) {
        val category = categoryId?.let { STUB.getCategoryById(categoryId) }

        _recipeListUiState.value = recipeListUiState.value?.copy(
            category = category,
        )
    }


}