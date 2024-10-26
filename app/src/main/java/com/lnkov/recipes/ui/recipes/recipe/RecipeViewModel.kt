package com.lnkov.recipes.ui.recipes.recipe

import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import com.lnkov.recipes.model.Recipe

class RecipeViewModel : ViewModel() {

    data class RecipeUiState(
        val recipe: Recipe? = null,
        val heartIconStatus: Boolean = false,
        val drawable: Drawable? = null,
        val numberOfPortions : Int? = null,
    )

}