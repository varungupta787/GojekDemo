package com.gojek.demo.data.repositories

import com.gojek.demo.data.model.RepoItem
import com.gojek.demo.data.remote.ApiService
import com.gojek.demo.di.AppModule
import com.gojek.demo.domain.RepositoryDataRepo
import com.gojek.demo.domain.models.ResponseResource
import com.gojek.demo.domain.repository.BaseRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class RepositoryRepoDataImpl @Inject constructor(
    var apiService: ApiService,
    @AppModule.IoDispatcher dispatcher : CoroutineDispatcher
) :
    RepositoryDataRepo, BaseRepository(dispatcher) {
    override suspend fun getRepoData(): ResponseResource<List<RepoItem>> {

        val response: ResponseResource<List<RepoItem>> =
            executeNetworkCall { apiService.getRepoList() }
        return response
    }

}