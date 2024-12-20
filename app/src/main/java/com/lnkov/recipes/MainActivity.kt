package com.lnkov.recipes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import com.lnkov.recipes.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityLearnWordBinding ust not be null")

    private val navOption = navOptions {
        launchSingleTop = true
        anim {
            enter = android.R.anim.fade_in
            exit = android.R.anim.fade_out
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCategory.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(
                R.id.categoriesListFragment, null, navOption
            )
        }

        binding.buttonFavorites.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(
                R.id.favoritesFragment, null, navOption
            )
        }
    }
}