package com.lnkov.recipes.ui.categories

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lnkov.recipes.MyApplication
import com.lnkov.recipes.data.RecipeRepository
import com.lnkov.recipes.model.Category

class CategoriesViewModel(application: Application) :
    AndroidViewModel(application) {

    init {
        Log.i("CategoriesViewModel", "CategoriesViewModel created")
    }

    private val threadPool = (application as MyApplication).threadPool

    private val recipeRepository = RecipeRepository()

    data class CategoriesUiState(
        val categories: List<Category>? = emptyList(),
    )

    private val _categoryUiState = MutableLiveData<CategoriesUiState>(CategoriesUiState())

    val categoryUiState: LiveData<CategoriesUiState>
        get() = _categoryUiState

    fun loadCategories() {

        Log.d("CategoriesViewModel", "fun loadCategories()")

        threadPool.execute() {
            val categories = recipeRepository.loadCategories()

            _categoryUiState.postValue(
                categoryUiState.value?.copy(categories = categories)
            )

            Log.d("CategoriesViewModel", "$categories")
            Log.d("CategoriesViewModel", "Выполнение потока: ${Thread.currentThread().name}")
        }
    }

}