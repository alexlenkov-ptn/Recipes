package com.lnkov.recipes.data

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.lnkov.recipes.RecipeApiService
import com.lnkov.recipes.model.Category
import com.lnkov.recipes.model.Recipe
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

class RecipeRepository {

    private val contentType = "application/json".toMediaType()
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
    private val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    fun loadCategories(): List<Category>? {
        try {
            val call: Call<List<Category>> = service.getCategories()
            val response: Response<List<Category>> = call.execute()

            return response.body()
        } catch (e: Exception) {
            Log.d("RecipeRepository", "Error: $e")

            return null
        }
    }

    fun loadRecipesById(categoryId: Int): List<Recipe>? {
        try {
            val call: Call<List<Recipe>> = service.getRecipesById(categoryId)
            val response: Response<List<Recipe>> = call.execute()

            return response.body()
        } catch (e: Exception) {
            Log.d("RecipeRepository", "Error: $e")

            return null
        }
    }

    fun loadCategoryById(categoryId: Int): Category? {
        try {
            val call: Call<Category> = service.getCategoryById(categoryId)
            val response: Response<Category> = call.execute()

            return response.body()
        } catch (e: Exception) {
            Log.d("RecipeRepository", "Error: $e")

            return null
        }
    }

    fun loadRecipeById(recipeId: Int): Recipe? {
        try {
            val call: Call<Recipe> = service.getRecipeById(recipeId)
            val response: Response<Recipe> = call.execute()

            return response.body()
        } catch (e: Exception) {
            Log.d("RecipeRepository", "Error: $e")

            return null
        }
    }

    fun loadRecipesByIds(recipeIds: String): List<Recipe>? {
        try {
            val call: Call<List<Recipe>> = service.getRecipesByIds(recipeIds)
            val response: Response<List<Recipe>> = call.execute()
            Log.d("RecipeRepository", "${response.body()}")

            return response.body()
        } catch (e: Exception) {
            Log.d("RecipeRepository", "Error: $e")

            return null
        }
    }


}