package com.gojek.demo.data.repositories

import com.gojek.demo.data.model.RepoItem
import com.gojek.demo.data.remote.ApiService
import com.gojek.demo.domain.RepositoryDataRepo
import com.gojek.demo.domain.models.ResponseResource
import com.gojek.demo.domain.repository.BaseRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class RepositoryRepoDataImpl constructor(
    var apiService: ApiService,
    dispatcher : CoroutineDispatcher = Dispatchers.IO
) :
    RepositoryDataRepo, BaseRepository(dispatcher) {
    override suspend fun getRepoData(): ResponseResource<RepoItem> {

        val response: ResponseResource<RepoItem> =
            executeNetworkCall { apiService.getRepoList() }
        return response
    }

}