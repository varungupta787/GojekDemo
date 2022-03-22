package com.gojek.demo.data.remote

import com.gojek.demo.data.NetworkUtils
import com.gojek.demo.data.model.RepoItem
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET(NetworkUtils.REPO_URL)
    suspend fun getRepoList(): Response<List<RepoItem>>
}