package com.lnkov.recipes.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lnkov.recipes.model.Recipe

@Dao
interface RecipesDao {
    @Query("SELECT * FROM Recipe")
    fun getAll(): List<Recipe>

    @Query("SELECT * FROM Recipe WHERE categoryId == :categoryId")
    fun getAllByCategoryId(categoryId: Int): List<Recipe>

    @Query("SELECT * FROM Recipe WHERE id == :recipeId")
    fun getRecipeByRecipeId(recipeId: Int): Recipe

    @Query("SELECT * FROM Recipe WHERE isFavorite == 1")
    fun getFavoritesRecipes(): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRecipe(recipe: Recipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRecipes(recipes: List<Recipe>)

    @Query("DELETE FROM Recipe")
    fun deleteRecipes()
}