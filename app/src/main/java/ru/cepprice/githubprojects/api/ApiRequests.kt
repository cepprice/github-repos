package ru.cepprice.githubprojects.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiRequests {

    @GET("user/repos")
    fun getRepos(): Call<List<RepoJson>>

    @FormUrlEncoded
    @POST("/login/oauth/authorize")
    fun getUserLogin(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("login") login: String,
    ): Call<ResponseBody?>?
}