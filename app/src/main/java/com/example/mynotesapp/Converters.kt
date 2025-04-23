package com.example.mynotesapp

import androidx.room.TypeConverter
import com.example.mynotesapp.data.TaskEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converters {
    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromTaskEntryList(value: List<TaskEntry>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toTaskEntryList(value: String): List<TaskEntry> {
        val listType = object : TypeToken<List<TaskEntry>>() {}.type
        return Gson().fromJson(value, listType)
    }
}