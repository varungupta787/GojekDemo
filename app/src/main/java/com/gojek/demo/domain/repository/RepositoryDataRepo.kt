package com.gojek.demo.domain

import com.gojek.demo.data.model.RepoItem
import com.gojek.demo.domain.models.ResponseResource

interface RepositoryDataRepo {
    suspend fun getRepoData() : ResponseResource<RepoItem>
}