package com.lnkov.recipes

import androidx.room.TypeConverter
import com.lnkov.recipes.model.Ingredient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    @TypeConverter
    fun fromIngredientsList(ingredients : List<Ingredient>): String {
        return Json.encodeToString(ingredients)
    }

    @TypeConverter
    fun toIngredientsList(str : String) : List<Ingredient> {
        return Json.decodeFromString(str)
    }

//    @TypeConverter
//    fun fromListString(strList : List<String>): String {
//        return strList.joinToString(separator = ",")
//    }
//
//    @TypeConverter
//    fun toListString(str : String) : List<String> {
//        return str.split(",")
//    }
}