package ru.cepprice.githubprojects.data.remote

import retrofit2.Response
import ru.cepprice.githubprojects.utils.Resource

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                val header = response.headers()["Link"]
                if (body != null) return Resource.Success(body, header)
            }
            return error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Resource.Error<T> {
        return Resource.Error<T>(message)
    }

    companion object {
        const val ERR_IN_PROCESS = "in_process"
        const val ERR_NO_COMMITS = "no_commits"
    }
}