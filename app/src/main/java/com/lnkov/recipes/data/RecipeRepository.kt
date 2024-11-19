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
            val categoriesCall: Call<List<Category>> = service.getCategories()
            val categoriesResponse: Response<List<Category>> =
                categoriesCall.execute()

            return categoriesResponse.body()
        } catch (e: Exception) {
            Log.d("RecipeRepository", "Error: $e")

            return null
        }
    }

    fun loadRecipesById(categoryId: Int): List<Recipe>? {
        try {
            val recipesCall: Call<List<Recipe>> = service.getRecipesById(categoryId)
            val recipesResponse: Response<List<Recipe>> = recipesCall.execute()

            return recipesResponse.body()
        } catch (e: Exception) {
            Log.d("RecipeRepository", "Error: $e")

            return null
        }
    }

    fun loadCategoryById(categoryId: Int): Category? {
        try {
            val categoryCall: Call<Category> = service.getCategoryById(categoryId)
            val categoryResponse: Response<Category> = categoryCall.execute()

            return categoryResponse.body()
        } catch (e: Exception) {
            Log.d("RecipeRepository", "Error: $e")

            return null
        }
    }

    fun loadRecipeById(recipeId: Int): Recipe? {
        try {
            val recipeCall: Call<Recipe> = service.getRecipeById(recipeId)
            val recipeResponse: Response<Recipe> = recipeCall.execute()

            return recipeResponse.body()
        } catch (e: Exception) {
            Log.d("RecipeRepository", "Error: $e")

            return null
        }
    }

    fun loadRecipeById(recipeIds: String): List<Recipe>? {
        try {
            val recipesCall: Call<List<Recipe>> = service.getRecipesByIds(recipeIds)
            val recipesResponse: Response<List<Recipe>> = recipesCall.execute()

            return recipesResponse.body()
        } catch (e: Exception) {
            Log.d("RecipeRepository", "Error: $e")

            return null
        }
    }


}