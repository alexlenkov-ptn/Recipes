package com.lnkov.recipes.data

import android.util.Log
import com.lnkov.recipes.RecipeApiService
import com.lnkov.recipes.model.Category
import com.lnkov.recipes.model.Recipe
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class RecipeRepository(
    private val appDatabase: AppDatabase,
    private val service: RecipeApiService,
    private val dispatcher: CoroutineContext,
) {

    suspend fun loadCategories(): List<Category>? = withContext(dispatcher) {

        try {
            val call: Call<List<Category>> = service.getCategories()
            val response: Response<List<Category>> = call.execute()

            response.body()
        } catch (e: Exception) {
            Log.d("RecipeRepository", "Error: $e")

            null
        }
    }

    suspend fun getCategoriesFromCache(): List<Category> = withContext(dispatcher) {
        appDatabase.categoryDao().getAll()
    }

    suspend fun loadCategoriesToCache(categories: List<Category>) = withContext(dispatcher) {
        appDatabase.categoryDao().addCategory(categories)
    }

    suspend fun getCategoryById(categoryId: Int): Category = withContext(dispatcher) {
        appDatabase.categoryDao().getCategoryById(categoryId)
    }

    suspend fun getAllByCategoryId(categoryId: Int): List<Recipe> = withContext(dispatcher) {
        appDatabase.recipeDao().getAllByCategoryId(categoryId)
    }

    suspend fun getRecipeByRecipeId(recipeId: Int): Recipe = withContext(dispatcher) {
        appDatabase.recipeDao().getRecipeByRecipeId(recipeId)
    }

    suspend fun getFavoritesRecipes(): List<Recipe> = withContext(dispatcher) {
        appDatabase.recipeDao().getFavoritesRecipes()
    }

    suspend fun loadRecipesToCache(recipes: List<Recipe>?) = withContext(dispatcher) {
        recipes?.let { appDatabase.recipeDao().addRecipes(it) }
    }

    suspend fun loadRecipeToCache(recipe: Recipe) = withContext(dispatcher) {
        appDatabase.recipeDao().addRecipe(recipe)
    }

    suspend fun loadRecipesById(categoryId: Int): List<Recipe>? = withContext(dispatcher) {

        try {
            val call: Call<List<Recipe>> = service.getRecipesById(categoryId)
            val response: Response<List<Recipe>> = call.execute()

            response.body()
        } catch (e: Exception) {
            Log.d("RecipeRepository", "Error: $e")

            null
        }
    }
}