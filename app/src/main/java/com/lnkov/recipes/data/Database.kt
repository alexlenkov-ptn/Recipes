package com.lnkov.recipes.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lnkov.recipes.Converters
import com.lnkov.recipes.model.Category
import com.lnkov.recipes.model.Recipe
import com.lnkov.recipes.ui.categories.CategoriesDao
import com.lnkov.recipes.ui.recipes.recipe_list.RecipesDao

@Database(entities = [Category::class, Recipe::class], version = 3)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoriesDao

    abstract fun recipeDao(): RecipesDao

}