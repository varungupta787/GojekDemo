package com.gojek.demo.data.remote

import com.gojek.demo.data.NetworkUtils
import com.gojek.demo.data.model.RepoItem
import com.gojek.demo.domain.models.ResponseResource
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET(NetworkUtils.REPO_URL)
    suspend fun getRepoList(): Response<List<RepoItem>>
}