package com.gojek.demo.domain.usercase

import com.gojek.demo.data.NetworkConnectionUtil
import com.gojek.demo.data.NetworkUtils
import com.gojek.demo.data.local.database.RepositoryDatabase
import com.gojek.demo.data.model.RepoItem
import com.gojek.demo.di.AppModule
import com.gojek.demo.domain.RepositoryDataRepo
import com.gojek.demo.domain.models.ResponseResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepoDataUsecase  constructor(
    var dispatcher : CoroutineDispatcher = Dispatchers.IO,
    var repository: RepositoryDataRepo,
    var database: RepositoryDatabase,
) :
    BaseUseCase(dispatcher, repository, database) {

    @Inject lateinit var networkConnectionUtil : NetworkConnectionUtil

    suspend fun getRepoData(onSuccess: (t: List<RepoItem>) -> Unit, onError: (t: String?) -> Unit) {
        withContext(dispatcher) {

            if (networkConnectionUtil.isNetworkAvailable()) {
                val responseData = repository.getRepoData()
                when (responseData.status) {
                    ResponseResource.StatusType.SUCCESS -> {
                        val repoDataList = responseData.data as List<RepoItem>
                        onSuccess(repoDataList)
                        saveToDatabase(repoDataList)
                    }

                    ResponseResource.StatusType.ERROR -> {
                        onError(responseData.message)
                    }
                }
            } else {
                getRepoDataFromDatabase(onSuccess)
            }
        }
    }

    fun getRepoDataFromDatabase(onSuccess: (t: List<RepoItem>) -> Unit) {
        onSuccess(convertDataToRepoItemList(database.repoItemDao().getAllRepositoryItem()))
    }

}