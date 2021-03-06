package ru.cepprice.githubprojects.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.cepprice.githubprojects.data.remote.GitHubRemoteDataSource
import ru.cepprice.githubprojects.data.remote.GitHubService
import ru.cepprice.githubprojects.data.repository.Repository
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder().
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()


    @Singleton
    @Provides
    fun provideRetrofitApi(client: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideGitHubService(retrofit: Retrofit): GitHubService =
        retrofit.create(GitHubService::class.java)

    @Singleton
    @Provides
    fun provideGitHubRemoteDataSource(gitHubService: GitHubService) =
        GitHubRemoteDataSource(gitHubService)

    @Singleton
    @Provides
    fun provideRepository(remoteDataSource: GitHubRemoteDataSource) =
        Repository(remoteDataSource)
}