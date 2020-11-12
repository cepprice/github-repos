package ru.cepprice.githubprojects.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.cepprice.githubprojects.data.remote.AuthService
import ru.cepprice.githubprojects.data.remote.GitHubRemoteDataSource
import ru.cepprice.githubprojects.data.remote.GitHubService
import ru.cepprice.githubprojects.data.repository.Repository
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofitApi(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideAuthService(retrofit: Retrofit): AuthService =
            retrofit.create(AuthService::class.java)

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