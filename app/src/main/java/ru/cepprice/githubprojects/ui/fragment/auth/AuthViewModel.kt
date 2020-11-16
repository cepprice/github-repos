package ru.cepprice.githubprojects.ui.fragment.auth

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import ru.cepprice.githubprojects.data.repository.Repository

class AuthViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {

    private val params = MutableLiveData<TokenQueryParams>()

    private val mAccessToken = params.switchMap {
        repository.getLiveAccessToken(
            it.clientId, it.clientSecret, it.code
        )
    }
    val accessToken = mAccessToken

    fun start(queryParams: TokenQueryParams) {
        params.value = queryParams
    }
}

data class TokenQueryParams(
    val clientId: String,
    val clientSecret: String,
    val code: String
)