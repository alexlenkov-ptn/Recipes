package com.lnkov.recipes

import android.app.Application
import com.lnkov.recipes.data.Constants
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MyApplication : Application() {
    val threadPool: ExecutorService = Executors.newFixedThreadPool(Constants.THREAD_POOLS)
}