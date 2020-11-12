package ru.cepprice.githubprojects.data.repository

import ru.cepprice.githubprojects.data.remote.GitHubRemoteDataSource
import ru.cepprice.githubprojects.utils.performDeleteOperation
import ru.cepprice.githubprojects.utils.performGetOperation
import ru.cepprice.githubprojects.utils.performPostOperation
import javax.inject.Inject

class Repository @Inject constructor(
    private val remoteDataSource: GitHubRemoteDataSource
) {

    suspend fun newGetAllRepos(accessToken: String) =
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

    fun getUser(accessToken: String) =
        performGetOperation { remoteDataSource.getUser(accessToken) }

    fun createRepo(
        accessToken: String,
        name: String,
        description: String = "",
        isPrivate: Boolean = false,
        isReadmeNeeded: Boolean = false
    ) =
        performPostOperation {
            remoteDataSource.createRepo(
                accessToken, name, description, isPrivate, isReadmeNeeded
            )
        }

    fun deleteRepo(
        accessToken: String,
        owner: String,
        repoName: String
    ) =
        performDeleteOperation { remoteDataSource.deleteRepo(accessToken, owner, repoName) }

}