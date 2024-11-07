package com.lnkov.recipes.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lnkov.recipes.databinding.ActivityMainBinding
import androidx.navigation.findNavController
import com.lnkov.recipes.R


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityLearnWordBinding ust not be null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.buttonCategory.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.recipesListFragment)
        }

        binding.buttonFavorites.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.favoritesFragment)
        }

    }
}