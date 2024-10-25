package com.lnkov.recipes.ui.recipes.recipe

import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import com.lnkov.recipes.model.Recipe

class ViewModelRecipe : ViewModel() {

    data class RecipeUiState(
        val sharePrefs: SharedPreferences? = null,
        var recipe: Recipe? = null,
        var heartIconStatus: Boolean,
        val favoritesSet: MutableSet<String> = mutableSetOf(),
        val recipeIdString: String? = recipe?.id.toString(),
        val drawable: Drawable? = null
    )

}