package com.lnkov.recipes.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lnkov.recipes.Converters
import com.lnkov.recipes.model.Category
import com.lnkov.recipes.model.Recipe

@Database(entities = [Category::class, Recipe::class], version = 4)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoriesDao

    abstract fun recipeDao(): RecipesDao

}