package ru.cepprice.githubprojects.data.remote

import javax.inject.Inject

class GitHubRemoteDataSource @Inject constructor(
    private val gitHubService: GitHubService
) : BaseDataSource() {

    suspend fun getAllOwnedRepos(accessToken: String) =
        getResult { gitHubService.getAllUserOwnedRepos(accessToken) }

    suspend fun getBranches(
        accessToken: String,
        branchesUrl: String
    ) = getResult { gitHubService.getBranches(accessToken, branchesUrl) }

    suspend fun getTags(
        accessToken: String,
        tagsUrl: String
    ) = getResult { gitHubService.getTags(accessToken, tagsUrl) }

    suspend fun getContributors(
        accessToken: String,
        contributorsUrl: String
    ) = getResult { gitHubService.getContributors(accessToken, contributorsUrl) }

    suspend fun createRepo(
        accessToken: String,
        name: String,
        description: String = "",
        isPrivate: Boolean = false,
        isReadmeNeeded: Boolean = false,
    ) = getResult { gitHubService.createRepo(
            accessToken, name, description, isPrivate, isReadmeNeeded
        ) }

    suspend fun deleteRepo(
        accessToken: String,
        owner: String,
        repoName: String
    ) = getResult { gitHubService.deleteRepo(accessToken, owner, repoName) }
}