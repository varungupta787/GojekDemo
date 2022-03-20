package com.gojek.demo.domain.usercase

import com.gojek.demo.data.local.database.RepositoryDatabase
import com.gojek.demo.data.model.NetworkResponseWrapper
import com.gojek.demo.data.model.RepoItem
import com.gojek.demo.domain.RepositoryDataRepo
import com.gojek.demo.domain.models.ResponseResource
import kotlinx.coroutines.*

open class BaseUseCase(var databaseDispatcher:CoroutineDispatcher,
                       var repo: RepositoryDataRepo,
                  var db: RepositoryDatabase
) {

    fun saveToDatabase(repoDataList : List<RepoItem>) {
       CoroutineScope(databaseDispatcher).launch{
           db.repoItemDao().insertAllRepositoryItems(repoDataList)
       }
   }
}