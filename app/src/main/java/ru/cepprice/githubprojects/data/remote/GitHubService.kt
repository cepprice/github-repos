package ru.cepprice.githubprojects.data.remote

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import ru.cepprice.githubprojects.data.remote.model.AccessToken
import ru.cepprice.githubprojects.data.remote.model.branch.Branch
import ru.cepprice.githubprojects.data.remote.model.contributor.Contributor
import ru.cepprice.githubprojects.data.remote.model.repo.Repo
import ru.cepprice.githubprojects.data.remote.model.tag.Tag

interface GitHubService {

    /*
    *   GET
    * */

    @GET("/user/repos?type=owner")
    suspend fun getAllUserOwnedRepos(
        @Header("Authorization") accessToken: String
    ): Response<List<Repo>>


    // Get list of one branch (that will be first) to get branches count
    // from Link header
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


    //TODO
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("/login/oauth/access_token")
    fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
    ): Call<AccessToken>


    /*
    *   POST
    * */


    @POST("/user/repos")
    @Headers("Accept: application/vnd.github.v3+json")
    suspend fun createRepo(
        @Header("Authorization") accessToken: String,
        @Query("name") name: String,
        @Query("description") description: String,
        @Query("private") isPrivate: Boolean,
        @Query("auto_init") isReadmeSelected: Boolean,
        //@Query("gitignore_template") gitignoreType: String,
        //@Query("license_template") licenseType: String
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