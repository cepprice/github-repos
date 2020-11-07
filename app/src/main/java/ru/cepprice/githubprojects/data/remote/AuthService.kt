package ru.cepprice.githubprojects.data.remote

import retrofit2.Call
import retrofit2.http.*
import ru.cepprice.githubprojects.data.remote.model.AccessToken


interface AuthService {

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("/login/oauth/access_token")
    fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
    ): Call<AccessToken>
}
