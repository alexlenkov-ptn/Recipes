package com.lnkov.recipes.ui.recipes.recipe_list

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lnkov.recipes.model.Category
import com.lnkov.recipes.model.Recipe

@Dao
interface RecipesDao {
    @Query("SELECT * FROM Recipe")
    fun getAll(): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRecipes(recipes: List<Recipe>)
}