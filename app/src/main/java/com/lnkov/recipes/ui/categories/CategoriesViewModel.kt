package com.lnkov.recipes.ui.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lnkov.recipes.data.STUB
import com.lnkov.recipes.model.Category

class CategoriesViewModel(application: Application) :
    AndroidViewModel(application) {

    data class CategoriesUiState(
        val categories: List<Category> = emptyList(),
    )

    private val _categoryUiState = MutableLiveData<CategoriesUiState>(CategoriesUiState())

    val categoryUiState: LiveData<CategoriesUiState>
        get() = _categoryUiState

    fun loadCategories() {
        _categoryUiState.value = categoryUiState.value?.copy(categories = STUB.getCategories())
    }

//    fun getCategory(categoryId: Int) : Category? = STUB.getCategoryById(categoryId)

}