package com.gojek.demo.data.local.database

import androidx.room.TypeConverter
import com.gojek.demo.data.model.Owner
import com.google.gson.Gson


class TypeConverterHelper {

    @TypeConverter
    fun fromOwner(value: String?): Owner? {
        return value?.run {
            val gson = Gson()
            return gson.fromJson(value, Owner::class.java)
        }
    }

    @TypeConverter
    fun toOwner(owner: Owner?): String? {
        return owner?.run {
            val gson = Gson()
            gson.toJson(owner, Owner::class.java)
        }
    }
}