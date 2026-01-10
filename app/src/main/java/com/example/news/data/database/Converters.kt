package com.example.news.data.database

import androidx.room.TypeConverter
import com.example.news.model.Source

class Converters {
    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name // We only really need the name for display
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(id = name, name = name)
    }
}