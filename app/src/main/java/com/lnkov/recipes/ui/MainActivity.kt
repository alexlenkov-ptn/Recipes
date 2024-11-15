package com.lnkov.recipes.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.lnkov.recipes.databinding.ActivityMainBinding
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import com.lnkov.recipes.R
import com.lnkov.recipes.data.Constants
import com.lnkov.recipes.model.Category
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityLearnWordBinding ust not be null")

    var categories: List<Category> = listOf()

    private val navOption = navOptions {
        launchSingleTop = true
        anim {
            enter = android.R.anim.fade_in
            exit = android.R.anim.fade_out
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var string: String? = null
        val thread = Thread {
            Log.d(
                "MainActivity", "Выполняю запрос на потоке: ${Thread.currentThread().name}"
            )
            Log.d(
                "MainActivity",
                "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}"
            )

            val url = URL(Constants.URL_CATEGORIES)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.connect()
            string = connection.inputStream.bufferedReader().readText()

            categories = Json.decodeFromString(string.toString())
            Log.d("MainActivity", "Categories from Web: $categories")

        }.start()

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