package com.lnkov.recipes

import com.lnkov.recipes.model.Category
import com.lnkov.recipes.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface RecipeApiService {

    @GET("category")
    fun getCategories(): Call<List<Category>>

    @GET("category/{id}/recipes")
    fun getRecipesById(@Path("id") id: Int): Call<List<Recipe>>
}