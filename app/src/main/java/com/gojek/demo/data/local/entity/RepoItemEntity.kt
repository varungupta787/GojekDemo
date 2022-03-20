package com.gojek.demo.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gojek.demo.data.model.Owner


@Entity
data class RepoItemEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "forks_count") val forks_count: Int?,
    @ColumnInfo(name = "language") val language: String?,
    @ColumnInfo(name = "watchers_count") val watchers_count: String?,
    @ColumnInfo(name = "owner") val owner: Owner?
)