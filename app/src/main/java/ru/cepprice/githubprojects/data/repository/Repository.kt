package ru.cepprice.githubprojects.data.repository

import ru.cepprice.githubprojects.data.remote.GitHubRemoteDataSource
import ru.cepprice.githubprojects.utils.performDeleteOperation
import ru.cepprice.githubprojects.utils.performPostOperation
import javax.inject.Inject

class Repository @Inject constructor(
    private val remoteDataSource: GitHubRemoteDataSource
) {

    suspend fun newGetAllRepos(accessToken: String) =
        remoteDataSource.getAllOwnedRepos(accessToken)

    suspend fun optGetBranchCount(
        accessToken: String,
        branchesUrl: String
    ) = remoteDataSource.getBranches(accessToken, branchesUrl)

    suspend fun optGetTagCount(
        accessToken: String,
        tagsUrl: String
    ) = remoteDataSource.getTags(accessToken, tagsUrl)

    suspend fun optGetContributors(
        accessToken: String,
        contributorsUrl: String
    ) = remoteDataSource.getContributors(accessToken, contributorsUrl)

    fun createRepo(
        accessToken: String,
        name: String,
        description: String = "",
        isPrivate: Boolean = false,
        isReadmeNeeded: Boolean = false
    ) =
        performPostOperation {remoteDataSource.createRepo(
            accessToken, name, description, isPrivate, isReadmeNeeded
        )}

    fun deleteRepo(
        accessToken: String,
        owner: String,
        repoName: String
    ) =
        performDeleteOperation { remoteDataSource.deleteRepo(accessToken, owner, repoName) }

}