package com.gojek.demo.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gojek.demo.data.local.dao.RepoItemDao
import com.gojek.demo.data.local.entity.RepoItemEntity

@Database(entities = [RepoItemEntity::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverterHelper::class)
abstract class RepositoryDatabase : RoomDatabase() {
    abstract fun repoItemDao(): RepoItemDao
}