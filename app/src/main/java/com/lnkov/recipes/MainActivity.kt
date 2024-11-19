package com.lnkov.recipes

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import com.lnkov.recipes.data.Constants
import com.lnkov.recipes.databinding.ActivityMainBinding
import com.lnkov.recipes.model.Category
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


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

//        val thread = Thread {
//            Log.d(
//                "MainActivity", "Выполняю запрос на потоке: ${Thread.currentThread().name}"
//            )
//            Log.d(
//                "MainActivity",
//                "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}"
//            )
//
//            try {
//                categories = loadCategories()
//                categoriesIdList = categories.map { it.id }
//
//                Log.d("MainActivity", "Categories from Web: $categories")
//                Log.d("MainActivity", "categoriesIdList: $categoriesIdList")
//
//                categoriesIdList.forEach {
//
//                    threadPool.execute() {
//                        Log.d("MainActivity", "Recipe: ${loadRecipesList(it)}")
//
//                        Log.d(
//                            "MainActivity",
//                            "forEach выполняется на потоке: ${Thread.currentThread().name}"
//                        )
//                    }
//                }
//            } catch (e: Exception) {
//                Log.d(
//                    "MainActivity",
//                    "Exception: $e. Thread: ${Thread.currentThread().name}"
//                )
//            }
//        }.start()

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