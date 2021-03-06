package com.gojek.demo.domain.usercase

import com.gojek.demo.data.local.database.RepositoryDatabase
import com.gojek.demo.data.local.entity.RepoItemEntity
import com.gojek.demo.data.model.NetworkResponseWrapper
import com.gojek.demo.data.model.RepoItem
import com.gojek.demo.di.AppModule
import com.gojek.demo.domain.RepositoryDataRepo
import com.gojek.demo.domain.models.ResponseResource
import kotlinx.coroutines.*
import javax.inject.Inject

open class BaseUseCase constructor(
    var databaseDispatcher: CoroutineDispatcher,
    var repo: RepositoryDataRepo,
    var db: RepositoryDatabase
) {

    suspend fun saveToDatabase(repoDataList: List<RepoItem>) {
        withContext(databaseDispatcher) { deleteAllPreviousData() }
        withContext(databaseDispatcher) { saveNewData(repoDataList) }
    }

    suspend fun deleteAllPreviousData() {
        db.repoItemDao().deleteAllRepositoryItems()
    }

    suspend fun saveNewData(repoDataList: List<RepoItem>) {
        db.repoItemDao().insertAllRepositoryItems(convertDataToRepoEntityList(repoDataList))
    }

    suspend fun convertDataToRepoEntityList(repoDataList: List<RepoItem>): List<RepoItemEntity> {
        val entityList: ArrayList<RepoItemEntity> = ArrayList()
        repoDataList.forEach { it ->
            entityList.add(
                RepoItemEntity(
                    it.id,
                    it.forks_count,
                    it.language,
                    it.watchers_count,
                    it.html_url,
                    it.forks_url,
                    it.description,
                    it.owner
                )
            )
        }
        return entityList
    }

    fun convertDataToRepoItemList(repoEntityList: List<RepoItemEntity>): List<RepoItem> {
        val repoList: ArrayList<RepoItem> = ArrayList()
        repoEntityList.forEach { it ->
            repoList.add(
                RepoItem(
                    it.id,
                    it.forks_count,
                    it.language,
                    it.watchers_count,
                    it.html_url,
                    it.forks_url,
                    it.description,
                    it.owner
                )
            )
        }
        return repoList
    }
}