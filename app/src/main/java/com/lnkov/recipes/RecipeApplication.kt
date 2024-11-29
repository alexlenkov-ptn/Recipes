package com.lnkov.recipes

import android.app.Application
import com.lnkov.recipes.di.AppContainer

class RecipeApplication : Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()

        appContainer = AppContainer(this)
    }

}