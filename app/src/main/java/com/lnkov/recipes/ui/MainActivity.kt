package com.lnkov.recipes.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.lnkov.recipes.R
import com.lnkov.recipes.RecipeApiService
import com.lnkov.recipes.data.Constants
import com.lnkov.recipes.databinding.ActivityMainBinding
import com.lnkov.recipes.model.Category
import com.lnkov.recipes.model.Recipe
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
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

    private val logging: HttpLoggingInterceptor = HttpLoggingInterceptor() { message ->
        Log.d("MainActivity", message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

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

            try {
                categories = loadCategories()
                categoriesIdList = categories.map { it.id }

                Log.d("MainActivity", "Categories from Web: $categories")
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
            } catch (e: Exception) {
                Log.d(
                    "MainActivity",
                    "Exception: $e. Thread: ${Thread.currentThread().name}"
                )
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
        val contentType = "application/json".toMediaType()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()

        val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

        val categoriesCall: Call<List<Category>> = service.getCategories()

        val categoriesResponse: Response<List<Category>> =
            categoriesCall.execute()

        return categoriesResponse.body() ?: listOf()
    }

    private fun loadRecipesList(categoryId: Int): List<Recipe> {
        val contentType = "application/json".toMediaType()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()

        val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

        val recipesCall: Call<List<Recipe>> = service.getRecipesById(categoryId)

        val recipesResponse: Response<List<Recipe>> = recipesCall.execute()

        return recipesResponse.body() ?: listOf()

    }
}