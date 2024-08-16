package com.lnkov.recipes

data class Recipe(
    val id: Int,
    val title: String,
    val ingredients: ArrayList<Ingredient>,
    val method: String,
    val imageUrl: String,
)