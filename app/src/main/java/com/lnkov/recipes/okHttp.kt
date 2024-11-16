package com.lnkov.recipes

import androidx.annotation.RequiresOptIn
import com.lnkov.recipes.data.Constants
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.logging.Level

fun main() {
    val logging: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val request = Request.Builder()
        .url(Constants.URL_CATEGORIES)
        .build()

    client.newCall(request).execute().use { response ->
        println(response.body?.string())
    }

}