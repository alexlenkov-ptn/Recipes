package com.lnkov.recipes.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.lnkov.recipes.RecipeApiService
import com.lnkov.recipes.model.Category
import com.lnkov.recipes.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

class RecipeRepository(context: Context) {

    private val contentType = "application/json".toMediaType()
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
    private val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)
    private val dispatcher = Dispatchers.IO

    private val db = Room.databaseBuilder(
        context = context,
        klass = AppDatabase::class.java,
        name = "database"
    ).build()

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
        db.categoryDao().getAll()
    }

    suspend fun loadCategoriesToCache(categories: List<Category>) = withContext(dispatcher) {
        db.categoryDao().addCategory(categories)
    }

    suspend fun getCategoryById(categoryId : Int) : Category = withContext(dispatcher) {
        db.categoryDao().getCategoryById(categoryId)
    }

    suspend fun getRecipesFromCache() : List<Recipe> = withContext(dispatcher) {
        db.recipeDao().getAll()
    }

    suspend fun loadRecipesToCache(recipes: List<Recipe>?) = withContext(dispatcher) {
        recipes?.let { db.recipeDao().addRecipes(it) }
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

    suspend fun loadCategoryById(categoryId: Int): Category? = withContext(dispatcher) {

        try {
            val call: Call<Category> = service.getCategoryById(categoryId)
            val response: Response<Category> = call.execute()

            response.body()
        } catch (e: Exception) {
            Log.d("RecipeRepository", "Error: $e")

            null
        }

    }

    suspend fun loadRecipeById(recipeId: Int): Recipe? = withContext(dispatcher) {

        try {
            val call: Call<Recipe> = service.getRecipeById(recipeId)
            val response: Response<Recipe> = call.execute()

            response.body()
        } catch (e: Exception) {
            Log.d("RecipeRepository", "Error: $e")

            null

        }
    }

    suspend fun loadRecipesByIds(recipeIds: String): List<Recipe>? = withContext(dispatcher) {

        Log.d("RecipeRepository", "recipeIds: $recipeIds")

        try {
            val call: Call<List<Recipe>> = service.getRecipesByIds(recipeIds)
            val response: Response<List<Recipe>> = call.execute()
            Log.d("RecipeRepository", "loadRecipesByIds: ${response.body()}")

            if (response.body() == null) emptyList()
            else response.body()
        } catch (e: Exception) {
            Log.d("RecipeRepository", "Error: $e")

            null
        }
    }
}