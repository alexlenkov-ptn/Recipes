package com.lnkov.recipes.model

import android.graphics.drawable.Drawable

data class RecipeUiState(
    val recipe: Recipe? = null,
    val heartIconStatus: Boolean = false,
    val drawable: Drawable? = null,
    val numberOfPortions: Int = 1,
)