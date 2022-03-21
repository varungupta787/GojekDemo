package com.gojek.demo.domain.usercase

import com.gojek.demo.data.NetworkUtils
import com.gojek.demo.data.local.database.RepositoryDatabase
import com.gojek.demo.data.model.RepoItem
import com.gojek.demo.domain.RepositoryDataRepo
import com.gojek.demo.domain.models.ResponseResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepoDataUsecase (
    var dispatcher : CoroutineDispatcher = Dispatchers.IO,
    var repository: RepositoryDataRepo,
    var database: RepositoryDatabase,
) :
    BaseUseCase(dispatcher, repository, database) {

    suspend fun getRepoData(onSuccess: (t: List<RepoItem>) -> Unit, onError: (t: String?) -> Unit) {
        withContext(dispatcher) {
            if (NetworkUtils.isNetworkAvailable()) {
                val responseData = repository.getRepoData()
                when (responseData.status) {
                    ResponseResource.StatusType.SUCCESS -> {
                        val repoDataList = responseData.data as List<RepoItem>
                        onSuccess(responseData.data as List<RepoItem>)
                        saveToDatabase(repoDataList)
                    }

                    ResponseResource.StatusType.ERROR -> {
                        onError(responseData.message)
                    }
                }
            } else {
                onSuccess(database.repoItemDao().getAllRepositoryItem())
            }
        }
    }

}