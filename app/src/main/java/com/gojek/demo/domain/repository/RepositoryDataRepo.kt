package com.gojek.demo.domain

import com.gojek.demo.data.model.NetworkResponseWrapper
import com.gojek.demo.data.model.RepoItem
import com.gojek.demo.domain.models.ResponseResource
import retrofit2.Response

interface RepositoryDataRepo {
    suspend fun getRepoData() : ResponseResource<RepoItem>
}