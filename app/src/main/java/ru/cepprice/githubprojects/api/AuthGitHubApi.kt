package ru.cepprice.githubprojects.api

import retrofit2.Call
import retrofit2.http.*
import ru.cepprice.githubprojects.api.model.AccessToken
import ru.cepprice.githubprojects.api.model.RepoJson


interface GitHubAuthApi {

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("/login/oauth/access_token")
    fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
    ): Call<AccessToken>
}
