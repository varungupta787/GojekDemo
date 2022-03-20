package com.gojek.demo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gojek.demo.data.model.RepoItem

@Dao
interface RepoItemDao {
    @Query("SELECT * FROM RepoItemEntity")
    fun getAllRepositoryItem(): List<RepoItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRepositoryItems(vararg repoItem: List<RepoItem>)

}