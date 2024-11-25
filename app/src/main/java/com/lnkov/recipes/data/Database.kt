package com.lnkov.recipes.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lnkov.recipes.model.Category
import com.lnkov.recipes.ui.categories.CategoriesDao

@Database(entities = [Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoriesDao



}