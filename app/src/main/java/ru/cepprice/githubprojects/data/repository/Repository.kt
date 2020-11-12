package ru.cepprice.githubprojects.data.repository

import ru.cepprice.githubprojects.data.remote.GitHubRemoteDataSource
import ru.cepprice.githubprojects.utils.performDeleteOperation
import ru.cepprice.githubprojects.utils.performGetOperation
import ru.cepprice.githubprojects.utils.performPostOperation
import javax.inject.Inject

class Repository @Inject constructor(
    private val remoteDataSource: GitHubRemoteDataSource
) {
    fun getAllRepos(accessToken: String) =
        performGetOperation{remoteDataSource.getAllOwnedRepos(accessToken) }

    suspend fun newGetAllRepos(accessToken: String) =
        remoteDataSource.getAllOwnedRepos(accessToken)

    fun getBranches(
        accessToken: String,
        branchesUrls: List<String>
    ) =
        performGetOperation { remoteDataSource.getBranchesOfAllRepos(accessToken, branchesUrls) }

    suspend fun optGetBranchCount(
        accessToken: String,
        branchesUrl: String
    ) = remoteDataSource.optGetBranchCount(accessToken, branchesUrl)

    suspend fun optGetTagCount(
        accessToken: String,
        tagsUrl: String
    ) = remoteDataSource.optGetTagCount(accessToken, tagsUrl)

    suspend fun optGetContributors(
        accessToken: String,
        contributorsUrl: String
    ) = remoteDataSource.optGetContributors(accessToken, contributorsUrl)

    fun getTags(
        accessToken: String,
        tagsUrls: List<String>
    ) =
        performGetOperation { remoteDataSource.getTagsOfAllRepos(accessToken, tagsUrls) }


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