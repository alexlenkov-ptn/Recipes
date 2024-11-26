package com.lnkov.recipes.ui.recipes.recipe_list

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lnkov.recipes.model.Recipe

@Dao
interface RecipesDao {
    @Query("SELECT * FROM Recipe")
    fun getAll(): List<Recipe>

    @Insert
    fun addRecipes(recipes: List<Recipe>)

    @Query("DELETE FROM Recipe")
    fun deleteRecipes()
}