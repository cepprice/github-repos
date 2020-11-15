package ru.cepprice.githubprojects.data.repository

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

    fun createRepo(
        accessToken: String,
        name: String,
        isPrivate: Boolean = false,
        isReadmeNeeded: Boolean = false
    ) =
        performPostOperation {
            remoteDataSource.createRepo(
                accessToken, name, isPrivate, isReadmeNeeded
            )
        }

    fun deleteRepo(
        accessToken: String,
        owner: String,
        repoName: String
    ) =
        performDeleteOperation { remoteDataSource.deleteRepo(accessToken, owner, repoName) }

}