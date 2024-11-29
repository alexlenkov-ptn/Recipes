package com.lnkov.recipes.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.lnkov.recipes.RecipeApiService
import com.lnkov.recipes.data.AppDatabase
import com.lnkov.recipes.data.Constants
import com.lnkov.recipes.data.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class AppContainer(context: Context) {

    private val db = Room.databaseBuilder(
        context = context,
        klass = AppDatabase::class.java,
        name = Constants.DATABASE_NAME
    )
        .fallbackToDestructiveMigration()
        .build()

    private val contentType = "application/json".toMediaType()

    private val logging = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .client(client)
        .build()

    private val recipeApiService: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    private val ioDispatcher = Dispatchers.IO

    private val repository = RecipeRepository(
        appDatabase = db,
        service = recipeApiService,
        dispatcher = ioDispatcher,
    )

    val categoriesListViewModelFactory = CategoriesListViewModelFactory(repository)
    val favoritesViewModelFactory = FavoritesViewModelFactory(repository)
    val recipesViewModelFactory = RecipesListViewModelFactory(repository)
    val recipeViewModelFactory = RecipeViewModelFactory(repository)


}