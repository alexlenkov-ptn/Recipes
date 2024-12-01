package com.lnkov.recipes.data

import android.util.Log
import com.lnkov.recipes.RecipeApiService
import com.lnkov.recipes.di.IoDispatcher
import com.lnkov.recipes.model.Category
import com.lnkov.recipes.model.Recipe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val service: RecipeApiService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val recipeDao: RecipesDao,
    private val categoryDao: CategoriesDao,
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
        categoryDao.getAll()
    }

    suspend fun loadCategoriesToCache(categories: List<Category>) = withContext(dispatcher) {
        categoryDao.addCategory(categories)
    }

    suspend fun getCategoryById(categoryId: Int): Category = withContext(dispatcher) {
        categoryDao.getCategoryById(categoryId)
    }

    suspend fun getAllByCategoryId(categoryId: Int): List<Recipe> = withContext(dispatcher) {
        recipeDao.getAllByCategoryId(categoryId)
    }

    suspend fun getRecipeByRecipeId(recipeId: Int): Recipe = withContext(dispatcher) {
        recipeDao.getRecipeByRecipeId(recipeId)
    }

    suspend fun getFavoritesRecipes(): List<Recipe> = withContext(dispatcher) {
        recipeDao.getFavoritesRecipes()
    }

    suspend fun loadRecipesToCache(recipes: List<Recipe>?) = withContext(dispatcher) {
        recipeDao.addRecipes(recipes ?: emptyList())
    }

    suspend fun loadRecipeToCache(recipe: Recipe) = withContext(dispatcher) {
        recipeDao.addRecipe(recipe)
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