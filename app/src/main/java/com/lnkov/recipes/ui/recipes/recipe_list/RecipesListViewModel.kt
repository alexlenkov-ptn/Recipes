package com.lnkov.recipes.ui.recipes.recipe_list

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lnkov.recipes.MyApplication
import com.lnkov.recipes.data.RecipeRepository
import com.lnkov.recipes.model.Category
import com.lnkov.recipes.model.Recipe

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
        val drawable: Drawable? = null,
        val recipeList: List<Recipe>? = emptyList(),
    )

    private val recipeRepository = RecipeRepository()

    private val threadPool = (application as MyApplication).threadPool

    fun loadCategory(categoryId: Int?) {


        threadPool.execute {
            val category = categoryId?.let { recipeRepository.loadCategoryById(it) }
            val recipeList = categoryId?.let { recipeRepository.loadRecipesById(it) }

            _recipeListUiState.postValue(
                recipeListUiState.value?.copy(
                    category = category,
                    drawable = getDrawable(category),
                    recipeList = recipeList
                )
            )
        }
    }

    private fun getDrawable(category: Category?): Drawable? {
        val drawableUrl = category?.imageUrl
        return try {
            Drawable.createFromStream(
                getApplication<Application>().assets.open(drawableUrl ?: ""),
                null
            )
        } catch (e: Exception) {
            Log.d("!!!", "Image not found: $drawableUrl")
            null
        }
    }


}