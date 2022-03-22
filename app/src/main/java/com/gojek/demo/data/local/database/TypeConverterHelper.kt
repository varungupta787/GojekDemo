package com.gojek.demo.data.local.database

import androidx.room.TypeConverter
import com.gojek.demo.data.model.Owner

class TypeConverterHelper {

    @TypeConverter
    fun fromOwner(value: String?): Owner? {
        return value?.let { Owner(it) }
    }

    @TypeConverter
    fun toOwner(owner: Owner?): String? {
        return owner?.login
    }
}