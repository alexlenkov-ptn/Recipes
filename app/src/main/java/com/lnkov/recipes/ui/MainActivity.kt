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
import com.lnkov.recipes.model.Recipe
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityLearnWordBinding ust not be null")

    private var categories: List<Category> = listOf()
    private var categoriesIdList: List<Int> = listOf()

    private val threadPool: ExecutorService = Executors.newFixedThreadPool(Constants.THREAD_POOLS)

    private val navOption = navOptions {
        launchSingleTop = true
        anim {
            enter = android.R.anim.fade_in
            exit = android.R.anim.fade_out
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val thread = Thread {
            Log.d(
                "MainActivity", "Выполняю запрос на потоке: ${Thread.currentThread().name}"
            )
            Log.d(
                "MainActivity",
                "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}"
            )

            categories = loadCategories()
            Log.d("MainActivity", "Categories from Web: $categories")

            categoriesIdList = categories.map { it.id }
            Log.d("MainActivity", "categoriesIdList: $categoriesIdList")

            categoriesIdList.forEach {

                threadPool.execute() {
                    Log.d("MainActivity", "Recipe: ${loadRecipesList(it)}")
                    Log.d(
                        "MainActivity",
                        "forEach выполняется на потоке: ${Thread.currentThread().name}"
                    )

                }
            }
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

    private fun loadCategories(): List<Category> {
        val url = URL(Constants.URL_CATEGORIES)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.connect()
        val string = connection.inputStream.bufferedReader().readText()

        return Json.decodeFromString(string)
    }

    private fun loadRecipesList(categoryId: Int): List<Recipe> {
        val url = URL("https://recipes.androidsprint.ru/api/category/$categoryId/recipes")
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.connect()
        val string = connection.inputStream.bufferedReader().readText()
        val recipesList: List<Recipe> = Json.decodeFromString(string)

        return recipesList
    }
}