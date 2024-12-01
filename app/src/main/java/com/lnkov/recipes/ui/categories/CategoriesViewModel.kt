package com.lnkov.recipes.ui.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lnkov.recipes.data.RecipeRepository
import com.lnkov.recipes.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
) : ViewModel() {

    init {
        Log.i("CategoriesViewModel", "CategoriesViewModel created")
    }

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