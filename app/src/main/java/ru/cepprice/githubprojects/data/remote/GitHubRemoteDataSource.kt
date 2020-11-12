package ru.cepprice.githubprojects.data.remote

import ru.cepprice.githubprojects.data.remote.model.branch.Branch
import ru.cepprice.githubprojects.data.remote.model.tag.Tag
import ru.cepprice.githubprojects.utils.Resource
import javax.inject.Inject

class GitHubRemoteDataSource @Inject constructor(
    private val gitHubService: GitHubService
) : BaseDataSource() {

    suspend fun getAllOwnedRepos(accessToken: String) =
        getResult { gitHubService.getAllUserOwnedRepos(accessToken) }

    private suspend fun getBranches(
        accessToken: String,
        branchesUrl: String
    ) = getResult { gitHubService.getBranches(accessToken, branchesUrl) }

    suspend fun optGetBranchCount(
        accessToken: String,
        branchesUrl: String
    ) = getResult { gitHubService.getBranches(accessToken, branchesUrl) }

    suspend fun optGetTagCount(
        accessToken: String,
        tagsUrl: String
    ) = getResult { gitHubService.getTags(accessToken, tagsUrl) }

    suspend fun optGetContributors(
        accessToken: String,
        contributorsUrl: String
    ) = getResult { gitHubService.getContributors(accessToken, contributorsUrl) }

    suspend fun getBranchesOfAllRepos(
        accessToken: String,
        branchesUrls: List<String>
    ): Resource<List<List<Branch>>> {
        var errorMessage = ""
        val listOfBranchesList = mutableListOf<Resource<List<Branch>>>()
        branchesUrls.forEach { branchesUrl ->
            val resource = getBranches(accessToken, branchesUrl.removeSuffix("{/branch}"))
            if (resource is Resource.Error) {
                errorMessage = resource.errorMessage!!
            }
            listOfBranchesList.add(resource)
        }

        return if (errorMessage.isNotBlank()) {
            Resource.Error(errorMessage)
        } else {
            Resource.Success(listOfBranchesList.map { resource ->  resource.data!! })
        }
    }

    private suspend fun getTags(
        accessToken: String,
        tagsUrl: String
    ) = getResult { gitHubService.getTags(accessToken, tagsUrl) }

    suspend fun getTagsOfAllRepos(
        accessToken: String,
        tagsUrls: List<String>
    ): Resource<List<List<Tag>>> {
        var errorMessage = ""
        val listOfTagsList = mutableListOf<Resource<List<Tag>>>()
        tagsUrls.forEach { branchesUrl ->
            val resource = getTags(accessToken, branchesUrl)
            if (resource is Resource.Error) {
                errorMessage = resource.errorMessage!!
            }
            listOfTagsList.add(resource)
        }

        return if (errorMessage.isNotBlank()) {
            Resource.Error(errorMessage)
        } else {
            Resource.Success(listOfTagsList.map { resource ->  resource.data!! })
        }
    }


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