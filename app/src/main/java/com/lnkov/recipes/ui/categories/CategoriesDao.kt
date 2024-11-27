package com.lnkov.recipes.ui.categories

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lnkov.recipes.model.Category

@Dao
interface CategoriesDao {

    @Query("SELECT * FROM Category")
    fun getAll(): List<Category>

    @Query("SELECT * FROM Category WHERE id == :categoryId")
    fun getCategoryById(categoryId: Int): Category

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategory(categories: List<Category>)
}