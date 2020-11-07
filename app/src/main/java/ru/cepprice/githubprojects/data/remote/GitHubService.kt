package ru.cepprice.githubprojects.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import ru.cepprice.githubprojects.data.remote.model.RepoJson

interface GitHubService {

    @GET("/user/repos?type=owner")
    suspend fun getAllUserOwnedRepos(
        @Header("Authorization") accessToken: String
    ): Call<List<RepoJson>>
}