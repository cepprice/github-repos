package ru.cepprice.githubprojects.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private const val API_BASE_URL = "https://api.github.com"
    private const val GIT_BASE_URL = "https://github.com"

    private fun getApiGitRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getAuthGitRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GIT_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authGitHub: AuthGitHubApi = getAuthGitRetrofit().create(AuthGitHubApi::class.java)
    val apiGitHub: GitHubApi = getApiGitRetrofit().create(GitHubApi::class.java)

    val REDIRECT_URI = "cepprice://callback"
}