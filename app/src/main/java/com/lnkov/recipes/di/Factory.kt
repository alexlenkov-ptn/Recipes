package com.lnkov.recipes.di

interface Factory<T> {

    fun create(): T

}