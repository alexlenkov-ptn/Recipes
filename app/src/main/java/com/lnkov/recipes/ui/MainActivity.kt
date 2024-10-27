package com.lnkov.recipes.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.lnkov.recipes.databinding.ActivityMainBinding
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import com.lnkov.recipes.R
import com.lnkov.recipes.ui.recipes.favorites.FavoritesFragment
import com.lnkov.recipes.ui.recipes.recipe.RecipeViewModel


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityLearnWordBinding ust not be null")

    private val vmRecipe: RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vmRecipe.recipeUiState.observe(this, Observer {})

        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.buttonCategory.setOnClickListener {
            supportFragmentManager.commit {
                replace<CategoriesListFragment>(R.id.mainContainer)
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }

        binding.buttonFavorites.setOnClickListener {
            supportFragmentManager.commit {
                replace<FavoritesFragment>(R.id.mainContainer)
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }

    }
}