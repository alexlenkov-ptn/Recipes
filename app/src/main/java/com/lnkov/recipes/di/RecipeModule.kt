package com.lnkov.recipes.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.lnkov.recipes.RecipeApiService
import com.lnkov.recipes.data.AppDatabase
import com.lnkov.recipes.data.CategoriesDao
import com.lnkov.recipes.data.Constants
import com.lnkov.recipes.data.RecipesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Module
@InstallIn(SingletonComponent::class)
class RecipeModule {

    @Singleton
    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = Constants.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideCategoriesDao(appDatabase: AppDatabase): CategoriesDao = appDatabase.categoryDao()

    @Singleton
    @Provides
    fun provideRecipesDao(appDatabase: AppDatabase): RecipesDao = appDatabase.recipeDao()

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()

        return retrofit
    }

    @Singleton
    @Provides
    fun recipeApiService(retrofit: Retrofit): RecipeApiService {
        return retrofit.create(RecipeApiService::class.java)
    }

}