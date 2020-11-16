package ru.cepprice.githubprojects.ui.fragment.add

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import ru.cepprice.githubprojects.data.local.model.SendRepo
import ru.cepprice.githubprojects.data.repository.Repository

class AddViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {

    private val accessToken =  MutableLiveData<String>()
    private val repoToAdd = MutableLiveData<SendRepo>()

    private val mRepos = accessToken.switchMap { token ->
        repository.getLiveAllRepos(token)
    }
    val repos = mRepos

    private val mGitignoreTemplates = repository.getLiveGitignoreTemplates()
    val gitignoreTemplates = mGitignoreTemplates

    private val mLicenses = repository.getLiveLicenses()
    val licenses = mLicenses

    private val mAddRepoResult = repoToAdd.switchMap { repo ->
        repository.createLiveRepo(accessToken.value!!, repo)
    }
    val addRepoResult = mAddRepoResult

    fun start(accessToken: String) {
        this.accessToken.value = accessToken
    }

    fun createRepo(repo: SendRepo) {
        repoToAdd.value = repo
    }
}