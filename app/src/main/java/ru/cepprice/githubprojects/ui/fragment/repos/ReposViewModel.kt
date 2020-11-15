package ru.cepprice.githubprojects.ui.fragment.repos

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import kotlinx.coroutines.*
import ru.cepprice.githubprojects.data.local.model.RepoView
import ru.cepprice.githubprojects.data.remote.model.branch.Branch
import ru.cepprice.githubprojects.data.remote.model.contributor.Contributor
import ru.cepprice.githubprojects.data.remote.model.tag.Tag
import ru.cepprice.githubprojects.data.remote.model.user.User
import ru.cepprice.githubprojects.data.repository.Repository
import ru.cepprice.githubprojects.extensions.toRepoView
import ru.cepprice.githubprojects.utils.Resource
import ru.cepprice.githubprojects.utils.Utils.parseHeader

class ReposViewModel @ViewModelInject constructor(
    private val repository: Repository,
) : ViewModel() {

    private val token = MutableLiveData<String>()

    val repoViewsLd = MutableLiveData<Resource<ArrayList<RepoView>>>()

    private val mUser = token.switchMap { token ->
        repository.getLiveUser(token)
    }

    val user: LiveData<Resource<User>> = mUser

    fun start(accessToken: String) {
        this.token.value = accessToken
        CoroutineScope(Dispatchers.IO).launch {
            val reposResource = repository.getAllRepos(accessToken) // Get all repos
            if (passErrorIfExists(reposResource)) return@launch

            val repos = reposResource.data!!
            val repoViewsResource = Resource.Success(ArrayList<RepoView>())

            var visibleReposCounter = 0

            // Get branches, tags and commits count for each repo
             for (i in repos.indices) {
                 val repo = repos[i]
                 val branchesUrl = repo.branches_url.removeSuffix("{/branch}")
                 val tagsUrl = repo.tags_url
                 val contributorsUrl = repo.contributors_url

                 val (branchesResource, tagsResource, contributorsResource) =
                     getResources(accessToken, branchesUrl, tagsUrl, contributorsUrl)

                 // If got some error then emit error and stop function execution
                 if (passErrorIfExists(branchesResource) ||
                     passErrorIfExists(tagsResource) ||
                     passErrorIfExists(contributorsResource)
                 ) {
                     return@launch
                 }

                 val branchesCount: Int =
                     parseHeader(branchesResource.header) ?: branchesResource.data!!.size
                 val tagsCount: Int =
                     parseHeader(tagsResource.header) ?: tagsResource.data!!.size
                 val commitsCount: Int =
                     if (contributorsResource is Resource.NoContent) 0
                     else contributorsResource.data?.map { it.contributions }?.sum() ?: 0

                 repoViewsResource.data!!.add(
                     repo.toRepoView(
                         branchesCount,
                         tagsCount,
                         commitsCount
                     )
                 )

                 passDataToAdapterIfNeeded(
                     ++visibleReposCounter,
                     repos.size,
                     repoViewsResource
                 )
            }
        }
    }

    private suspend fun passDataToAdapterIfNeeded(
        pos: Int,
        size: Int,
        repoViewsResource: Resource.Success<java.util.ArrayList<RepoView>>
    ) {
        val apprVisibleReposAmount = 7
        val isLastInPaging = pos.rem(apprVisibleReposAmount) == 0
        val isLastInList = pos == size
        if (isLastInPaging || isLastInList) {
            withContext(Dispatchers.Main) {
                repoViewsLd.value = repoViewsResource
            }
        }
    }

    private suspend fun getResources(
        token: String,
        branchesUrl: String,
        tagsUrl: String,
        contributorsUrl: String
    ): Triple<Resource<List<Branch>>, Resource<List<Tag>>, Resource<List<Contributor>>> {
        val urlsList = listOf(branchesUrl, tagsUrl, contributorsUrl)
        val query = "?per_page=1"

        var branchesResource: Resource<List<Branch>> = Resource.Success(emptyList())
        var tagsResource: Resource<List<Tag>> = Resource.Success(emptyList())
        var contributorsResource: Resource<List<Contributor>> = Resource.Success(emptyList())

        val jobs: List<Job> = urlsList.map { url ->
            CoroutineScope(Dispatchers.IO).launch {
                when (url) {
                    branchesUrl -> {
                        branchesResource = repository.getBranches(
                            token,
                            branchesUrl.plus(query)
                        )
                    }
                    tagsUrl -> {
                        tagsResource = repository.getTags(
                            token,
                            tagsUrl.plus(query)
                        )
                    }
                    contributorsUrl -> {
                        contributorsResource = repository.getContributors(
                            token, contributorsUrl
                        )
                    }
                }
            }
        }
        jobs.joinAll()

        return Triple(branchesResource, tagsResource, contributorsResource)
    }

    private suspend fun <T> passErrorIfExists(resource: Resource<T>): Boolean {
        if ( resource !is Resource.Error) return false

        withContext(Dispatchers.Main) {
            repoViewsLd.value = Resource.Error(resource.errorMessage)
        }

        return true.also { Log.d("M_ReposViewModel", "${resource.errorMessage}") }
    }

}

