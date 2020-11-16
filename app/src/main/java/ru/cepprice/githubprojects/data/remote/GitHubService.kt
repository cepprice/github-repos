package ru.cepprice.githubprojects.data.remote

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import ru.cepprice.githubprojects.data.local.model.SendRepo
import ru.cepprice.githubprojects.data.remote.model.AccessToken
import ru.cepprice.githubprojects.data.remote.model.branch.Branch
import ru.cepprice.githubprojects.data.remote.model.contributor.Contributor
import ru.cepprice.githubprojects.data.remote.model.license.License
import ru.cepprice.githubprojects.data.remote.model.repo.Repo
import ru.cepprice.githubprojects.data.remote.model.tag.Tag
import ru.cepprice.githubprojects.data.remote.model.user.User

interface GitHubService {

    /*
    *   GET
    * */

    @GET("/user")
    @Headers("Accept: application/vnd.github.v3+json")
    suspend fun getUser(
        @Header("Authorization") accessToken: String
    ): Response<User>


    @GET("/user/repos?type=owner")
    @Headers("Accept: application/vnd.github.v3+json")
    suspend fun getAllUserOwnedRepos(
        @Header("Authorization") accessToken: String
    ): Response<List<Repo>>


    @GET
    @Headers("Accept: application/vnd.github.v3+json")
    suspend fun getBranches(
        @Header("Authorization") accessToken: String,
        @Url branchUrl: String
    ): Response<List<Branch>>


    @GET
    @Headers("Accept: application/vnd.github.v3+json")
    suspend fun getTags(
        @Header("Authorization") accessToken: String,
        @Url branchUrl: String
    ): Response<List<Tag>>


    @GET
    @Headers("Accept: application/vnd.github.v3+json")
    suspend fun getContributors(
        @Header("Authorization") accessToken: String,
        @Url contributorsUrl: String
    ) : Response<List<Contributor>>


    @GET("/gitignore/templates")
    @Headers("Accept: application/vnd.github.v3+json")
    suspend fun getGitignoreTemplates(): Response<List<String>>


    @GET("/licenses")
    @Headers("Accept: application/vnd.github.v3+json")
    suspend fun getLicenses(): Response<List<License>>


    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("https://github.com/login/oauth/access_token")
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
    ): Response<AccessToken>


    /*
    *   POST
    * */


    @POST("/user/repos")
    @Headers("Accept: application/vnd.github.v3+json")
    suspend fun createRepo(
        @Header("Authorization") accessToken: String,
        @Body repo: SendRepo
        ): Response<Repo>


    /*
    *   DELETE
    *  */


    @DELETE("/repos/{owner}/{repo}")
    @Headers("Accept: application/vnd.github.v3+json")
    suspend fun deleteRepo(
        @Header("Authorization") accessToken: String,
        @Path("owner") owner: String,
        @Path("repo") repoName: String
        ) : Response<Void>

}