package ru.cepprice.githubprojects.data.repository

import ru.cepprice.githubprojects.data.local.model.SendRepo
import ru.cepprice.githubprojects.data.remote.GitHubRemoteDataSource
import ru.cepprice.githubprojects.utils.performDeleteOperation
import ru.cepprice.githubprojects.utils.performGetOperation
import ru.cepprice.githubprojects.utils.performPostOperation
import javax.inject.Inject

class Repository @Inject constructor(
    private val remoteDataSource: GitHubRemoteDataSource
) {

    suspend fun getAllRepos(accessToken: String) =
        remoteDataSource.getAllOwnedRepos(accessToken)

    suspend fun getBranches(
        accessToken: String,
        branchesUrl: String
    ) = remoteDataSource.getBranches(accessToken, branchesUrl)

    suspend fun getTags(
        accessToken: String,
        tagsUrl: String
    ) = remoteDataSource.getTags(accessToken, tagsUrl)

    suspend fun getContributors(
        accessToken: String,
        contributorsUrl: String
    ) = remoteDataSource.getContributors(accessToken, contributorsUrl)

    fun getLiveAllRepos(accessToken: String) =
        performGetOperation { remoteDataSource.getAllOwnedRepos(accessToken) }

    fun getLiveUser(accessToken: String) =
        performGetOperation { remoteDataSource.getUser(accessToken) }

    fun getLiveGitignoreTemplates() =
        performGetOperation { remoteDataSource.getGitignoreTemplates() }

    fun getLiveLicenses() =
        performGetOperation { remoteDataSource.getLicenses() }

    fun getLiveAccessToken(
        clientId: String,
        clientSecret: String,
        code: String
    ) =
        performGetOperation { remoteDataSource.getAccessToken(clientId, clientSecret, code) }

    fun createLiveRepo(
        accessToken: String,
        repo: SendRepo
    ) =
        performPostOperation {
            remoteDataSource.createRepo(
                accessToken, repo
            )
        }

    fun deleteRepo(
        accessToken: String,
        owner: String,
        repoName: String
    ) =
        performDeleteOperation { remoteDataSource.deleteRepo(accessToken, owner, repoName) }

}