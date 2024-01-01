package com.arincdogansener.finalrecipeapp.entities.converter

import androidx.room.TypeConverter
import com.arincdogansener.finalrecipeapp.entities.Category
import com.arincdogansener.finalrecipeapp.entities.MealsItems
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MealListConverter {
    @TypeConverter
    fun fromCategoryList(category: List<MealsItems>):String? {
        if(category == null){
            return null
        }
        else{
            val gson = Gson()
            val type = object : TypeToken<MealsItems>(){

            }.type
            return gson.toJson(category, type)
        }
    }

    @TypeConverter
    fun toCategoryList(categoryString: String):List<MealsItems>?{
        if (categoryString == null){
            return null
        }
        else{
            val gson = Gson()
            val type = object:TypeToken<MealsItems>(){

            }.type
            return gson.fromJson(categoryString, type)
        }
    }
}