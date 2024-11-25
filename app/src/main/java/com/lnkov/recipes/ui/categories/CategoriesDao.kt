package com.lnkov.recipes.ui.categories

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lnkov.recipes.model.Category

@Dao
interface CategoriesDao {

    @Query("SELECT * FROM Category")
    fun getAll(): List<Category>

    @Insert
    fun addCategory(vararg categories: Category)
}