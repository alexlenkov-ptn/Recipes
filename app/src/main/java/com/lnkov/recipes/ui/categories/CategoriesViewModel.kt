package com.lnkov.recipes.ui.categories

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lnkov.recipes.data.RecipeRepository
import com.lnkov.recipes.model.Category
import kotlinx.coroutines.launch

class CategoriesViewModel(application: Application) :
    AndroidViewModel(application) {

    init {
        Log.i("CategoriesViewModel", "CategoriesViewModel created")
    }

    private val recipeRepository = RecipeRepository(application)

    data class CategoriesUiState(
        val categories: List<Category>? = emptyList(),
    )

    private val _categoryUiState = MutableLiveData<CategoriesUiState>(CategoriesUiState())

    val categoryUiState: LiveData<CategoriesUiState>
        get() = _categoryUiState

    fun loadCategories() {
        Log.d("CategoriesViewModel", "fun loadCategories()")

        viewModelScope.launch {
            recipeRepository.apply {
                var categories: List<Category> = getCategoriesFromCache()

                if (categories.isEmpty()) {
                    categories = loadCategories() ?: emptyList()
                    loadCategoriesToCache(categories)
                }

                _categoryUiState.postValue(
                    categoryUiState.value?.copy(categories = categories)
                )

                Log.d("CategoriesViewModel", "$categories")
            }
        }

    }

}