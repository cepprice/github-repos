package ru.cepprice.githubprojects.ui.fragment.repos

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.cepprice.githubprojects.data.local.model.RepoView
import ru.cepprice.githubprojects.data.remote.model.branch.Branch
import ru.cepprice.githubprojects.data.remote.model.contributor.Contributor
import ru.cepprice.githubprojects.data.remote.model.repo.Repo
import ru.cepprice.githubprojects.data.remote.model.tag.Tag
import ru.cepprice.githubprojects.data.repository.Repository
import ru.cepprice.githubprojects.extensions.toRepoView
import ru.cepprice.githubprojects.utils.Resource
import ru.cepprice.githubprojects.utils.parseHeader

class ReposViewModel @ViewModelInject constructor(
    private val repository: Repository,
) : ViewModel() {

    val repoViewsLd = MutableLiveData<Resource<ArrayList<RepoView>>>()

    fun start(accessToken: String) {
        val accessToken = "token $accessToken"
        CoroutineScope(Dispatchers.IO).launch {
            val reposResource = repository.newGetAllRepos(accessToken) // Get all repos

            if (passErrorIfExists(reposResource)) return@launch

            var branchesResource: Resource<List<Branch>> = Resource.Success(emptyList())
            var tagsResource: Resource<List<Tag>> = Resource.Success(emptyList())
            var contributorsResource: Resource<List<Contributor>> = Resource.Success(emptyList())

            val repos = reposResource.data!!
            val repoViewsResource = Resource.Success(ArrayList<RepoView>())

            val apprVisibleReposAmount = 7
            var visibleReposCounter = 0

            // Get branches, tags and commits count for each repo
             for (i in repos.indices) {
                val repo = repos[i]
                val branchesUrl = repo.branches_url.removeSuffix("{/branch}")
                val tagsUrl = repo.tags_url
                val contributorsUrl = repo.contributors_url
                val urlsList = listOf(branchesUrl, tagsUrl, contributorsUrl)
                val query = "?per_page=1"

                val jobs: List<Job> = urlsList.map { url ->
                    CoroutineScope(Dispatchers.IO).launch {
                        when (url) {
                            branchesUrl -> {
                                branchesResource = repository.optGetBranchCount(
                                    accessToken,
                                    branchesUrl.plus(query)
                                )
                            }

                            tagsUrl -> {
                                tagsResource = repository.optGetTagCount(
                                    accessToken,
                                    tagsUrl.plus(query)
                                )
                            }

                            contributorsUrl -> {
                                contributorsResource = repository.optGetContributors(
                                    accessToken, contributorsUrl
                                )
                            }
                        }
                    }
                }
                jobs.joinAll()

                // If got some error then emit error and stop function execution
                if (passErrorIfExists(branchesResource) ||
                    passErrorIfExists(tagsResource) ||
                    passErrorIfExists(contributorsResource)) {
                    return@launch
                }

                // If resource doesn't have heeded (it means there is 0..1 tags or/and branches)
                val repeatedUrls = mutableListOf<String>()
                if (branchesResource.header == null) repeatedUrls.add(branchesUrl)
                if (tagsResource.header == null) repeatedUrls.add(tagsUrl)

                val repeatedJobs: List<Job> = repeatedUrls.map { url ->
                    CoroutineScope(Dispatchers.IO).launch {
                        when (url) {
                            branchesUrl -> {
                                branchesResource = repository.optGetBranchCount(
                                    accessToken, branchesUrl
                                )
                            }

                            tagsUrl -> {
                                tagsResource = repository.optGetTagCount(
                                    accessToken, tagsUrl
                                )
                            }
                        }
                    }
                }
                repeatedJobs.joinAll()

                if (passErrorIfExists(branchesResource) || passErrorIfExists(tagsResource)) {
                    return@launch
                }

                val branchesCount = parseHeader(branchesResource.header?: null) ?: branchesResource.data!!.size
                val tagsCount = parseHeader(tagsResource.header ?: null) ?: tagsResource.data!!.size
                val commitsCount = contributorsResource.data?.map { it.contributions }?.sum() ?: 0

                repoViewsResource.data!!.add(repo.toRepoView(branchesCount, tagsCount, commitsCount))

                if ((++visibleReposCounter).rem(apprVisibleReposAmount) == 0 ||
                        visibleReposCounter == repos.size) {
                    withContext(Dispatchers.Main){
                        repoViewsLd.value = repoViewsResource
                    }
                }
            }
        }
    }

    private suspend fun <T> passErrorIfExists(resource: Resource<T>): Boolean {
        if (resource is Resource.Success) return false

        withContext(Dispatchers.Main) {
            repoViewsLd.value = Resource.Error(resource.errorMessage)
        }

        return true.also { Log.d("M_ReposViewModel", "${resource.errorMessage}") }
    }

}

